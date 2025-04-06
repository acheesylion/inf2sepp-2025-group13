package controller;

import external.AuthenticationService;
import external.EmailService;
import model.AuthenticatedUser;
import model.SharedContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import view.View;

/**
 * Controller responsible for handling actions available to guest users,
 * such as logging into the system.
 */
public class GuestController extends Controller {

    /**
     * Constructs a {@code GuestController} with the necessary dependencies.
     *
     * @param sharedContext the shared application context for managing state
     * @param view the view interface used for input/output interactions
     * @param auth the authentication service for verifying login credentials
     * @param email the email service (not used in this class)
     */
    public GuestController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Prompts the guest user to log in using a username and password.
     * <p>
     * This method sends the login request to the {@link AuthenticationService},
     * processes the JSON response, and updates the shared context with an
     * {@link AuthenticatedUser} if login is successful.
     * <p>
     * If there is an error in the login process (invalid credentials or malformed response),
     * appropriate feedback is displayed using the view interface.
     */
    public void login() {
        String username = view.getInput("Enter your username: ");
        String password = view.getInput("Enter your password: ");
        String response = auth.login(username, password);

        JSONParser parser = new JSONParser();
        String email = null;
        String role = null;

        try {
            JSONObject result = (JSONObject) parser.parse(response);
            if (result.containsKey("error")) {
                String errorMessage = (String) result.get("error");
                view.displayError(errorMessage);
                return;
            }
            email = (String) result.get("email");
            role = (String) result.get("role");
        } catch (ParseException e) {
            view.displayException(e);
        }

        try {
            sharedContext.currentUser = new AuthenticatedUser(email, role);
        } catch (IllegalArgumentException e) {
            view.displayException(e);
        }

        view.displaySuccess("Logged in as " + username);
    }
}
