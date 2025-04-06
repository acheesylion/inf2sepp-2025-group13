package system_tests;

import controller.AdminStaffController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class FAQSystemTests extends TUITest {

    /**
     * Test the ability for administrators to add FAQ items
     * Including creating new topics and adding FAQ items
     */
    @Test
    public void testAddFAQItem() throws URISyntaxException, IOException, ParseException {
        setMockInput("-2", "Test Topic", "Test Question?", "Test Answer", "n", "-1");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        context.currentUser = new AuthenticatedUser("admin1@hindeburg.ac.uk", "AdminStaff");
        AdminStaffController adminController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());

        startOutputCapture();
        adminController.manageFAQ();

        assertOutputContains("Created topic 'Test Topic'");
        assertOutputContains("Created new FAQ item");

        FAQ faq = context.getFAQ();
        assertEquals(1, faq.getSections().size());
        FAQSection section = faq.getSections().get(0);
        assertEquals("Test Topic", section.getTopic());
        assertEquals(1, section.getItems().size());
        assertEquals("Test Question?", section.getItems().get(0).getQuestion());
        assertEquals("Test Answer", section.getItems().get(0).getAnswer());
        assertFalse(section.getItems().get(0).hasCourseTag());
    }

    /**
     * Test adding FAQ items with course tags
     */
    @Test
    public void testAddFAQItemWithCourseTag() throws URISyntaxException, IOException, ParseException {
        setMockInput("-2", "Course Topic", "Course Question?", "Course Answer", "y", "INF2", "-1");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        AdminStaffController adminController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        context.currentUser = new AuthenticatedUser("admin1@hindeburg.ac.uk", "AdminStaff");

        startOutputCapture();

        adminController.manageFAQ();

        assertOutputContains("Created topic 'Course Topic'");
        assertOutputContains("FAQ item will be tagged with course code: INF2");
        assertOutputContains("Created new FAQ item");

        FAQ faq = context.getFAQ();
        assertEquals(1, faq.getSections().size());
        FAQSection section = faq.getSections().get(0);
        assertEquals("Course Topic", section.getTopic());
        assertEquals(1, section.getItems().size());
        assertEquals("Course Question?", section.getItems().get(0).getQuestion());
        assertEquals("Course Answer", section.getItems().get(0).getAnswer());
        assertTrue(section.getItems().get(0).hasCourseTag());
        assertEquals("INF2", section.getItems().get(0).getCourseTag());
    }

    /**
     * Testing the FAQ hierarchy
     * Creating Topics and Subtopics
     */
    @Test
    public void testFAQHierarchy() throws URISyntaxException, IOException, ParseException {
        setMockInput("-2", "Parent Topic", "Parent Question?", "Parent Answer", "n", "-1",
                "0", "-2", "y", "Child Topic", "Child Question?", "Child Answer", "n", "-1", "-1");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        AdminStaffController adminController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        context.currentUser = new AuthenticatedUser("admin1@hindeburg.ac.uk", "AdminStaff");

        adminController.manageFAQ();

        startOutputCapture();
        adminController.manageFAQ();

        assertOutputContains("Created topic 'Child Topic' under 'Parent Topic'");
        assertOutputContains("Created new FAQ item");

        FAQ faq = context.getFAQ();
        assertEquals(1, faq.getSections().size());
        FAQSection parentSection = faq.getSections().get(0);
        assertEquals("Parent Topic", parentSection.getTopic());
        assertEquals(1, parentSection.getItems().size());
        assertEquals(1, parentSection.getSubsections().size());

        FAQSection childSection = parentSection.getSubsections().get(0);
        assertEquals("Child Topic", childSection.getTopic());
        assertEquals(1, childSection.getItems().size());
        assertEquals(parentSection, childSection.getParent());
    }

    /**
     * Test the function of deleting FAQ items
     */
    @Test
    public void testRemoveFAQItem() throws URISyntaxException, IOException, ParseException {
        setMockInput("-2", "Remove Test", "Question 1", "Answer 1", "n", "-1",
                "0", "-2", "n", "Question 2", "Answer 2", "n", "-1", "-1",
                "0", "-3", "0", "-1", "-1");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        AdminStaffController adminController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());

        context.currentUser = new AuthenticatedUser("admin1@hindeburg.ac.uk", "AdminStaff");

        adminController.manageFAQ();

        adminController.manageFAQ();

        startOutputCapture();
        adminController.manageFAQ();

        // 验证输出
        assertOutputContains("FAQ item removed successfully");

        // 验证FAQ结构
        FAQ faq = context.getFAQ();
        assertEquals(1, faq.getSections().size());
        FAQSection section = faq.getSections().get(0);
        assertEquals("Remove Test", section.getTopic());
        assertEquals(1, section.getItems().size());
        assertEquals("Question 2", section.getItems().get(0).getQuestion());
    }

    /**
     * Test the function that when deleting the last FAQ item in a topic, the topic is deleted and the subtopics are moved up
     */
    @Test
    public void testRemoveLastItemInSection() throws URISyntaxException, IOException, ParseException {
        setMockInput("-2", "Parent Topic", "Parent Question", "Parent Answer", "n", "-1",
                "0", "-2", "n", "Child Topic", "Child Question", "Child Answer", "n", "-1", "-1",
                "0", "-3", "0", "-1", "-1");

        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        AdminStaffController adminController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());

        context.currentUser = new AuthenticatedUser("admin1@hindeburg.ac.uk", "AdminStaff");

        adminController.manageFAQ();

         adminController.manageFAQ();


        startOutputCapture();
        adminController.manageFAQ();

        // 验证输出
        assertOutputContains("item removed successfully");

        // 验证子主题已上移到根级别
        FAQ faq = context.getFAQ();
        assertEquals(1, faq.getSections().size());
        FAQSection section = faq.getSections().get(0);

        assertEquals("Parent Topic", section.getTopic());
        assertNull(section.getParent());
    }

    /**
     * Testing FAQ subscription feature
     */
    @Test
    public void testFAQSubscription() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);

        // 用户订阅FAQ主题
        String testEmail = "test@example.com";
        String topicName = "Test Topic";
        assertTrue(context.registerForFAQUpdates(testEmail, topicName));

        // 验证订阅成功
        assertEquals(1, context.usersSubscribedToFAQTopic(topicName).size());
        assertTrue(context.usersSubscribedToFAQTopic(topicName).contains(testEmail));

        // 取消订阅
        assertTrue(context.unregisterForFAQUpdates(testEmail, topicName));
        assertEquals(0, context.usersSubscribedToFAQTopic(topicName).size());
    }
}
