package controller;

import external.AuthenticationService;
import external.EmailService;
import model.AuthenticatedUser;
import model.Inquiry;
import model.SharedContext;
import view.View;

import java.util.List;

/**
 * Controller class responsible for handling actions by staff members,
 * such as viewing and responding to user inquiries.
 */
public class StaffController extends Controller {

    /**
     * Constructs a new StaffController with the provided shared context, view, authentication, and email services.
     *
     * @param sharedContext the shared application context
     * @param view the user interface view for staff
     * @param auth the authentication service
     * @param email the email service used to send responses
     */
    public StaffController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Extracts and returns an array of inquiry subject titles from a list of inquiries.
     *
     * @param inquiries the list of inquiries
     * @return an array of stripped subject titles from the inquiries
     */
    protected String[] getInquiryTitles(List<Inquiry> inquiries) {
        String[] inquiryTitles = new String[inquiries.size()];

        for (int i = 0; i < inquiryTitles.length; ++i) {
            inquiryTitles[i] = inquiries.get(i).getSubject().strip();
        }

        return inquiryTitles;
    }

    /**
     * Allows a staff member to respond to a specific inquiry. Prompts for a subject and response body,
     * sends an email to the inquirer, removes the inquiry from the system, and displays a success message.
     *
     * @param inquiry the inquiry to respond to
     */
    protected void respondToInquiry(Inquiry inquiry) {
        String subject = view.getInput("Enter subject: ");
        String response = view.getInput("Enter response:\n");
        String currentEmail = ((AuthenticatedUser) sharedContext.currentUser).getEmail();
        email.sendEmail(currentEmail, inquiry.getInquirerEmail(), subject, response);
        sharedContext.inquiries.remove(inquiry);
        view.displaySuccess("Email response sent!");
    }
}
