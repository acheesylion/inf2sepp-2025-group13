package model;
import java.sql.SQLOutput;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.*;

import view.TextUserInterface;
import view.View;
import org.tinylog.Logger;

/* + CourseManager(view: View)
+ addCourse(
code: String, name: String, description: String, requiresComputers:boolean, COName: String, COEmail: String, CSName: String, CSEmail: String,reqTuorials: int, reqLabs: int): boolean
+ checkCourseCode(courseCode: String): boolean
+ removeCourse(courseCode: String): String[]
+ addCourseToStudentTimetable(studentEmail : String, courseCode: String)
+chooseActivityForCourse(
studentEmail: String,
courseCode:String, activityId: int)
- hasCourse(courseCode: String): boolean
+ viewCourses(): String
+ viewCourse(courseCode:String):String
- checkChosenTutorials(courseCode: String, timetable: Timetable): boolean
- checkChosenLabs(courseCode: String, timetable: Timetable): boolean
- getTimetable(studentEmail: String): Timetable
+ viewTimetable(
studentEmail: String
)
*/
public class CourseManager {
    // Map to store courses by course code
    private final List<Course> courses;
    protected final View view;

    // Map to store students' timetables by email
//    private Map<String, Timetable> studentTimetables;

    public CourseManager(View view) {
        this.courses = new ArrayList<>();
        this.view = view;
    }

    public void viewCourses(){
        //changed from (Course course : courses.values())
        for (Course course : courses){
            view.displayCourse(course);
        }
    }

    public boolean checkCourseCode(String courseCode){
        return courseCode.matches("^[A-Z]{4}\\d{5}$");
    }

    private boolean hasCourse(String courseCode){
        for (Course course : courses) {
            if (course.hasCode(courseCode)) {
                return true;
            }
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

    public void addActivitiesToCourse(Course new_course, View view) {
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

    public void addCourse(String email, CourseInfo info)
    {
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
                info.getCourseCode(), info.getName(), info.getDescription(), info.getRequiresComputers(),
                info.getCourseOrganiserName(), info.getCourseOrganiserEmail(),
                info.getCourseSecretaryName(), info.getCourseSecretaryEmail(),
                info.getRequiredTutorials(), info.getRequiredLabs()
        );

        addActivitiesToCourse(newCourse, view);
        courses.add(newCourse);

        Logger.info("{}, {}, addCourse, {} SUCCESS (New course added)", System.currentTimeMillis(), email, info.getCourseInfo() );
        view.displaySuccess("Course has been successfully created");

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

    public Course getCourseByCode(List<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null; // Return null if no matching course is found.
    }

    public void addCourseToStudentTimetable(String studentEmail, String courseCode) {
        if(!hasCourse(courseCode)){
            Logger.error("{}, {}, addCoursetoStudentTimetable, {} FAILURE (Error: Incorrect course code provided  )",
                    System.currentTimeMillis(), studentEmail, courseCode );
            view.displayError("Incorrect course code");
        } else {
            Course courseToBeAdded = getCourseByCode(courses, courseCode);
            List<String> fullActivityDetailsAsString = courseToBeAdded.getActivitiesAsString();

        }
//        for (Timetable timetable : timetables) {
//        }

    }
}
