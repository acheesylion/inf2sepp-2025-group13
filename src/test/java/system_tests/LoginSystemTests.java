package system_tests;

import controller.AdminStaffController;
import controller.GuestController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.AuthenticatedUser;
import model.Inquiry;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


public class LoginSystemTests extends TUITest {


    @Test
    public void testLoginAsAdminStaff() throws URISyntaxException, IOException, ParseException {
        setMockInput("admin1", "admin1pass");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        guestController.login();
        assertOutputContains("Logged in as admin1");
        assertInstanceOf(AuthenticatedUser.class, context.currentUser);
        assertEquals("AdminStaff", ((AuthenticatedUser) context.currentUser).getRole());
    }

    @Test
    public void testLoginAsTeachingStaff() throws URISyntaxException, IOException, ParseException {
        setMockInput("teacher1", "teacher1pass");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        guestController.login();
        assertOutputContains("Logged in as teacher1");
        assertInstanceOf(AuthenticatedUser.class, context.currentUser);
        assertEquals("TeachingStaff", ((AuthenticatedUser) context.currentUser).getRole());
    }

    @Test
    public void testAdminRespondsToInquiry() throws Exception {
        setMockInput("0", "1", "Sure, submit by tomorrow", "-1", "-1");
        // Set up the test environment
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MockEmailService emailService = new MockEmailService();

        // Add one pending inquiry
        Inquiry inquiry = new Inquiry("student@example.com", "Late submission", "I missed the deadline, can I still submit?", null);
        context.inquiries.add(inquiry);

        // Simulate input:
        // 0 => select first inquiry from the list
        // 1 => "Respond to inquiry"
        // "Sure, submit by tomorrow" => response text
        // -1 => return to inquiry list
        // -1 => return to main menu

        AdminStaffController admin = new AdminStaffController(context, view, new MockAuthenticationService(), emailService);

        // Capture the output
        startOutputCapture();

        // Run the method
        admin.manageInquiries();

        // Check that output contains relevant info
        assertOutputContains("Pending inquiries");
        assertOutputContains("Respond to inquiry");
        assertOutputContains("Your inquiry has been recorded");
    }
}