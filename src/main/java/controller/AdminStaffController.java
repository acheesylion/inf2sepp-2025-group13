package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import org.tinylog.Logger;
import view.View;



import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AdminStaffController extends StaffController {
    public AdminStaffController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }


    public void manageFAQ() {
        FAQSection currentSection = null;

        while (true) {
            if (currentSection == null) {
                view.displayFAQ(sharedContext.getFAQ());
                view.displayInfo("[-1] Return to main menu");
            } else {
                view.displayFAQSection(currentSection);
                view.displayInfo("[-1] Return to " + (currentSection.getParent() == null ? "FAQ" : currentSection.getParent().getTopic()));
            }
            view.displayInfo("[-2] Add FAQ item");
            String input = view.getInput("Please choose an option: ");
            try {
                int optionNo = Integer.parseInt(input);

                if (optionNo == -2) {
                    addFAQItem(currentSection);
                } else if (optionNo == -1) {
                    if (currentSection == null) {
                        break;
                    } else {
                        currentSection = currentSection.getParent();
                    }
                } else {
                    try {
                        if (currentSection == null) {
                            currentSection = sharedContext.getFAQ().getSections().get(optionNo);
                        } else {
                            currentSection = currentSection.getSubsections().get(optionNo);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        view.displayError("Invalid option: " + optionNo);
                    }
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid option: " + input);
            }
        }
    }

    private void addFAQItem(FAQSection currentSection) {
        // When adding an item at root of FAQ, creating a section is mandatory
        boolean createSection = (currentSection == null);
        if (!createSection) {
            createSection = view.getYesNoInput("Would you like to create a new topic for the FAQ item?");
        }

        if (createSection) {
            String newTopic = view.getInput("Enter new topic title: ");
            FAQSection newSection = new FAQSection(newTopic);
            if (currentSection == null) {
                if (sharedContext.getFAQ().getSections().stream().anyMatch(section -> section.getTopic().equals(newTopic))) {
                    view.displayWarning("Topic '" + newTopic + "' already exists!");
                    newSection = sharedContext.getFAQ().getSections().stream().filter(section -> section.getTopic().equals(newTopic)).findFirst().orElseThrow();
                } else {
                    sharedContext.getFAQ().addSection(newSection);
                    view.displayInfo("Created topic '" + newTopic + "'");
                }
            } else {
                if (currentSection.getSubsections().stream().anyMatch(section -> section.getTopic().equals(newTopic))) {
                    view.displayWarning("Topic '" + newTopic + "' already exists under '" + currentSection.getTopic() + "'!");
                    newSection = currentSection.getSubsections().stream().filter(section -> section.getTopic().equals(newTopic)).findFirst().orElseThrow();
                } else {
                    currentSection.addSubsection(newSection);
                    view.displayInfo("Created topic '" + newTopic + "' under '" + currentSection.getTopic() + "'");
                }
            }
            currentSection = newSection;
        }

        String question = view.getInput("Enter the question for new FAQ item: ");
        String answer = view.getInput("Enter the answer for new FAQ item: ");
        currentSection.getItems().add(new FAQItem(question, answer));

        String emailSubject = "FAQ topic '" + currentSection.getTopic() + "' updated";
        StringBuilder emailContentBuilder = new StringBuilder();
        emailContentBuilder.append("Updated Q&As:");
        for (FAQItem item : currentSection.getItems()) {
            emailContentBuilder.append("\n\n");
            emailContentBuilder.append("Q: ");
            emailContentBuilder.append(item.getQuestion());
            emailContentBuilder.append("\n");
            emailContentBuilder.append("A: ");
            emailContentBuilder.append(item.getAnswer());
        }
        String emailContent = emailContentBuilder.toString();

        email.sendEmail(
                ((AuthenticatedUser) sharedContext.currentUser).getEmail(),
                SharedContext.ADMIN_STAFF_EMAIL,
                emailSubject,
                emailContent
        );
        for (String subscriberEmail : sharedContext.usersSubscribedToFAQTopic(currentSection.getTopic())) {
            email.sendEmail(
                    SharedContext.ADMIN_STAFF_EMAIL,
                    subscriberEmail,
                    emailSubject,
                    emailContent
            );
        }
        view.displaySuccess("Created new FAQ item");
    }

    public void manageInquiries() {
        String[] inquiryTitles = getInquiryTitles(sharedContext.inquiries);

        while (true) {
            view.displayInfo("Pending inquiries");
            int selection = selectFromMenu(inquiryTitles, "Back to main menu");
            if (selection == -1) {
                return;
            }
            Inquiry selectedInquiry = sharedContext.inquiries.get(selection);

            while (true) {
                view.displayDivider();
                view.displayInquiry(selectedInquiry);
                view.displayDivider();
                String[] followUpOptions = { "Redirect inquiry", "Respond to inquiry" };
                int followUpSelection = selectFromMenu(followUpOptions, "Back to all inquiries");

                if (followUpSelection == -1) {
                    break;
                } else if (followUpOptions[followUpSelection].equals("Redirect inquiry")) {
                    redirectInquiry(selectedInquiry);
                } else if (followUpOptions[followUpSelection].equals("Respond to inquiry")) {
                    respondToInquiry(selectedInquiry);
                    inquiryTitles = getInquiryTitles(sharedContext.inquiries); // required to remove responded inquiry from titles
                    break;
                }
            }
        }
    }

    private void redirectInquiry(Inquiry inquiry) {
        inquiry.setAssignedTo(view.getInput("Enter assignee email: "));
        email.sendEmail(
                SharedContext.ADMIN_STAFF_EMAIL,
                inquiry.getAssignedTo(),
                "New inquiry from " + inquiry.getInquirerEmail(),
                "Subject: " + inquiry.getSubject() + "\nPlease log into the Self Service Portal to review and respond to the inquiry."
        );
        view.displaySuccess("Inquiry has been reassigned");
    }

    private enum manageCoursesOptions {
        ADD_COURSE,
        REMOVE_COURSE,
    }

    public void manageCourses() {
        boolean endLoop = false;
        while (!endLoop) {
            endLoop = handleManageCourses();
        }
    }

    private boolean handleManageCourses() {
        int optionNo = selectFromMenu(AdminStaffController.manageCoursesOptions.values(), "Back to main menu");
        if (optionNo == -1) {
            return true;
        }
        AdminStaffController.manageCoursesOptions option = AdminStaffController.manageCoursesOptions.values()[optionNo];
        switch (option) {
            case ADD_COURSE -> addCourse();
            case REMOVE_COURSE -> deleteCourse();
        }
        return false;
    }

    private int readInteger(View view, String prompt) {
        while (true) {
            String input = view.getInput(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                view.displayInfo("Invalid integer. Please try again.");
            }
        }
    }
    private boolean readBoolean(View view, String prompt) {
        while (true) {
            String input = view.getInput(prompt);
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(input);
            } else {
                view.displayInfo("Invalid input. Please enter true or false.");
            }
        }
    }

    private static String getValidatedInput(String fieldName, View view) {
        while (true) {
            String input = view.getInput(String.format("Enter %s: ", fieldName));
            // Perform validation based on the field name.
            switch (fieldName) {
                // For these fields, input must not be empty.
                case "courseCode":
                case "name":
                case "description":
                case "courseOrganiserName":
                case "courseOrganiserEmail":
                case "courseSecretaryName":
                case "courseSecretaryEmail":
                    if (input == null || input.trim().isEmpty()) {
                        view.displayInfo(fieldName + " cannot be empty.");
                        continue; // Prompt again.
                    }
                    break;

                // requiresComputers should be either "true" or "false".
                case "requiresComputers":
                    if (!"true".equalsIgnoreCase(input) && !"false".equalsIgnoreCase(input)) {
                        view.displayInfo("Invalid input for requiresComputers. Please enter true or false.");
                        continue;
                    }
                    break;

                // For these numeric fields, input must be a non-negative integer.
                case "requiredTutorials":
                case "requiredLabs":
                    try {
                        int num = Integer.parseInt(input);
                        if (num < 0) {
                            view.displayInfo(fieldName + " cannot be negative.");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        view.displayInfo("Invalid number for " + fieldName + ". Please enter a valid integer.");
                        continue;
                    }
                    break;

                default:
                    // If there are additional fields without specific validation, you could add them here.
                    break;
            }
            // If validation passes, return the input.
            return input;
        }
    }

    private void deleteCourse() {
        CourseManager courseManager = sharedContext.getCourseManager();
        view.displayInfo("=== Remove Course ===");
        String input = getValidatedInput("courseCode", view);
        if (!courseManager.checkCourseCode(input)){
            //System.currentTimeMillis(),email,"addCourse",courseInfo,"FAILURE"+" (Error: Provided courseCode is invalid)")
            Logger.error("{}, {}, deleteCourse, {} FAILURE (Error: Provided courseCode is invalid)",
                    System.currentTimeMillis(), sharedContext.getCurrentUserEmail(), input );
            view.displayError("Provided courseCode is invalid");
            return ;}
        Course courseDeleted = courseManager.getCourseByCode(input);
        String[] emaillist = courseManager.removeCourse(input, sharedContext);
        for (String emailAddress : emaillist) {
            email.sendEmail(sharedContext.getCurrentUserEmail(), emailAddress,
                    "Course deleted - " + courseDeleted.getCourseCode(),
                    "this course has been provided with the following details: " + courseDeleted.toString());
        }
    }

    private void addCourse(){
        view.displayInfo("=== Add Course ===");
        CourseInfo newCourseInfo = new CourseInfo();
        String[] courseInfoNames = {
            "courseCode",
            "name",
            "description",
            "requiresComputers",
            "courseOrganiserName",
            "courseOrganiserEmail",
            "courseSecretaryName",
            "courseSecretaryEmail",
            "requiredTutorials",
            "requiredLabs"
        };

        // For each field, get validated input and store it in CourseInfo.
        for (String fieldName : courseInfoNames) {
            String input = getValidatedInput(fieldName, view);
            newCourseInfo.setField(fieldName, input);
        }

        String currentEmail = sharedContext.getCurrentUserEmail();

        CourseManager courseManager = sharedContext.getCourseManager();

        courseManager.addCourse(currentEmail, newCourseInfo);

        //sendEmail(email, courseOrganiserEmail, "Course Created - " + courseCode,
        //"A course has been provided with the following details: " +
        //courseInfo)
        //
        email.sendEmail(currentEmail, newCourseInfo.getCourseOrganiserName(),
                "Course Created - " + newCourseInfo.getCourseCode(),
                "A course has been provided with the following details: " + newCourseInfo.getCourseInfo());

    }
}
