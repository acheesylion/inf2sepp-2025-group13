package system_tests;

import controller.AdminStaffController;
import controller.InquirerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;
import model.FAQ;
import model.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class AddFAQQASystemTest extends TUITest {

        @Test
        public void testAddFAQSection() throws URISyntaxException, IOException, ParseException {
            // Setup
            View view = new TextUserInterface();
            SharedContext sharedContext = new SharedContext(view);
            FAQ faq = sharedContext.getFAQ();

            // Add a section
            FAQSection section = new FAQSection("General Information");
            faq.addSection(section);

            // Verify section was added
            assertEquals(1, faq.getSections().size(), "FAQ should contain one section.");
            assertEquals("General Information", faq.getSections().get(0).getTopic(), "Section name should match.");
        }

        @Test
        public void testAddMultipleFAQSections() throws URISyntaxException, IOException, ParseException {
            // Setup
            View view = new TextUserInterface();
            SharedContext sharedContext = new SharedContext(view);
            FAQ faq = sharedContext.getFAQ();

            // Add multiple sections
            FAQSection section1 = new FAQSection("General Information");
            FAQSection section2 = new FAQSection("Specific Information");
            faq.addSection(section1);
            faq.addSection(section2);

            // Verify both sections exist
            assertEquals(2, faq.getSections().size(), "FAQ should contain two sections.");
            assertEquals("General Information", faq.getSections().get(0).getTopic(), "First section should be 'General Information'.");
            assertEquals("Specific Information", faq.getSections().get(1).getTopic(), "Second section should be 'Specific Information'.");
        }

        @Test
        public void testAddFAQItemToSection() throws URISyntaxException, IOException, ParseException {
            // Setup
            View view = new TextUserInterface();
            SharedContext sharedContext = new SharedContext(view);
            FAQ faq = sharedContext.getFAQ();
            FAQSection section = new FAQSection("General Information");
            faq.addSection(section);



            // Add FAQ item
            FAQItem item = new FAQItem("What is this system?", "This system helps manage university courses.");
            section.getItems().add(item);

            // Verify item was added
            assertEquals(1, section.getItems().size(), "Section should contain one item.");
            assertEquals("What is this system?", section.getItems().get(0).getQuestion(), "Question should match.");
            assertEquals("This system helps manage university courses.", section.getItems().get(0).getAnswer(), "Answer should match.");
        }

        @Test
        public void testAddMultipleFAQItemsToSection() throws URISyntaxException, IOException, ParseException {
            // Setup
            View view = new TextUserInterface();
            SharedContext sharedContext = new SharedContext(view);
            FAQ faq = sharedContext.getFAQ();
            FAQSection section = new FAQSection("General Information");
            faq.addSection(section);

            // Add multiple FAQ items
            FAQItem item1 = new FAQItem("What is this system?", "This system helps manage university courses.");
            FAQItem item2 = new FAQItem("Why is this system?", "University students need help.");
            section.getItems().add(item1);
            section.getItems().add(item2);

            // Verify both items exist
            assertEquals(2, section.getItems().size(), "Section should contain two items.");
            assertEquals("What is this system?", section.getItems().get(0).getQuestion(), "First item question should match.");
            assertEquals("Why is this system?", section.getItems().get(1).getQuestion(), "Second item question should match.");
        }


    }