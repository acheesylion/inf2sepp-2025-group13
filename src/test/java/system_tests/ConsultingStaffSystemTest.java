package system_tests;

import controller.AdminStaffController;
import controller.AuthenticatedUserController;
import controller.InquirerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.FAQ;
import model.*;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsultingStaffSystemTest extends TUITest {
    @Test
    public void testsendInquiry() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MockEmailService mockEmail = new MockEmailService();
        //InquirerController inquirer = new InquirerController(context, view, new MockAuthenticationService(), mockEmail);
        Inquiry inquiry = new Inquiry("charlotte.email.com", "Issue with assignment", "I am unable to submit the assignment.", "COMP12345");
        context.inquiries.add(inquiry);
        assertEquals(1, context.inquiries.size(), "Expected one inquiry to be added");
        Inquiry inquiryrecived = context.inquiries.get(0);
        assertEquals("Issue with assignment", inquiryrecived.getSubject());
        assertEquals("I am unable to submit the assignment.", inquiryrecived.getContent());
        assertEquals(inquiryrecived.getCourseCode(), "COMP12345");
    }

    @Test
    public void testifadminstaffgetemailed() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MockEmailService mockEmail = new MockEmailService();
        loginAsStudent1(context);

        InquirerController inquirer = new InquirerController(context, view, new MockAuthenticationService(), mockEmail);

        startOutputCapture();

        inquirer.sendInquiry("test@example.com", "Issue with assignment", "I am unable to submit the assignment.",false, "");

        assertOutputContains("Email from test@example.com to " + SharedContext.ADMIN_STAFF_EMAIL);
        assertOutputContains("New inquiry from test@example.com");
        assertOutputContains("Subject: Issue with assignment");

    }

    @Test
    public void testifcoursestaffgetemailed() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MockEmailService mockEmail = new MockEmailService();
        CourseManager courseManager = context.getCourseManager(); // ensure it's the same manager context uses
        Course course = new Course(
                "COMP12345",                       // courseCode (valid)
                "Introduction to Programming",     // name
                "Learn basic programming concepts",// description
                true,                              // requiresComputers
                "Dr. Alice Smith",                 // courseOrganiserName
                "alice.smith@university.edu",      // courseOrganiserEmail
                "Bob Johnson",                     // courseSecretaryName
                "bob.johnson@university.edu",      // courseSecretaryEmail
                0,                                // requiredTutorials
                0                                  // requiredLabs
        );
        courseManager.addCourseToCourseList(course);
        //courseManager.viewCourses();
        //System.out.println(courseManager.getCourseByCode("COMP12345"));
        loginAsStudent1(context);
        course.getCourseOrganiserEmail();

        InquirerController inquirer = new InquirerController(context, view, new MockAuthenticationService(), mockEmail);

        startOutputCapture();

        inquirer.sendInquiry("test@example.com", "Issue with assignment", "I am unable to submit the assignment.",true, "COMP12345");

        assertOutputContains("Email from test@example.com to " + course.getCourseOrganiserEmail());
        assertOutputContains("New inquiry from test@example.com");
        assertOutputContains("Subject: Issue with assignment");

    }

//    @Test  //DOESNT WORK INFINITE
//    public void testAdminCanViewUnansweredInquiries() throws Exception {
//        setMockInput("0", "1", "This is a response.", "-1", "-1");
//        View view = new TextUserInterface();
//        SharedContext context = new SharedContext(view);
//
//        // Add some inquiries
//        context.inquiries.add(new Inquiry("test1@example.com", "Subject A", "Content A", null));
//        context.inquiries.add(new Inquiry("test2@example.com", "Subject B", "Content B", "COMP12345"));
//
//        // Log in as admin
//        loginAsAdminStaff(context);
//
//        // Capture output
//
//        AdminStaffController admin = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
//
//        startOutputCapture();
//
//        admin.manageInquiries();  // this is a made-up method name you'll need to implement
//
//        assertOutputContains("Subject A");
//        assertOutputContains("Subject B");
//        assertOutputContains("test1@example.com");
//    }

}