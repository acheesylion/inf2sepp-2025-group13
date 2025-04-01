package model;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        boolean courseCodeIsValid = checkCourseCode(info.getCourseCode());

        if (!courseCodeIsValid){
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
        Course course1 = new Course("INFR29381", "informatics 2d reasoning and agents",
                "very hard course", true, "Boris Johnson",
                "boris_johnson@gmail.com", "Hanna Johnson",
                "hanna_johnson@gmail.com", 2, 3);

        courses.add(course1);
    }
    public void addCourseToStudentTimetable(String studentEmail, String courseCode) {
        if(hasCourse(courseCode)){
            System.out.println("has course already");
        } else {
            view.displayError("Required course info not provided");
        }
    }
}
