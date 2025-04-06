package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Controller for handling inquirer interactions such as browsing the FAQ, submitting inquiries,
 * and subscribing/unsubscribing to FAQ topic updates.
 */
public class InquirerController extends Controller {

    /**
     * Constructs a new InquirerController.
     *
     * @param sharedContext the shared application context
     * @param view the user interface view
     * @param auth the authentication service
     * @param email the email service
     */
    public InquirerController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Allows the user to browse the FAQ, navigate sections, and manage email subscriptions for updates on topics.
     */
    public void consultFAQ() {
        FAQSection currentSection = null;
        String userEmail;
        if (sharedContext.currentUser instanceof AuthenticatedUser) {
            userEmail = ((AuthenticatedUser) sharedContext.currentUser).getEmail();
        } else {
            userEmail = null;
        }

        int optionNo = 0;
        while (currentSection != null || optionNo != -1) {
            if (currentSection == null) {
                view.displayFAQ(sharedContext.getFAQ());
                view.displayInfo("[-1] Return to main menu");
            } else {
                view.displayFAQSection(currentSection);
                view.displayInfo("[-1] Return to " + (currentSection.getParent() == null ? "FAQ" : currentSection.getParent().getTopic()));

                if (userEmail == null) {
                    view.displayInfo("[-2] Request updates for this topic");
                    view.displayInfo("[-3] Stop receiving updates for this topic");
                } else {
                    if (sharedContext.usersSubscribedToFAQTopic(currentSection.getTopic()).contains(userEmail)) {
                        view.displayInfo("[-2] Stop receiving updates for this topic");
                    } else {
                        view.displayInfo("[-2] Request updates for this topic");
                    }
                }
            }

            String input = view.getInput("Please choose an option: ");

            try {
                optionNo = Integer.parseInt(input);

                if (optionNo != -1 && optionNo != -2 && optionNo != -3) {
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

                if (currentSection != null) {
                    String topic = currentSection.getTopic();

                    if (userEmail == null && optionNo == -2) {
                        requestFAQUpdates(null, topic);
                    } else if (userEmail == null && optionNo == -3) {
                        stopFAQUpdates(null, topic);
                    } else if (optionNo == -2) {
                        if (sharedContext.usersSubscribedToFAQTopic(topic).contains(userEmail)) {
                            stopFAQUpdates(userEmail, topic);
                        } else {
                            requestFAQUpdates(userEmail, topic);
                        }
                    } else if (optionNo == -1) {
                        currentSection = currentSection.getParent();
                        optionNo = 0;
                    }
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid option: " + input);
            }
        }
    }

    /**
     * Registers a user's email to receive updates for a specific FAQ topic.
     *
     * @param userEmail the email to register (can be prompted if null)
     * @param topic the FAQ topic
     */
    private void requestFAQUpdates(String userEmail, String topic) {
        if (userEmail == null) {
            userEmail = view.getInput("Please enter your email address: ");
        }
        boolean success = sharedContext.registerForFAQUpdates(userEmail, topic);
        if (success) {
            view.displaySuccess("Successfully registered " + userEmail + " for updates on '" + topic + "'");
        } else {
            view.displayError("Failed to register " + userEmail + " for updates on '" + topic + "'. Perhaps this email was already registered?");
        }
    }

    /**
     * Unregisters a user's email from receiving updates for a specific FAQ topic.
     *
     * @param userEmail the email to unregister (can be prompted if null)
     * @param topic the FAQ topic
     */
    private void stopFAQUpdates(String userEmail, String topic) {
        if (userEmail == null) {
            userEmail = view.getInput("Please enter your email address: ");
        }
        boolean success = sharedContext.unregisterForFAQUpdates(userEmail, topic);
        if (success) {
            view.displaySuccess("Successfully unregistered " + userEmail + " for updates on '" + topic + "'");
        } else {
            view.displayError("Failed to unregister " + userEmail + " for updates on '" + topic + "'. Perhaps this email was not registered?");
        }
    }

    /**
     * Prompts and retrieves the email address of the inquirer, validating its format.
     *
     * @return the valid email address or null if invalid
     */
    public String getinquirerEmail(){
        String inquirerEmail;
        if (sharedContext.currentUser instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) sharedContext.currentUser;
            inquirerEmail = user.getEmail();
        } else {
            inquirerEmail = view.getInput("Enter your email address: ");
            // From https://owasp.org/www-community/OWASP_Validation_Regex_Repository
            if (!inquirerEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                view.displayError("Invalid email address! Please try again");
                return(null);
            }
        }
        return(inquirerEmail);
    }

    /**
     * Prompts and retrieves the subject of the inquiry.
     *
     * @return the subject string or null if blank
     */
    public String getSubject(){
        String subject = view.getInput("Describe the topic of your inquiry in a few words: ");
        if (subject.strip().isBlank()) {
            view.displayError("Inquiry subject cannot be blank! Please try again");
            return(null);
        }
        return(subject);
    }

    /**
     * Prompts and retrieves the course code related to the inquiry (optional).
     *
     * @return the course code string
     */
    public String getCourseCode(){
        String courseCode = view.getInput("Please enter the course code relating to your inquiry (optional): ");
        return(courseCode);
    }

    /**
     * Prompts and retrieves the content of the inquiry.
     *
     * @return the content string or null if blank
     */
    public String getContent(){
        String text = view.getInput("Write your inquiry:" + System.lineSeparator());
        if (text.strip().isBlank()) {
            view.displayError("Inquiry content cannot be blank! Please try again");
            return(null);
        }
        return (text);
    }

    /**
     * Sends the inquiry to the appropriate recipient via email based on the presence and validity of a course code.
     *
     * @param inquirerEmail the inquirer's email
     * @param subject the inquiry subject
     * @param text the body of the inquiry
     * @param courseCodeEntered whether a course code was entered
     * @param courseCode the course code (optional)
     */
    public void sendInquiry(String inquirerEmail, String subject, String text, boolean courseCodeEntered, String courseCode) {
        Inquiry inquiry = new Inquiry(inquirerEmail, subject, text, courseCodeEntered ? courseCode : null);
        sharedContext.inquiries.add(inquiry);

        String msgBody = "Subject: " + subject + System.lineSeparator() +
                (courseCodeEntered ? "Course Code: " + courseCode + System.lineSeparator() : "") +
                "Please log into the Self Service Portal to review and respond to inquiry.";
        if (!courseCodeEntered) {
            email.sendEmail(
                    inquirerEmail,
                    SharedContext.ADMIN_STAFF_EMAIL,
                    "New inquiry from " + inquirerEmail,
                    msgBody
            );
            view.displaySuccess("Your inquiry has been recorded. Someone will be in touch via email soon!");

        } else {
            //lookup courseCode
            CourseManager courseManager = sharedContext.getCourseManager();
            Course course = courseManager.getCourseByCode(courseCode);
            System.out.println(courseCode);
            courseManager.viewCourses();
            if (course != null) {
                SharedContext.COURSE_ORGANISER_EMAIL = course.getCourseOrganiserEmail();
                email.sendEmail(
                        //consult member of teaching staff responsible for that course
                        inquirerEmail,
                        SharedContext.COURSE_ORGANISER_EMAIL,
                        "New inquiry from " + inquirerEmail,
                        msgBody
                );
                view.displaySuccess("Your inquiry has been recorded. Someone will be in touch via email soon!");
            } else {
                view.displayError("Not a valid course code");

            }

        }
    }

    /**
     * Coordinates the process of collecting inquiry details from the user and sending the inquiry.
     */
    public void contactStaff() {

        String inquirerEmail = getinquirerEmail();
        if (inquirerEmail==null){return;}

        String subject = getSubject();
        if (subject==null){return;}

        String courseCode = getCourseCode();

        String text = getContent();
        if (text==null){return;}

        boolean courseCodeEntered;
        courseCodeEntered = !courseCode.strip().isBlank();
        sendInquiry(inquirerEmail, subject, text, courseCodeEntered, courseCode);

    }
}
