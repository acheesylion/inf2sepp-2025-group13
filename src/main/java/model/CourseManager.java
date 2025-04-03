package model;
import java.sql.SQLOutput;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import view.TextUserInterface;
import view.View;
import org.tinylog.Logger;


public class CourseManager {

    private final List<Course> courses;
    private final List<Timetable> timetables;
    View view;


    public CourseManager(View view) {
        this.courses = new ArrayList<>();
        this.timetables = new ArrayList<>();
        this.view = view;
    }

    private boolean nullCourseInfo(CourseInfo temp){
        String code = temp.getCourseCode();
        String name = temp.getName();
        String description = temp.getDescription();
        String COName = temp.getCourseOrganiserName();
        String COEmail = temp.getCourseOrganiserEmail();
        String CSName = temp.getCourseSecretaryName();
        String CSEmail = temp.getCourseSecretaryEmail();
        int requiredTutorials = temp.getRequiredTutorials();
        int requiredLabs = temp.getRequiredLabs();

        // Validate each field: if any are invalid, return false.
        return code != null && !code.isEmpty() &&
                name != null && !name.isEmpty() &&
                description != null && !description.isEmpty() &&
                COName != null && !COName.isEmpty() &&
                COEmail != null && !COEmail.isEmpty() &&
                CSName != null && !CSName.isEmpty() &&
                CSEmail != null && !CSEmail.isEmpty() &&
                requiredTutorials >= 0 && requiredLabs >= 0;
    }
    private void addActivitiesToCourse(Course new_course, View view) {
        int numActivities = readInteger(view, "Enter how many activities you wish to add: ");

        for (int i = 0; i < numActivities; i++) {
            LocalDate startDate = readDate(view, "Enter startDate (YYYY-MM-DD): ");
            LocalTime startTime = readTime(view, "Enter startTime (HH:MM): ");
            LocalDate endDate;
            while (true) {
                endDate = readDate(view, "Enter endDate (YYYY-MM-DD): ");
                if (endDate.isAfter(startDate)) {
                    break; // Valid: end date is after start date
                } else {
                    view.displayInfo("End date must be after the start date. Please try again.");
                }
            }

            LocalTime endTime = readTime(view, "Enter endTime (HH:MM): ");

            String location = view.getInput("Enter location: ");
            // Validate that location is not empty
            while (location == null || location.trim().isEmpty()) {
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
                new_course.addActivity(startDate, startTime, endDate, endTime, location, day, capacity, type);
            } else if (type.equalsIgnoreCase("lecture")) {
                boolean isRecorded = readBoolean(view, "Enter if lectures are recorded (true/false): ");
                new_course.addActivity(startDate, startTime, endDate, endTime, location, day, isRecorded, type);
            }

        }
    }
    public String[] removeCourse(String courseCode){
        List<String> members = getCourseByCode(courseCode).getMembers();
        members.add(getCourseByCode(courseCode).getCourseOrganiserEmail());
        courses.removeIf(course -> course.getCourseCode().equalsIgnoreCase(courseCode));
        return (members.toArray(new String[0]));
    }

