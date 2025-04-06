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

public class ConsultFAQSystemTest extends TUITest {

    @Test  //DOESNT WORK INFINITE
    public void testviewFAQSection() throws URISyntaxException, IOException, ParseException {
        // Setup
        setMockInput("0", "-1", "-1");

        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        FAQ faq = sharedContext.getFAQ();

        // Add a section
        FAQSection section1 = new FAQSection("General Information");
        FAQSection section2 = new FAQSection("more questions");
        faq.addSection(section1);
        faq.addSection(section2);
        FAQItem item1 = new FAQItem("question 1?", "answer 1");
        section1.getItems().add(item1);
        //FAQItem item2 = new FAQItem("question 2?", "answer 2");
       // section1.getItems().add(item2);
        FAQItem item3 = new FAQItem("question 3?", "answer 3");
        section2.getItems().add(item3);
        FAQItem item4 = new FAQItem("question 4?", "answer 4");
        section2.getItems().add(item4);

        InquirerController inquirerController = new InquirerController(sharedContext, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        inquirerController.consultFAQ();

        assertOutputContains("question 1?");

    }
    @Test  //DOESNT WORK INFINITE
    public void testviewFAQSubSection() throws URISyntaxException, IOException, ParseException {
        // Setup
        setMockInput("0", "0", "-1", "-1","-1");

        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        FAQ faq = sharedContext.getFAQ();

        // Add a section
        FAQSection section1 = new FAQSection("General Information");
        FAQSection section2 = new FAQSection("more questions");
        faq.addSection(section1);
        faq.addSection(section2);
        FAQItem item1 = new FAQItem("question 1?", "answer 1");
        section1.getItems().add(item1);
        FAQSection subsection = new FAQSection("SubSection");
        section1.addSubsection(subsection);
        FAQItem item2 = new FAQItem("question 2?", "answer 2");
        subsection.getItems().add(item2);
        FAQItem item3 = new FAQItem("question 3?", "answer 3");
        section2.getItems().add(item3);
        FAQItem item4 = new FAQItem("question 4?", "answer 4");
        section2.getItems().add(item4);

        InquirerController inquirerController = new InquirerController(sharedContext, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        inquirerController.consultFAQ();

        assertOutputContains("question 2?");

    }

}