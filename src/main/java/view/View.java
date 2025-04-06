package view;

import model.*;

/**
 * The View interface defines the methods for interacting with the user.
 * It provides a contract for displaying information, receiving input, and handling various
 * user interface interactions related to the system, such as displaying course details, timetables,
 * inquiries, and handling user input for confirmation or text entry.
 */
public interface View {

    /**
     * Prompts the user for input with the specified message.
     *
     * @param prompt The message displayed to the user to prompt for input.
     * @return The input provided by the user.
     */
    String getInput(String prompt);

    /**
     * Prompts the user with a yes/no question and returns the user's choice.
     *
     * @param prompt The yes/no question displayed to the user.
     * @return true if the user selects "yes", false if the user selects "no".
     */
    boolean getYesNoInput(String prompt);

    /**
     * Displays an informational message to the user.
     *
     * @param text The information to be displayed.
     */
    void displayInfo(String text);

    /**
     * Displays a success message to the user.
     *
     * @param text The success message to be displayed.
     */
    void displaySuccess(String text);

    /**
     * Displays a warning message to the user.
     *
     * @param text The warning message to be displayed.
     */
    void displayWarning(String text);

    /**
     * Displays an error message to the user.
     *
     * @param text The error message to be displayed.
     */
    void displayError(String text);

    /**
     * Displays an exception message to the user, typically used for errors or unexpected conditions.
     *
     * @param e The exception to be displayed.
     */
    void displayException(Exception e);

    /**
     * Displays a visual divider to separate sections in the UI.
     */
    void displayDivider();

    /**
     * Displays the FAQ to the user.
     *
     * @param faq The FAQ object to be displayed.
     */
    void displayFAQ(FAQ faq);

    /**
     * Displays a specific FAQ section to the user.
     *
     * @param section The FAQSection to be displayed.
     */
    void displayFAQSection(FAQSection section);

    /**
     * Displays a specific inquiry to the user.
     *
     * @param inquiry The Inquiry to be displayed.
     */
    void displayInquiry(Inquiry inquiry);

    /**
     * Displays course information to the user.
     *
     * @param course The Course to be displayed.
     */
    void displayCourse(Course course);

    /**
     * Displays a student's timetable to the user.
     *
     * @param userTimetable The Timetable of the student to be displayed.
     */
    void displayTimetable(Timetable userTimetable);
}
