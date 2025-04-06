package view;

import model.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

/**
 * The TextUserInterface class implements the View interface, providing text-based user interaction.
 * This class handles user input and output through the console, displaying messages, taking user input,
 * and formatting text using different colors for success, warning, and error messages.
 */
public class TextUserInterface implements View {
    private final Scanner scanner = new Scanner(System.in);
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Prompts the user for input and returns the entered text.
     *
     * @param prompt The message displayed to the user to prompt for input.
     * @return The input provided by the user.
     */
    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Prompts the user with a yes/no question and returns the user's response.
     *
     * @param prompt The yes/no question displayed to the user.
     * @return true if the user selects "yes", false if the user selects "no".
     */
    @Override
    public boolean getYesNoInput(String prompt) {
        System.out.println(prompt + " [Y/n]");
        String line = scanner.nextLine();
        if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
            return true;
        } else if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
            return false;
        }
        return Boolean.parseBoolean(line);
    }

    /**
     * Displays an informational message to the user.
     *
     * @param text The information to be displayed.
     */
    @Override
    public void displayInfo(String text) {
        System.out.println(text);
    }

    /**
     * Displays a success message to the user in green color.
     *
     * @param text The success message to be displayed.
     */
    @Override
    public void displaySuccess(String text) {
        System.out.println(ANSI_GREEN + text + ANSI_RESET);
    }

    /**
     * Displays a warning message to the user in yellow color.
     *
     * @param text The warning message to be displayed.
     */
    @Override
    public void displayWarning(String text) {
        System.out.println(ANSI_YELLOW + text + ANSI_RESET);
    }

    /**
     * Displays an error message to the user in red color.
     *
     * @param text The error message to be displayed.
     */
    @Override
    public void displayError(String text) {
        System.out.println(ANSI_RED + text + ANSI_RESET);
    }

    /**
     * Displays an exception's stack trace to the user as an error message.
     *
     * @param e The exception to be displayed.
     */
    @Override
    public void displayException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        displayError(sw.toString());
    }

    /**
     * Displays a divider in the console, used for separating different sections of output.
     */
    @Override
    public void displayDivider() {
        System.out.println("-------------------------");
    }

    /**
     * Displays the FAQ to the user, including a list of FAQ sections.
     *
     * @param faq The FAQ object containing sections to be displayed.
     */
    @Override
    public void displayFAQ(FAQ faq) {
        System.out.println("Frequently Asked Questions");
        displayDivider();
        int i = 0;
        for (FAQSection section : faq.getSections()) {
            System.out.print("[");
            System.out.print(i++);
            System.out.print("] ");
            System.out.println(section.getTopic());
        }
    }

    /**
     * Displays a specific FAQ section to the user, including its items and any subsections.
     *
     * @param section The FAQSection object to be displayed.
     */
    @Override
    public void displayFAQSection(FAQSection section) {
        System.out.println(section.getTopic());
        displayDivider();
        for (FAQItem item : section.getItems()) {
            System.out.println(item.getQuestion());
            System.out.print("> ");
            System.out.println(item.getAnswer());
        }

        System.out.println("Subsections:");
        int i = 0;
        for (FAQSection subsection : section.getSubsections()) {
            System.out.print("[");
            System.out.print(i++);
            System.out.print("] ");
            System.out.println(subsection.getTopic());
        }
    }

    /**
     * Displays a specific inquiry to the user, including its details such as the inquirer, assigned person,
     * content, and course code.
     *
     * @param inquiry The Inquiry object to be displayed.
     */
    @Override
    public void displayInquiry(Inquiry inquiry) {
        System.out.println("Inquirer: " + inquiry.getInquirerEmail());
        System.out.println("Created at: " + inquiry.getCreatedAt());
        System.out.println("Assigned to: " + (inquiry.getAssignedTo() == null ? "No one" : inquiry.getAssignedTo()));
        System.out.println("Query:");
        System.out.println(inquiry.getContent());
        System.out.println("Course Code: " + inquiry.getCourseCode());
    }

    /**
     * Displays a specific course to the user, formatted for display.
     *
     * @param course The Course object to be displayed.
     */
    @Override
    public void displayCourse(Course course) {
        System.out.println(course);
    }

    /**
     * Displays a student's timetable to the user.
     *
     * @param timetable The Timetable object to be displayed.
     */
    @Override
    public void displayTimetable(Timetable timetable) {
        System.out.println(timetable);
    }
}
