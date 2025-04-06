package model;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private final Activity activity;
    public String courseCode;
    public TimeSlotStatus status;
    public int activityId;
    public LocalTime startTime;
    public LocalTime endTime;
    public DayOfWeek day;
    public ActivityType type;

    public TimeSlot(Activity activity, int activityId,
                    LocalTime startTime, LocalTime endTime,
                    DayOfWeek day, ActivityType type,
                    String courseCode, TimeSlotStatus status) {

        this.courseCode = courseCode;
        this.activity = activity;
        this.activityId = activityId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.type = type;
    }


    // Checks if the TimeSlot has the given course code
    public boolean hasCourseCode(String courseCode) {
        return this.courseCode.equals(courseCode);
    }



    public boolean hasActivityIdTimeSlot(int id) {return (activityId == id);}
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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String day = String.valueOf(this.day);
        String time = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
        return (String.format("| %-10s | %-19s | %-14s | %-10s | %-10s |",
                day, time, courseCode, activityId, type));
    }


}
