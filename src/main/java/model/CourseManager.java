package model;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import view.TextUserInterface;
import view.View;
import org.tinylog.Logger;

public class CourseManager {

    View view;
    /* + CourseManager(view: View)
+ addCourse(
code: String, name: String, description: String, requiresComputers:boolean, COName: String, COEmail: String, CSName: String, CSEmail: String,reqTuorials: int, reqLabs: int): boolean
+ checkCourseCode(courseCode: String): boolean
+ removeCourse(courseCode: String): String[]
+ addCourseToStudentTimetable(studentEmail : String, courseCode: String)
+chooseActivityForCourse(
studentEmail: String,
courseCode:String, acivityId: int)
- hasCourse(courseCode: String): boolean
+ viewCourses(): String
+ viewCourse(courseCode:String):String
- checkChosenTutorials(courseCode: String, timetable: Timetable): boolean
- checkChosenLabs(courseCode: String, timetable: Timetable): boolean
- getTimeable(studentEmail: String): Timetable
+ viewTimetable(
studentEmail: String
)
*/
    // Map to store courses by course code
    private Map<String, Course> courses;
    // Map to store students' timetables by email
    private Map<String, Timetable> studentTimetables;

    public CourseManager(){
        courses = new HashMap<>();
        studentTimetables = new HashMap<>();
    }

    public boolean checkCourseCode(String courseCode){
        // TODO idk what makes a course valid
        return (true);
    }

    public boolean addCourse(String code, String name, String description, boolean requiresComputers, String COName, String COEmail, String CSName, String CSEmail,
                      int requiredTutorials, int requiredLabs, String email, CourseInfo course){
        if (code == null || code.isEmpty() || name == null || name.isEmpty() ||
                description == null || description.isEmpty() || COName == null || COName.isEmpty() ||
                COEmail == null || COEmail.isEmpty() || CSName == null || CSName.isEmpty() ||
                CSEmail == null || CSEmail.isEmpty() || requiredTutorials < 0 || requiredLabs < 0) {
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Required course info not provided)", System.currentTimeMillis(), email, course.getCourseInfo() );
            view.displayError("Required course info not provided");

            return false; // Indicate failure
        }
        boolean courseCodeIsValid = this.checkCourseCode(code);

        if (!courseCodeIsValid){
            //System.currentTimeMillis(),email,"addCourse",courseInfo,"FAILURE"+" (Error: Provided courseCode is invalid)")
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Provided courseCode is invalid)", System.currentTimeMillis(), email, course.getCourseInfo() );
            view.displayError("Provided courseCode is invalid");

            return false;
        }

        boolean hasCode = false;

        for (Course temp : courses.values()) {
            if (temp.hasCode(code)) {
                hasCode = true;
                break; // Exit loop once a match is found
            }
        }

        if (hasCode){
            //System.currentTimeMillis(),email,"addCourse",courseInfo,"FAILURE"+" (Error: Provided courseCode is invalid)")
            Logger.error("{}, {}, addCourse, {} FAILURE (Error: Course with that code already exists)", System.currentTimeMillis(), email, course.getCourseInfo() );
            view.displayError("Course with that code already exists");

            return false;
        }
//displayInfo("=== Add Course - Activities===")
        view.displayInfo("=== Add Course - Activities===");

        Course new_course = new Course(code, name, description, requiresComputers, COName, COEmail, CSName, CSEmail,
        requiredTutorials, requiredLabs);

        int numactivities = Integer.parseInt(view.getInput("Enter how many activities you wish to add"));
       // startDate, startTime, endDate, endTime, location, day, the type of activity
        for(int i = 0; i < numactivities; i++){
            LocalDate startDate = LocalDate.parse(view.getInput("Enter startDate"));
            LocalTime startTime = LocalTime.parse(view.getInput("Enter startTime"));
            LocalDate endDate = LocalDate.parse(view.getInput("Enter endDate"));
            LocalTime endTime = LocalTime.parse(view.getInput("Enter endTime"));
            String location = view.getInput("Enter location");
            DayOfWeek day = DayOfWeek.valueOf(view.getInput("Enter day (e.g 'MONDAY')").toUpperCase());
            String type = view.getInput("Enter type (lab, tutorial, lecture)");
            //capacity for labs and tutorials, whether recorded for lectures
            if (Objects.equals(type, "lab")){
                int extra = Integer.parseInt(view.getInput("Enter capacity"));
                new_course.addActivity(startDate, startTime, endDate, endTime, location, day, extra, type);
            }
            if (Objects.equals(type, "tutorial")){
                int extra = Integer.parseInt(view.getInput("Enter capacity"));
                new_course.addActivity(startDate, startTime, endDate, endTime, location, day, extra, type);
            }
            if (Objects.equals(type, "lecture")){
                boolean extra = Boolean.parseBoolean(view.getInput("Enter if lectures are recorded (true/false)"));
                new_course.addActivity(startDate, startTime, endDate, endTime, location, day, extra, type);
            }

        }
        Logger.info("{}, {}, addCourse, {} SUCCESS (New course added)", System.currentTimeMillis(), email, course.getCourseInfo() );
        view.displaySuccess("Course has been successfully created");

        return (true);
    }
}
