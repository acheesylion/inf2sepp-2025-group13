package model;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {
    private final Activity activity;
    public String courseCode;
    public TimeSlotStatus status;


    public TimeSlot(Activity activity, String courseCode, TimeSlotStatus status) {
        this.courseCode = courseCode;
        this.activity = activity;
        this.status = status;
    }


    // Checks if the TimeSlot has the given course code
    public boolean hasCourseCode(String courseCode) {
        return this.courseCode.equals(courseCode);
    }



    public boolean hasActivityId(int id) {return activity.getId() == id;}
    public boolean isChosen() {return this.status == TimeSlotStatus.CHOSEN;}
    public void setStatus(TimeSlotStatus status) {this.status = status;}
    public LocalTime getStartTime() {return activity.getStartTime();}
    public LocalTime getEndTime() {return activity.getEndTime();}
    public DayOfWeek getDay() {return activity.getDay();}
    public int getActivityId() {return activity.getId();}
    public String getCourseCode() {return courseCode;}
    public boolean isTutorial() {return activity instanceof Tutorial;}
    public boolean isLab() {return activity instanceof Lab;}
    public boolean isLecture() {return activity instanceof Lecture;}

    // Provides a string representation of the TimeSlot
    @Override
    public String toString() {
        return "TimeSlot{" +
                "day=" + activity.getDay() +
                ", startTime=" + activity.getStartTime() +
                ", endTime=" + activity.getEndTime() +
                ", courseCode='" + courseCode + '\'' +
                ", activityId=" + activity.getId() +
                ", status=" + status +
                '}';
    }


}
