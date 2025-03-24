package model;
import java.util.HashMap;
import java.util.Map;
import view.View;
import org.tinylog.Logger;

public class CourseManager {
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

    public boolean addCourse(String code, String name, String description, boolean requiresComputers, String COName, String COEmail, String CSName, String CSEmail,
                      int requiredTutorials, int requiredLabs ){
        return (true);
    }
}
