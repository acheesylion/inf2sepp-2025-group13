package model;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.*;
import view.View;
import org.tinylog.Logger;
import static java.lang.Math.abs;

/**
 * The CourseManager class is responsible for managing courses in the system.
 * It includes methods for adding, removing, viewing, and managing courses and their activities,
 * as well as handling course-related operations like adding courses to a student's timetable.
 */
public class CourseManager {

    private final List<Course> courses;
    public final List<Timetable> timetables;
    View view;

    /**
     * Constructs a CourseManager object with the specified view.
     *
     * @param view The view used to interact with the user.
     */
    public CourseManager(View view) {
        this.courses = new ArrayList<>();
        this.timetables = new ArrayList<>();
        this.view = view;
    }

    /**
     * Generates a unique activity ID for new activities.
     *
     * @return A unique activity ID.
     */
    public int generateActivityId() {
        return (UUID.randomUUID().hashCode());
    }

    /**
     * Validates the provided course information.
     *
     * @param courseInfo The course information to validate.
     * @return true if the course information is valid, false otherwise.
     */
    public Boolean validCourseInfo(CourseInfo courseInfo) {
        String courseCode = courseInfo.getCourseCode();
        String name = courseInfo.getName();
        String description = courseInfo.getDescription();
        String courseOrganiserName = courseInfo.getCourseOrganiserName();
        String courseOrganiserEmail = courseInfo.getCourseOrganiserEmail();
        String courseSecretaryName = courseInfo.getCourseSecretaryName();
        String courseSecretaryEmail = courseInfo.getCourseSecretaryEmail();
        String requiredTutorials = courseInfo.getRequiredTutorials();
        String requiredLabs = courseInfo.getRequiredLabs();

        if (courseCode.isEmpty() || name.isEmpty() || description.isEmpty()||
                courseOrganiserName.isEmpty() || courseOrganiserEmail.isEmpty() ||
                courseSecretaryName.isEmpty()|| courseSecretaryEmail.isEmpty()||
                requiredTutorials.isEmpty()|| requiredLabs.isEmpty()) {
            return false;
        }

        if (!checkEmailString(courseOrganiserEmail) || !checkEmailString(courseSecretaryEmail)) {
            return false;
        }

        try {
            Integer.parseInt(requiredTutorials);
            Integer.parseInt(requiredLabs);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Adds activities to a newly created course based on user input.
     *
     * @param newCourse The course to which activities will be added.
     * @param view      The view used to interact with the user.
     */
    private void addActivitiesToCourse(Course newCourse, View view) {
        int numActivities = readInteger(view, "Enter how many activities you wish to add: ");

        for (int i = 0; i < numActivities; i++) {
            LocalDate startDate = readDate(view, "Enter startDate (YYYY-MM-DD): ");
            LocalDate endDate;
            while (true) {
                endDate = readDate(view, "Enter endDate (YYYY-MM-DD): ");
                if (endDate.isAfter(startDate)) {
                    break; // Valid: end date is after start date
                } else {
                    view.displayInfo("End date must be after the start date. Please try again.");
                }
            }

            LocalTime startTime = readTime(view, "Enter startTime (HH:MM): ");
            LocalTime endTime;
            while (true) {
                endTime = readTime(view, "Enter endTime (HH:MM): ");;
                if (endTime.isAfter(startTime)) {
                    break; // Valid: end date is after start date
                } else {
                    view.displayInfo("End time must be after the start time. Please try again.");
                }
            }
            String location = view.getInput("Enter location: ");
            // Validate that location is not empty
            while (location.isEmpty()) {
                view.displayInfo("Location cannot be empty. Please enter a valid location.");
                location = view.getInput("Enter location: ");
            }

            // Validate and parse day
            DayOfWeek day = null;
            while (day == null) {
                String dayInput = view.getInput("Enter day (e.g., 'MONDAY'): ");
                try {
                    day = DayOfWeek.valueOf(dayInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    view.displayInfo("Invalid day. Please enter a valid day of the week (e.g., MONDAY).");
                }
            }

            // Validate type input
            String type = view.getInput("Enter type (lab, tutorial, lecture): ");
            while (!type.equalsIgnoreCase("lab") &&
                    !type.equalsIgnoreCase("tutorial") &&
                    !type.equalsIgnoreCase("lecture")) {
                view.displayInfo("Invalid type. Please enter lab, tutorial, or lecture.");
                type = view.getInput("Enter type (lab, tutorial, lecture): ");
            }

            // Depending on the type, ask for extra input (capacity for lab/tutorial or recorded for lecture)
            if (type.equalsIgnoreCase("lab") || type.equalsIgnoreCase("tutorial")) {
                int capacity = readInteger(view, "Enter capacity: ");
                newCourse.addActivity(generateActivityId(), startDate, startTime, endDate, endTime, location, day, capacity, type);
            } else if (type.equalsIgnoreCase("lecture")) {
                boolean isRecorded = readBoolean(view, "Enter if lectures are recorded (true/false): ");
                newCourse.addActivity(generateActivityId(),startDate, startTime, endDate, endTime, location, day, isRecorded, type);
            }

        }
    }

    /**
     * Removes a course by its course code.
     *
     * @param courseCode The code of the course to remove.
     * @return An array of the course's members' emails.
     */
    public String[] removeCourse(String courseCode){
        Course courseToRemove = getCourseByCode(courseCode);
        if (courseToRemove == null) {
            return new String[0];
        }
        List<String> members = courseToRemove.getMembers();
        members.add(courseToRemove.getCourseOrganiserEmail());
        courseToRemove.removeActivities();
        courses.removeIf(course -> courseToRemove.hasCode(courseCode));
        return (members.toArray(new String[0]));
    }

    /**
     * Adds a new course to the system based on the provided course information.
     *
     * @param email    The email of the user adding the course.
     * @param info     The course information to add.
     */
    public void addCourse(String email, CourseInfo info) {
        if(!validCourseInfo(info)) {
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: : Required course info not provided)",
                    System.currentTimeMillis(), email, info.getCourseInfo() );
            view.displayError("Required course info not provided");
            return;

        }

        if (!checkCourseCode(info.getCourseCode())){
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Provided courseCode is invalid)",
                    System.currentTimeMillis(), email, info.getCourseInfo() );
            view.displayError("Provided courseCode is invalid");
            return;
        }

