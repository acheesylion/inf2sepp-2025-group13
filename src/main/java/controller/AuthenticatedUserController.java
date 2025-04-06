package controller;

import external.AuthenticationService;
import external.EmailService;
import model.Guest;
import model.SharedContext;
import view.View;
/**
 * Controller responsible for handling authenticated user actions within the application.
 * Extends the base {@link Controller} class and provides specific functionality
 * such as logging out.
 */
public class AuthenticatedUserController extends Controller {
    /**
     * Constructs an {@code AuthenticatedUserController} with the necessary dependencies.
     *
     * @param sharedContext the shared application context holding session and user info
     * @param view the view interface for displaying information to the user
     * @param auth the authentication service used for verifying user credentials
     * @param email the email service used for sending notifications (not used here)
     */
    public AuthenticatedUserController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }
    /**
     * Logs out the currently authenticated user.
     * <p>
     * If the current user is a guest, an error message is displayed and the logout is aborted.
     * Otherwise, the current user is replaced with a new {@link Guest} instance and
     * a success message is displayed.
     */
    public void logout() {
        if (sharedContext.currentUser instanceof Guest) {
            view.displayError("Guest users cannot logout!");
            return;
        }
        sharedContext.currentUser = new Guest();
        view.displaySuccess("Logged out!");
    }
}
