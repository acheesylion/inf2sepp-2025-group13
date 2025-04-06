package controller;

import external.AuthenticationService;
import external.EmailService;
import model.AuthenticatedUser;
import model.Inquiry;
import model.SharedContext;
import view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling actions specific to teaching staff users.
 * Extends the general {@link StaffController} functionality to manage inquiries assigned to the current user.
 */
public class TeachingStaffController extends StaffController {

    /**
     * Constructs a {@code TeachingStaffController} with the necessary context, view, and external services.
     *
     * @param sharedContext the shared application context containing user and inquiry data
     * @param view the view interface used to display and collect user interaction
     * @param auth the authentication service for verifying users
     * @param email the email service used to send notifications
     */
    public TeachingStaffController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Allows the teaching staff member to view and respond to inquiries assigned to them.
     * Filters inquiries based on the current user's email, displays a menu of those inquiries,
     * and provides the ability to respond to selected ones.
     */
    public void manageReceivedInquiries() {
        String userEmail = ((AuthenticatedUser) sharedContext.currentUser).getEmail();
        List<Inquiry> assignedInquiries = new ArrayList<>();
        for (Inquiry inquiry : sharedContext.inquiries) {
            if (userEmail.equals(inquiry.getAssignedTo())) {
                assignedInquiries.add(inquiry);
            }
        }
        String[] inquiryTitles = getInquiryTitles(assignedInquiries);

        while (true) {
            view.displayInfo("Assigned inquiries");
            int selection = selectFromMenu(inquiryTitles, "Back to main menu");
            if (selection == -1) {
                return;
            }
            Inquiry selectedInquiry = assignedInquiries.get(selection);

            while (true) {
                view.displayDivider();
                view.displayInquiry(selectedInquiry);
                view.displayDivider();
                String[] followUpOptions = { "Respond to inquiry" };
                int followUpSelection = selectFromMenu(followUpOptions, "Back to assigned inquiries");

                if (followUpSelection == -1) {
                    break;
                } else if (followUpOptions[followUpSelection].equals("Respond to inquiry")) {
                    respondToInquiry(selectedInquiry);
                    inquiryTitles = getInquiryTitles(sharedContext.inquiries); // required to remove responded inquiry from titles
                    break;
                }
            }
        }
    }
}
