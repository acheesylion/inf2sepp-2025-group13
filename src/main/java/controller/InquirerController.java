package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.tinylog.Logger;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InquirerController extends Controller {
    public InquirerController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void consultFAQ() {
        FAQSection currentSection = null;
        String userEmail;
        String courseCodeFilter = null;
        
        // 询问用户是否要按课程代码筛选FAQ
        if (view.getYesNoInput("Would you like to filter FAQ by a course code?")) {
            courseCodeFilter = view.getInput("Enter course code: ");
            if (courseCodeFilter.trim().isEmpty()) {
                courseCodeFilter = null;
                view.displayInfo("No course code provided. Showing all FAQ items.");
            } else {
                view.displayInfo("Showing FAQ items for course code: " + courseCodeFilter);
            }
        }
        
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
                view.displayInfo("[-2] Search FAQ");
                if (courseCodeFilter != null) {
                    view.displayInfo("Currently filtering by course code: " + courseCodeFilter);
                    view.displayInfo("[-3] Clear course code filter");
                }
            } else {
                if (courseCodeFilter != null) {
                    view.displayFilteredFAQSection(currentSection, courseCodeFilter);
                    view.displayInfo("Currently filtering by course code: " + courseCodeFilter);
                } else {
                    view.displayFAQSection(currentSection);
                }
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
                
                if (courseCodeFilter != null) {
                    view.displayInfo("[-4] Clear course code filter");
                }
            }

            String input = view.getInput("Please choose an option: ");

            try {
                optionNo = Integer.parseInt(input);

                if (currentSection == null && optionNo == -2) {
                    searchFAQ(courseCodeFilter);
                    continue;
                }
                
                // 清除课程代码筛选
                if (currentSection == null && optionNo == -3 && courseCodeFilter != null) {
                    courseCodeFilter = null;
                    view.displayInfo("Course code filter cleared. Showing all FAQ items.");
                    continue;
                }
                
                // 清除课程代码筛选（在FAQ部分内）
                if (currentSection != null && optionNo == -4 && courseCodeFilter != null) {
                    courseCodeFilter = null;
                    view.displayInfo("Course code filter cleared. Showing all FAQ items.");
                    continue;
                }

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
                //         Logger.info("{},{},addFAQItem,{} SUCCESS: Created new FAQ item '{}'"
                //              ,System.currentTimeMillis(),"staff",currentSection.getTopic(),question);
                if (currentSection != null) {
                    Logger.error("{},consultFAQ,{} FAILED: Invalid option: {}",
                            System.currentTimeMillis(),currentSection.getTopic(),input);
                } else {
                    Logger.error("{},consultFAQ,{} FAILED: Invalid option: {}",
                            System.currentTimeMillis(),"FAQ",input);
                }

                view.displayError("Invalid option: " + input);
            }
        }
    }

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

    private void searchFAQ(String courseCodeFilter) {
        String keyword = view.getInput("Enter search keyword: ").toLowerCase();
        if (keyword.strip().isEmpty()) {
            Logger.warn("{},searchFAQ,{} FAILED: Search keyword cannot be empty!",
                    System.currentTimeMillis(),"FAQ");
            view.displayWarning("Search keyword cannot be empty!");
            return;
        }

        view.displayInfo("Search results for '" + keyword + "'" + 
                (courseCodeFilter != null ? " (filtered by course code: " + courseCodeFilter + ")" : "") + ":");
        view.displayDivider();

        boolean foundResults = false;
        List<FAQSection> allSections = new ArrayList<>();
        List<FAQItem> matchingItems = new ArrayList<>();

        // Add all root sections
        allSections.addAll(sharedContext.getFAQ().getSections());

        // Process all sections and their subsections
        for (int i = 0; i < allSections.size(); i++) {
            FAQSection section = allSections.get(i);
            // Add subsections to be processed
            allSections.addAll(section.getSubsections());

            // Check items in this section
            for (FAQItem item : section.getItems()) {
                // 如果设置了课程代码筛选，则只显示匹配的项目
                if (courseCodeFilter != null && 
                    (!item.hasCourseTag() || !item.getCourseTag().equals(courseCodeFilter))) {
                    continue; // 跳过不匹配课程代码的项目
                }
                
                if (item.getQuestion().toLowerCase().contains(keyword) ||
                    item.getAnswer().toLowerCase().contains(keyword)) {
                    foundResults = true;
                    matchingItems.add(item);
                    view.displayInfo("Topic: " + section.getTopic());
                    view.displayInfo("Q: " + item.getQuestion());
                    view.displayInfo("> " + item.getAnswer());
                    if (item.hasCourseTag() && courseCodeFilter == null) {
                        view.displayInfo("Course: " + item.getCourseTag());
                    }
                    view.displayDivider();
                }
            }
        }

        if (!foundResults) {
            Logger.info("{},searchFAQ,{} SUCCESS: No results found for '{}'",
                    System.currentTimeMillis(),"FAQ",keyword);
            view.displayInfo("No results found for '" + keyword + "'");
        } else {
            Logger.info("{},searchFAQ,{} SUCCESS: Found {} matching FAQ items",
                    System.currentTimeMillis(),"FAQ",matchingItems.size());
            view.displayInfo("Found " + matchingItems.size() + " matching FAQ items");
        }

        view.getInput("Press Enter to continue...");
    }

    public void contactStaff() {
        String inquirerEmail;
        if (sharedContext.currentUser instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) sharedContext.currentUser;
            inquirerEmail = user.getEmail();
        } else {
            inquirerEmail = view.getInput("Enter your email address: ");
            // From https://owasp.org/www-community/OWASP_Validation_Regex_Repository
            if (!inquirerEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                view.displayError("Invalid email address! Please try again");
                return;
            }
        }

        String subject = view.getInput("Describe the topic of your inquiry in a few words: ");
        if (subject.strip().isBlank()) {
            view.displayError("Inquiry subject cannot be blank! Please try again");
            return;
        }

        String text = view.getInput("Write your inquiry:" + System.lineSeparator());
        if (text.strip().isBlank()) {
            view.displayError("Inquiry content cannot be blank! Please try again");
            return;
        }

        Inquiry inquiry = new Inquiry(inquirerEmail, subject, text);
        sharedContext.inquiries.add(inquiry);

        email.sendEmail(
                SharedContext.ADMIN_STAFF_EMAIL,
                SharedContext.ADMIN_STAFF_EMAIL,
                "New inquiry from " + inquirerEmail,
                "Subject: " + subject + System.lineSeparator() + "Please log into the Self Service Portal to review and respond to the inquiry."
        );
        view.displaySuccess("Your inquiry has been recorded. Someone will be in touch via email soon!");
    }
}
