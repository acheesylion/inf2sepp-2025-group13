package controller;

import external.AuthenticationService;
import external.EmailService;
import model.SharedContext;
import view.View;
/**
 * Abstract base class for controllers in the application.
 * <p>
 * Provides shared dependencies and utility methods for interacting
 * with the user through the view, managing authentication, and handling context.
 */
public abstract class Controller {
    /** The shared application context holding session and user information. */
    protected final SharedContext sharedContext;
    /** The view interface used to display output and collect input. */
    protected final View view;
    /** The authentication service used for verifying credentials. */
    protected final AuthenticationService auth;
    /** The email service used for sending notifications. */
    protected final EmailService email;

    /**
     * Constructs a Controller with the specified shared context and services.
     *
     * @param sharedContext the shared application context
     * @param view the view interface
     * @param auth the authentication service
     * @param email the email service
     */
    protected Controller(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        this.sharedContext = sharedContext;
        this.view = view;
        this.auth = auth;
        this.email = email;
    }

    /**
     * Displays a menu to the user and prompts them to select an option.
     *
     * @param <T> the type of the menu options
     * @param options an array of menu options to choose from
     * @param exitOption a string describing the exit option (e.g., "Cancel", "Back")
     * @return the index of the selected option, or -1 if the exit option was chosen
     */
    protected <T> int selectFromMenu(T[] options, String exitOption) {
        while (true) {
            view.displayDivider();
            int i = 0;
            for (T option : options) {
                view.displayInfo("[" + i + "] " + option);
                i++;
            }
            view.displayInfo("[-1] " + exitOption);
            view.displayDivider();
            String input = view.getInput("Please choose an option: ");
            try {
                int optionNo = Integer.parseInt(input);
                if (optionNo == -1) {
                    return -1;
                }
                if (optionNo >= 0 && optionNo < options.length) {
                    return optionNo;
                }
                view.displayError("Invalid option " + optionNo);
            } catch (NumberFormatException e) {
                view.displayError("Invalid option " + input);
            }
        }
    }
}
