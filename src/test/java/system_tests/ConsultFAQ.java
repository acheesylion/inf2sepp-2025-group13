package system_tests;

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

public class ConsultFAQ extends TUITest {

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
}