        if (hasCourse(info.getCourseCode())){
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Course with that code already exists)",
                    System.currentTimeMillis(), email, info.getCourseInfo() );
            view.displayError("Course with that code already exists");

            return;
        }

        view.displayInfo("=== Add Course - Activities===");

        Course newCourse = new Course(
                info.getCourseCode(), info.getName(),
                info.getDescription(), info.getRequiresComputers(),
                info.getCourseOrganiserName(), info.getCourseOrganiserEmail(),
                info.getCourseSecretaryName(), info.getCourseSecretaryEmail(),
                Integer.parseInt(info.getRequiredTutorials()), Integer.parseInt(info.getRequiredLabs())
        );

        addActivitiesToCourse(newCourse, view);
        addCourseToCourseList(newCourse);

        Logger.info("{}, {}, addCourse, {} SUCCESS (New course added)", System.currentTimeMillis(), email, info.getCourseInfo() );
        view.displaySuccess("Course has been successfully created");

    }

    /**
     * Adds the specified course to the list of courses.
     *
     * @param course The course to add.
     */
    public void addCourseToCourseList(Course course) {
        courses.add(course);
    }

    /**
     * Views all courses in the system.
     */
    public void viewCourses() {
        if (courses.isEmpty()) {
            Logger.error("{}, viewCourses, FAILURE (Error: Course list is empty.)",
                    System.currentTimeMillis());
            view.displayError("No courses found");
        } else {
            for (Course course : courses){
                view.displayCourse(course);
            }
            Logger.info("{}, viewCourse, SUCCESS", System.currentTimeMillis());
            view.displaySuccess("Successfully viewed courses");
        }

    }

    /**
     * Views a specific course based on its course code.
     *
     * @param courseCode The code of the course to view.
     */
    public void viewCourse(String courseCode) {
        if (!hasCourse(courseCode)) {
            Logger.error("{}, {}, viewCourse, FAILURE (Error: Incorrect course code provided.)",
                    System.currentTimeMillis(), courseCode);
            view.displayError("Incorrect course code");
        } else {
            Course findCourse = getCourseByCode(courseCode);
            view.displayInfo("------------------------");
            view.displayCourse(findCourse);
            view.displayInfo("------------------------");
            Logger.info("{}, {}, viewCourse, SUCCESS", System.currentTimeMillis(), courseCode);
            view.displaySuccess("Successfully viewed course " + courseCode);
        }

    }

    /**
     * Checks if the provided course code matches a valid format.
     *
     * @param courseCode The course code to check.
     * @return true if the course code is valid, false otherwise.
     */
    public boolean checkCourseCode(String courseCode){
        return courseCode.matches("^[A-Z]{4}\\d{5}$");
    }

    /**
     * Checks if the provided email is in a valid format.
     *
     * @param email The email to check.
     * @return true if the email is valid, false otherwise.
     */
    public boolean checkEmailString(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Checks if a course with the provided course code exists in the system.
     *
     * @param courseCode The course code to check.
     * @return true if the course exists, false otherwise.
     */
    public boolean hasCourse(String courseCode) {
        for (Course course : courses) {
            if (course.hasCode(courseCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a timetable exists for a given student email.
     *
     * @param studentEmail The email of the student whose timetable is being checked.
     * @return true if the timetable exists, false otherwise.
     */
    private boolean timetableExists(String studentEmail) {
        return timetables.stream()
                .anyMatch(t -> t.hasStudentEmail(studentEmail));
    }

    /**
     * Retrieves the timetable for a given student email.
     *
     * @param studentEmail The email of the student whose timetable is being retrieved.
     * @return The Timetable object associated with the given student email.
     * @throws NoSuchElementException if no timetable exists for the student.
     */
    private Timetable getTimetable(String studentEmail) {
        return timetables.stream()
                .filter(t -> t.hasStudentEmail(studentEmail))
                .findFirst()
                .get();
    }

    /**
     * Prompts the user for an integer input and validates it.
     *
     * @param view   The view used to interact with the user.
     * @param prompt The message shown to the user to ask for the integer input.
     * @return The integer input provided by the user.
     */
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

    /**
     * Prompts the user for a date input and validates it.
     *
     * @param view   The view used to interact with the user.
     * @param prompt The message shown to the user to ask for the date input.
     * @return The LocalDate input provided by the user.
     */
    private LocalDate readDate(View view, String prompt) {
        while (true) {
            String input = view.getInput(prompt);
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                view.displayInfo("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    /**
     * Prompts the user for a time input and validates it.
     *
     * @param view   The view used to interact with the user.
     * @param prompt The message shown to the user to ask for the time input.
     * @return The LocalTime input provided by the user.
     */
    private LocalTime readTime(View view, String prompt) {
        while (true) {
            String input = view.getInput(prompt);
            try {
                return LocalTime.parse(input);
            } catch (DateTimeParseException e) {
                view.displayInfo("Invalid time format. Please use HH:MM.");
            }
        }
    }

    /**
     * Prompts the user for a boolean input and validates it.
     *
     * @param view   The view used to interact with the user.
     * @param prompt The message shown to the user to ask for the boolean input.
     * @return The boolean input provided by the user.
     */
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

    /**
     * Retrieves a course by its course code.
     *
     * @param courseCode The code of the course to retrieve.
     * @return The Course object associated with the given course code, or null if no course is found.
     */
    public Course getCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Removes a course from a student's timetable.
     * The method verifies that the course exists, the student has a timetable,
     * and that the course is in the student's timetable before removal.
     *
     * @param studentEmail The email of the student from whose timetable the course will be removed.
     * @param courseCode   The course code of the course to be removed.
     */
    public void removeCourseFromTimetable(String studentEmail, String courseCode) {
        if (!hasCourse(courseCode)) {
            Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: Incorrect course code provided.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Incorrect course code");
            return;
        }

        if (!timetableExists(studentEmail)) {
            Logger.error("{}, {}, removeCourseFromTimetable, {} FAILURE (Error: Student does not have Timetable.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Student does not have Timetable");
            return;
        }

        Timetable userTimetable = getTimetable(studentEmail);

        if (!userTimetable.hasSlotsForCourse(courseCode)) {
            Logger.error("{}, {}, removeCourseFromTimetable, {} FAILURE (Error: Course not in timetable.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Course not in timetable");
            return;
        }

        userTimetable.removeSlotsForCourse(courseCode);

        Logger.info("{}, {}, removeCourseFromTimetable, {} SUCCESS", System.currentTimeMillis(), studentEmail, courseCode);
        view.displaySuccess("The course was successfully removed from your timetable");

    }

    /**
     * Adds a course to a student's timetable.
     * The method checks if the course exists, verifies there are no conflicts in the timetable,
     * and handles adding the course's activities (lab, tutorial, lecture) to the timetable.
     * It also ensures that required tutorials and labs are selected before confirming the addition.
     *
     * @param studentEmail The email of the student to whom the course will be added.
     * @param courseCode   The course code of the course to be added.
     */
    public void addCourseToStudentTimetable(String studentEmail, String courseCode) {
        if (!hasCourse(courseCode)) {
            Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: Incorrect course code provided.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Incorrect course code");
            return;
        }

        Course courseToBeAdded = getCourseByCode(courseCode);

        Timetable userTimetable = null;

        if (timetableExists(studentEmail)) {
            userTimetable = getTimetable(studentEmail);
        } else {
            userTimetable = new Timetable(studentEmail);
            timetables.add(userTimetable);
        }

        if (userTimetable.hasSlotsForCourse(courseCode)) {
            Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: Already added course to timetable)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Already added course to timetable");
            return;
        }

        for (Activity activity : courseToBeAdded.getActivities()) {
            String[] conflictingCourseCodeAndActivityId = userTimetable.checkConflicts(
                    activity.getDay(),
                    activity.getStartTime(),
                    activity.getEndTime()
            );

            if (conflictingCourseCodeAndActivityId.length > 0) {
                String conflictCourseCode = conflictingCourseCodeAndActivityId[0];
                String conflictActivityId = conflictingCourseCodeAndActivityId[1];
                boolean unrecordedLecture1 = courseToBeAdded.isUnrecordedLecture(activity.getId());
                Course conflictingCourse = getCourseByCode(conflictCourseCode);
                boolean unrecordedLecture2 = conflictingCourse.isUnrecordedLecture(Integer.parseInt(conflictActivityId));

                if (unrecordedLecture1 || unrecordedLecture2) {
                    Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: at least one clash with an unrecorded lecture)",
                            System.currentTimeMillis(), studentEmail, courseCode);
                    view.displayError("You have at least one clash with an unrecorded lecture. The course cannot be added to your timetable.");
                    return;

                } else {
                    Logger.warn("{}, {}, addCourseToStudentTimetable, {} FAILURE (Warning: at least one clash with another activity)",
                            System.currentTimeMillis(), studentEmail, courseCode);
                    view.displayWarning("You have at least one clash with another activity");
                }
            }
        }

        for (Activity activity : courseToBeAdded.getActivities()) {
            if (activity instanceof Lab) {
                userTimetable.addTimeSlot(activity, courseCode, ActivityType.LAB);
            }
            if  (activity instanceof Tutorial) {
                userTimetable.addTimeSlot(activity, courseCode, ActivityType.TUTORIAL);
            }
            if (activity instanceof Lecture) {
                userTimetable.addTimeSlot(activity, courseCode, ActivityType.LECTURE);
            }
        }

        int requiredTutorials = courseToBeAdded.getRequiredTutorials();
        if (requiredTutorials > 0) {
            Logger.warn("{}, {}, addCourseToStudentTimetable, {} FAILURE (Warning: number of required tutorials {} not yet chosen)",
                    System.currentTimeMillis(), studentEmail, courseCode, requiredTutorials);
            view.displayWarning("You have to choose " + requiredTutorials + " tutorials for this course ");
        }


        int requiredLabs = courseToBeAdded.getRequiredLabs();
        if (requiredLabs > 0) {
            Logger.warn("{}, {}, addCourseToStudentTimetable, {} FAILURE (Warning: number of required labs {} not yet chosen)",
                    System.currentTimeMillis(), studentEmail, courseCode, requiredLabs);
            view.displayWarning("You have to choose " + requiredLabs + " labs for this course ");
        }

        courseToBeAdded.addMember(studentEmail); //This line adds student as a member so if the course is deleted the student will be emailed

        Logger.info("{}, {}, addCourseToStudentTimetable, {} SUCCESS", System.currentTimeMillis(), studentEmail, courseCode);
        view.displaySuccess("The course was successfully added to your timetable");

    }

    /**
     * Allows a student to choose an activity for a course from their timetable.
     * The method validates the provided activity ID, checks for conflicts with existing activities,
     * and ensures that the required tutorials and labs are selected. If all checks pass, the activity
     * is added to the student's timetable.
     *
     * @param studentEmail The email of the student who is selecting the activity.
     * @param courseCode   The course code of the course the activity belongs to.
     * @param activityIdString The ID of the activity the student wants to select (provided as a string).
     */
    public void chooseActivityForCourse(String studentEmail, String courseCode, String activityIdString) {
        int activityId;

        try {
            activityId =  Integer.parseInt(activityIdString);
        } catch (NumberFormatException e) {
            view.displayInfo("Invalid integer. Please try again.");
            Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: Incorrect activity id provided.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Incorrect activity id");
            return;
        }

        if (!timetableExists(studentEmail)) {
            Logger.error("{}, {}, chooseActivityForCourse, FAILURE (Error: Timetable does not exist)",
                    System.currentTimeMillis(), studentEmail);
            view.displayError("Timetable does not exist");
            return;
        }

        Timetable userTimetable = getTimetable(studentEmail);

        if (!hasCourse(courseCode)) {
            Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: Incorrect course code provided.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Incorrect course code");
            return;
        }

        Course courseToBeChosen = getCourseByCode(courseCode);

        if (!userTimetable.hasSlotsForCourse(courseCode)) {
            Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: Course does not exist in timetable.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Course does not exist in timetable");
            return;
        }

        if (!userTimetable.hasSlotsForActivityId(activityId)) {
            Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: Activity does not exist in timetable.)",
                    System.currentTimeMillis(), studentEmail, activityId);
            view.displayError("Activity does not exist in timetable");
            return;
        }

        String[] conflictingCourseCodeAndActivityId = userTimetable.checkConflicts(
                courseToBeChosen.getActivityDay(activityId),
                courseToBeChosen.getActivityStartTime(activityId),
                courseToBeChosen.getActivityEndTime(activityId)
        );

        if (conflictingCourseCodeAndActivityId.length > 0) {
            String conflictCourseCode = conflictingCourseCodeAndActivityId[0];
            String conflictActivityId = conflictingCourseCodeAndActivityId[1];
            Course conflictingCourse = getCourseByCode(conflictCourseCode);
            boolean unrecordedLecture = conflictingCourse.isUnrecordedLecture(Integer.parseInt(conflictActivityId));

            if (unrecordedLecture) {
                Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: at least one clash with an unrecorded lecture)",
                        System.currentTimeMillis(), studentEmail, courseCode);
                view.displayError("You have at least one clash with an unrecorded lecture. The activity cannot be added to your timetable.");
                return;

            } else {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: at least one clash with another activity)",
                        System.currentTimeMillis(), studentEmail, courseCode);
                view.displayWarning("You have at least one clash with another activity");
            }
        }

        userTimetable.chooseActivity(courseCode, activityId);

        if (userTimetable.isIdTutorial(activityId)) {
            int requiredTutorials = getCourseByCode(courseCode).getRequiredTutorials();
            int chosenTutorials = userTimetable.numChosenTutorialInTimeSlots(courseCode);
            int requiredVsChosenTutorial = requiredTutorials - chosenTutorials;
            if (requiredVsChosenTutorial > 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: number of required tutorials {} not yet chosen)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenTutorial);
                view.displayWarning("You have to choose " + requiredVsChosenTutorial + " more tutorial(s) for this course");
            }

            if (requiredVsChosenTutorial < 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: {} more chosen tutorials than required tutorials)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenTutorial);
                view.displayWarning("You have to choose " + abs(requiredVsChosenTutorial) + " less tutorial(s) for this course");
            }
        }

        if (userTimetable.isIdLab(activityId)) {
            int requiredLabs = getCourseByCode(courseCode).getRequiredLabs();
            int chosenLabs = userTimetable.numChosenLabInTimeSlots(courseCode);
            int requiredVsChosenLab = requiredLabs - chosenLabs;
            if (requiredVsChosenLab > 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: number of required labs {} not yet chosen)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenLab);
                view.displayWarning("You have to choose " + requiredVsChosenLab + " more lab(s) for this course");
            }

            if (requiredVsChosenLab < 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: {} more chosen labs than required labs)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenLab);
                view.displayWarning("You have to choose " + abs(requiredVsChosenLab) + " less lab(s) for this course");
            }
        }

        Logger.info("{}, {}, chooseActivityForCourse, {} SUCCESS", System.currentTimeMillis(), studentEmail, activityId);
        view.displaySuccess("The activity was successfully added to your timetable");
    }

    /**
     * Prints the timetable of a student.
     * If the student's timetable exists, it is displayed; otherwise, an error message is shown.
     *
     * @param email The email of the student whose timetable is being printed.
     * @param view  The view used to display the timetable to the user.
     */
    public void printTimetable(String email, View view) {
        if (timetableExists(email)) {
            Timetable userTimetable = getTimetable(email);
            view.displayTimetable(userTimetable);
            Logger.info("{}, {}, printTimetable, SUCCESS", System.currentTimeMillis(), email);
            view.displaySuccess("Successfully printed timetable");
        } else {
            Logger.error("{}, {}, printTimetable, {} FAILURE (Error: Timetable for {} does not exist)",
                    System.currentTimeMillis(), email, email);
            view.displayError("Timetable does not exist");
        }
    }

}