    public void addCourse(String email, CourseInfo info) {
        if (!nullCourseInfo(info)) {
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Required course info not provided)",
                    System.currentTimeMillis(), email, info.getCourseInfo() );
            view.displayError("Required course info not provided");
            return;
        }


        if (!checkCourseCode(info.getCourseCode())){
            //System.currentTimeMillis(),email,"addCourse",courseInfo,"FAILURE"+" (Error: Provided courseCode is invalid)")
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Provided courseCode is invalid)",
                    System.currentTimeMillis(), email, info.getCourseInfo() );
            view.displayError("Provided courseCode is invalid");

            return;
        }

        if (hasCourse(info.getCourseCode())){
            //System.currentTimeMillis(),email,"addCourse",courseInfo,"FAILURE"+" (Error: Provided courseCode is invalid)")
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
                info.getRequiredTutorials(), info.getRequiredLabs()
        );

        addActivitiesToCourse(newCourse, view);
        courses.add(newCourse);

        Logger.info("{}, {}, addCourse, {} SUCCESS (New course added)", System.currentTimeMillis(), email, info.getCourseInfo() );
        view.displaySuccess("Course has been successfully created");

    }

    public void viewCourses() {
        if (courses.isEmpty()) {
            view.displayError("No courses found");
        } else {
            for (Course course : courses){
                view.displayInfo("-------------------------");
                view.displayCourse(course);
                view.displayInfo("-------------------------");
            }
        }

    }

    public void viewCourse(String courseCode) {
        if (!hasCourse(courseCode)) {
            view.displayError("Incorrect course code");
        } else {
            Course findCourse = getCourseByCode(courseCode);
            view.displayCourse(findCourse);
        }

    }

    public boolean checkCourseCode(String courseCode){return courseCode.matches("^[A-Z]{4}\\d{5}$");}

    private boolean hasCourse(String courseCode) {
        for (Course course : courses) {
            if (course.hasCode(courseCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean timetableExists(String studentEmail) {
        return timetables.stream()
                .anyMatch(t -> t.hasStudentEmail(studentEmail));
    }


    private Timetable getTimetable(String studentEmail) {
        if (timetableExists(studentEmail)) {
            // If a timetable exists, return the first one found.
            return timetables.stream()
                    .filter(t -> t.hasStudentEmail(studentEmail))
                    .findFirst()
                    .get();
        } else {
            // Otherwise, create a new timetable, add it, and return it.
            Timetable newTimetable = new Timetable(studentEmail);
            timetables.add(newTimetable);
            return newTimetable;
        }
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

    public Course getCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null; // Return null if no matching course is found.
    }


    public int checkChosenTutorials(String courseCode, Timetable timetable) {
        if (timetable == null || courseCode == null) {
            return 0;
        }
        if (!timetable.hasSlotsForCourse(courseCode)) {
            return 0;
        }
        int requiredTutorials = getCourseByCode(courseCode).getRequiredTutorials();
        int chosenTutorials = timetable.numTutorialInTimeSlots(courseCode);

        return (requiredTutorials - chosenTutorials);
    }

    public int checkChosenLabs(String courseCode, Timetable timetable) {
        if (timetable == null || courseCode == null) {
            return 0;
        }
        if (!timetable.hasSlotsForCourse(courseCode)) {
            return 0;
        }
        int requiredLabs = getCourseByCode(courseCode).getRequiredLabs();
        int chosenLabs = timetable.numLabInTimeSlots(courseCode);

        return (requiredLabs - chosenLabs);
    }



    public void addCourseToStudentTimetable(String studentEmail, String courseCode) {
        if (!hasCourse(courseCode)) {
            Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: Incorrect course code provided  )",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Incorrect course code");
            return;
        }


        Course courseToBeAdded = getCourseByCode(courseCode);
        Timetable userTimetable = getTimetable(studentEmail);

        if (userTimetable.hasSlotsForCourse(courseCode)) {
            Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: Already added course to timetable)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Already added course to timetable");
            return;
        }

        for (Activity activity : courseToBeAdded.getActivities()) {
            String[] conflictingCourseCodeAndActivityId = userTimetable.checkConflicts(
                    activity.getStartDate(), activity.getStartTime(),
                    activity.getEndDate(), activity.getEndTime()
            );

            // If there is a conflict
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

            Activity copyActivity = null;

            if (activity instanceof Lab) {
                copyActivity = new Lab((Lab) activity);
            } else if (activity instanceof Tutorial) {
                copyActivity = new Tutorial((Tutorial) activity);
            } else if (activity instanceof Lecture) {
                copyActivity = new Lecture((Lecture) activity);
            } else {
                Logger.error("{}, {}, addCourseToStudentTimetable, {} FAILURE (Error: Unsupported activity type)",
                        System.currentTimeMillis(), studentEmail, courseCode);
                view.displayError("Unsupported activity type");
                return;
            }
            userTimetable.addTimeSlot(copyActivity, courseCode);

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
                view.displayWarning("You have to choose " + requiredLabs + " tutorials for this course ");
            }
            courseToBeAdded.addMember(studentEmail); //This line adds student as a member so if the course is deleted the student will be emailed

            Logger.info("{}, {}, addCourseToStudentTimetable, {} SUCCESS", System.currentTimeMillis(), studentEmail, courseCode);
            view.displaySuccess("The course was successfully added to your timetable");

        }
    }



    public void chooseActivityForCourse(String studentEmail) {
        if (!timetableExists(studentEmail)) {
            Logger.error("{}, {}, chooseActivityForCourse, FAILURE (Error: Timetable does not exist)",
                    System.currentTimeMillis(), studentEmail);
            view.displayError("Timetable does not exist");
            return;
        }

        Timetable userTimetable = getTimetable(studentEmail);


        String courseCode = view.getInput("Enter courseCode: ");

        if (!userTimetable.hasSlotsForCourse(courseCode)) {
            Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: Course does not exist in timetable.)",
                    System.currentTimeMillis(), studentEmail, courseCode);
            view.displayError("Already added course to timetable");
            return;
        }

        int activityId = readInteger(view, "Enter activityId: ");

        TimeSlot selectedTimeSlot = userTimetable.getTimeSlotByActivityId(activityId);

        Activity activity = selectedTimeSlot.getActivity();

        String[] conflictingCourseCodeAndActivityId = userTimetable.checkConflicts(
                activity.getStartDate(), activity.getStartTime(),
                activity.getEndDate(), activity.getEndTime()
        );

        // If there is a conflict
        if (conflictingCourseCodeAndActivityId.length > 0) {
            String conflictCourseCode = conflictingCourseCodeAndActivityId[0];
            String conflictActivityId = conflictingCourseCodeAndActivityId[1];
            Course conflictingCourse = getCourseByCode(conflictCourseCode);
            boolean unrecordedLecture2 = conflictingCourse.isUnrecordedLecture(Integer.parseInt(conflictActivityId));

            if (unrecordedLecture2) {
                Logger.error("{}, {}, chooseActivityForCourse, {} FAILURE (Error: at least one clash with an unrecorded lecture)",
                        System.currentTimeMillis(), studentEmail, courseCode);
                view.displayError("You have at least one clash with an unrecorded lecture. The course cannot be added to your timetable.");
                return;

            } else {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: at least one clash with another activity)",
                        System.currentTimeMillis(), studentEmail, courseCode);
                view.displayWarning("You have at least one clash with another activity");
            }
        }

        selectedTimeSlot.setStatus(TimeSlotStatus.CHOSEN);

        if (activity instanceof Tutorial) {
            int requiredVsChosenTutorial = checkChosenTutorials(courseCode, userTimetable);
            if (requiredVsChosenTutorial > 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: number of required tutorials {} not yet chosen)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenTutorial);
                view.displayWarning("You have to choose " + requiredVsChosenTutorial + " tutorials for this course ");
            }

            if (requiredVsChosenTutorial < 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: {} more chosen tutorials than required tutorials)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenTutorial);
                view.displayWarning("You have to choose " + requiredVsChosenTutorial + " tutorials for this course ");
            }
        }
        if (activity instanceof Lecture) {
            int requiredVsChosenLab = checkChosenLabs(courseCode, userTimetable);
            if (requiredVsChosenLab > 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: number of required labs {} not yet chosen)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenLab);
                view.displayWarning("You have to choose " + requiredVsChosenLab + " tutorials for this course ");
            }

            if (requiredVsChosenLab < 0) {
                Logger.warn("{}, {}, chooseActivityForCourse, {} FAILURE (Warning: {} more chosen labs than required labs)",
                        System.currentTimeMillis(), studentEmail, courseCode, requiredVsChosenLab);
                view.displayWarning("You have to choose " + requiredVsChosenLab + " tutorials for this course ");
            }
        }
        Logger.info("{}, {}, chooseActivityForCourse, {} SUCCESS", System.currentTimeMillis(), studentEmail, activityId);
        view.displaySuccess("The activity was successfully added to your timetable");
    }

    public void testAddCourse(){
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

        List<String> courseInfoValues = Arrays.asList(
                "INFR29381",
                "informatics 2d reasoning and agents",
                "very hard course",
                "true",
                "Boris Johnson",
                "boris_johnson@gmail.com",
                "Hanna Johnson",
                "hanna_johnson@gmail.com",
                "2",
                "3"
        );

        // Create a map to hold the course information
        Map<String, String> courseInfoMap = new HashMap<>();

        // Ensure that keys and values have the same size
        if (courseInfoNames.length != courseInfoValues.size()) {
            throw new IllegalArgumentException("Keys and values must be of the same length.");
        }

        // Map each key to its corresponding value
        for (int i = 0; i < courseInfoNames.length; i++) {
            courseInfoMap.put(courseInfoNames[i], courseInfoValues.get(i));
        }

        for (Map.Entry<String, String> entry : courseInfoMap.entrySet()) {
            newCourseInfo.setField(entry.getKey(), entry.getValue());
        }

        Course newCourse = new Course(
                newCourseInfo.getCourseCode(), newCourseInfo.getName(), newCourseInfo.getDescription(), newCourseInfo.getRequiresComputers(),
                newCourseInfo.getCourseOrganiserName(), newCourseInfo.getCourseOrganiserEmail(),
                newCourseInfo.getCourseSecretaryName(), newCourseInfo.getCourseSecretaryEmail(),
                newCourseInfo.getRequiredTutorials(), newCourseInfo.getRequiredLabs()
        );

        LocalDate startDate1 = LocalDate.of(2025, 4, 2);
        LocalTime startTime1 = LocalTime.of(9, 0);
        LocalDate endDate1 = LocalDate.of(2025, 4, 2);
        LocalTime endTime1 = LocalTime.of(11, 0);
        String location1 = "Room 101";
        DayOfWeek day1 = DayOfWeek.WEDNESDAY;
        int capacity1 = 20;
        String type1 = "lab";

        newCourse.addActivity(startDate1, startTime1, endDate1, endTime1, location1, day1, capacity1, type1);

        LocalDate startDate2 = LocalDate.of(2025, 4, 3);
        LocalTime startTime2 = LocalTime.of(14, 0);
        LocalDate endDate2 = LocalDate.of(2025, 4, 3);
        LocalTime endTime2 = LocalTime.of(16, 0);
        String location2 = "Room 202";
        DayOfWeek day2 = DayOfWeek.THURSDAY;
        int capacity2 = 15;
        String type2 = "tutorial";

        newCourse.addActivity(startDate2, startTime2, endDate2, endTime2, location2, day2, capacity2, type2);

        courses.add(newCourse);
    }

}
