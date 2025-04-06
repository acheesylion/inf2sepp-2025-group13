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
import java.util.ArrayList;
import java.util.List;

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
            if (currentSection != null && !currentSection.getItems().isEmpty()) {
                view.displayInfo("[-3] Remove FAQ item");
            }
            String input = view.getInput("Please choose an option: ");
            try {
                int optionNo = Integer.parseInt(input);

                if (optionNo == -2) {
                    addFAQItem(currentSection);
                } else if (optionNo == -3 && currentSection != null && !currentSection.getItems().isEmpty()) {
                    removeFAQItem(currentSection);
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

    private void removeFAQItem(FAQSection currentSection) {
        if (currentSection.getItems().isEmpty()) {
            Logger.error("{},{},removeFAQItem,{} FAILURE: No FAQ items to remove in this section!"
                    ,System.currentTimeMillis(),"staff",currentSection.getTopic());
            view.displayWarning("No FAQ items to remove in this section!");
            return;
        }

        view.displayInfo("Select an FAQ item to remove:");
        for (int i = 0; i < currentSection.getItems().size(); i++) {
            FAQItem item = currentSection.getItems().get(i);
            view.displayInfo("[" + i + "] " + item.getQuestion());
        }
        view.displayInfo("[-1] Cancel");

        String input = view.getInput("Please choose an option: ");
        try {
            int optionNo = Integer.parseInt(input);

            if (optionNo == -1) {
                Logger.info("{},{},removeFAQItem,{} SUCCESS: Removal cancelled"
                        ,System.currentTimeMillis(),"staff",currentSection.getTopic());
                view.displayInfo("Removal cancelled");
                return;
            }

            if (optionNo >= 0 && optionNo < currentSection.getItems().size()) {
                FAQItem removedItem = currentSection.getItems().remove(optionNo);

                // 检查是否删除了主题中的最后一个问题
                if (currentSection.getItems().isEmpty()) {
                    // 获取父主题和当前主题的子主题
                    FAQSection parent = currentSection.getParent();
                    List<FAQSection> subsections = new ArrayList<>(currentSection.getSubsections());

                    // 将子主题上移一级
                    for (FAQSection subsection : subsections) {
                        if (parent == null) {
                            // 如果是根主题，将子主题移到FAQ根目录
                            sharedContext.getFAQ().addSection(subsection);
                            currentSection.getSubsections().remove(subsection);
                        } else {
                            // 如果不是根主题，将子主题移到父主题下
                            parent.addSubsection(subsection);
                            currentSection.getSubsections().remove(subsection);
                        }
                    }

                    // 从父主题或FAQ根目录中删除当前主题
                    if (parent == null) {
                        sharedContext.getFAQ().getSections().remove(currentSection);
                    } else {
                        parent.getSubsections().remove(currentSection);
                    }

                    view.displayInfo("Topic '" + currentSection.getTopic() + "' has been removed as it no longer contains any FAQ items.");
                    currentSection = parent; // 返回到父主题
                }

                // 保存被删除项目的主题名称，以便在主题被删除后仍能使用
                String topicName = currentSection != null ? currentSection.getTopic() : "FAQ";

                String emailSubject = "FAQ topic '" + topicName + "' updated - Item removed";
                StringBuilder emailContentBuilder = new StringBuilder();
                emailContentBuilder.append("The following Q&A has been removed from topic '" + topicName + "':");
                emailContentBuilder.append("\n\n");
                emailContentBuilder.append("Q: ");
                emailContentBuilder.append(removedItem.getQuestion());
                emailContentBuilder.append("\n");
                emailContentBuilder.append("A: ");
                emailContentBuilder.append(removedItem.getAnswer());
                if (removedItem.hasCourseTag()) {
                    emailContentBuilder.append("\nCourse: ");
                    emailContentBuilder.append(removedItem.getCourseTag());
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
                Logger.info("{},{},removeFAQItem,{} SUCCESS: Removed FAQ item '{}'"
                        ,System.currentTimeMillis(),"staff",currentSection.getTopic(),removedItem.getQuestion());

                view.displaySuccess("FAQ item removed successfully");
            } else {
                Logger.error("{},{},removeFAQItem,{} FAILURE: Invalid option: {}"
                        ,System.currentTimeMillis(),"staff",currentSection.getTopic(),optionNo);
                view.displayError("Invalid option: " + optionNo);
            }
        } catch (NumberFormatException e) {
            Logger.error("{},{},removeFAQItem,{} FAILURE: Invalid option: {}"
                    ,System.currentTimeMillis(),"staff",currentSection.getTopic(),input);
            view.displayError("Invalid option: " + input);
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

        // 添加可选的课程标签
        String courseTag = null;
        if (view.getYesNoInput("Would you like to add a course tag to this FAQ item?")) {
            courseTag = view.getInput("Enter course code: ");
            if (courseTag.trim().isEmpty()) {
                courseTag = null;
                view.displayInfo("No course code provided. FAQ item will not have a course tag.");
            } else {
                view.displayInfo("FAQ item will be tagged with course code: " + courseTag);
            }
        }

        currentSection.getItems().add(new FAQItem(question, answer, courseTag));

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
            if (item.hasCourseTag()) {
                emailContentBuilder.append("\nCourse: ");
                emailContentBuilder.append(item.getCourseTag());
            }
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
        Logger.info("{},{},addFAQItem,{} SUCCESS: Created new FAQ item '{}'"
                ,System.currentTimeMillis(),"staff",currentSection.getTopic(),question);
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

    private void removeCourse() {
        view.displayInfo("=== Remove Course ===");
        String courseCode = view.getInput("Enter course code: ");
        String currentEmail = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();

        String[] courseMembersEmail = courseManager.removeCourse(courseCode);

        for (String allMembersEmail : courseMembersEmail) {
            email.sendEmail(currentEmail, allMembersEmail,
                    "Course Removed - " + courseCode,
                    "The following Course has been removed."
            );
        }
    }

     private String deletecourseinput() {
          CourseManager courseManager = sharedContext.getCourseManager();
         String courseCode = view.getInput("Enter course code: ");
          if (!courseManager.checkCourseCode(courseCode)){
              //System.currentTimeMillis(),email,"addCourse",courseInfo,"FAILURE"+" (Error: Provided courseCode is invalid)")
              Logger.error("{}, {}, deleteCourse, {} FAILURE (Error: Provided courseCode is invalid)",
                      System.currentTimeMillis(), sharedContext.getCurrentUserEmail(), courseCode );
              view.displayError("Provided courseCode is invalid");
              return null ;}
          return courseCode;
      }

      public void deletecourselogic(String input) {
          CourseManager courseManager = sharedContext.getCourseManager();
          Course courseDeleted = courseManager.getCourseByCode(input);
          String[] emaillist = courseManager.removeCourse(input);
          for (String emailAddress : emaillist) {
              email.sendEmail(sharedContext.getCurrentUserEmail(), emailAddress,
                      "Course deleted - " + courseDeleted.getCourseCode(),
                      "this course has been provided with the following details: " + courseDeleted.toString());
          }
      }

    private void deleteCourse() {
        CourseManager courseManager = sharedContext.getCourseManager();
        view.displayInfo("=== Remove Course ===");
        String input = deletecourseinput();
        deletecourselogic(input);
    }


    private void fillCourseInfo(CourseInfo courseInfo) {
        String[] courseInfoNames = {
                "courseCode",
                "name",
                "description",
                "courseOrganiserName",
                "courseOrganiserEmail",
                "courseSecretaryName",
                "courseSecretaryEmail",
                "requiredTutorials",
                "requiredLabs"
        };

        // For each field, get validated input and store it in CourseInfo.
        for (String fieldName : courseInfoNames) {
            String input = view.getInput(String.format("Enter %s: ", fieldName));
            courseInfo.setField(fieldName, input);
        }

        Boolean requiresComputers = view.getYesNoInput("Enter requiresComputers: ");
        courseInfo.setRequiresComputers(requiresComputers);
    }

    private void addCourse(){
        view.displayInfo("=== Add Course ===");

        CourseInfo newCourseInfo = new CourseInfo();

        fillCourseInfo(newCourseInfo);

        String currentEmail = sharedContext.getCurrentUserEmail();

        CourseManager courseManager = sharedContext.getCourseManager();

        courseManager.addCourse(currentEmail, newCourseInfo);

        email.sendEmail(currentEmail, newCourseInfo.getCourseOrganiserName(),
                "Course Created - " + newCourseInfo.getCourseCode(),
                "A course has been provided with the following details: " + newCourseInfo.getCourseInfo());

    }

}
