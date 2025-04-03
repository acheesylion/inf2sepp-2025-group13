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

   public Activity getActivity() {return activity;}

    // Checks if the TimeSlot has the given course code
    public boolean hasCourseCode(String courseCode) {
        if (this.courseCode == null) {
            return false;
        }
        return this.courseCode.equals(courseCode);
    }


    // Checks if the TimeSlot has the given activity ID
    public boolean hasActivityId(int id) {return activity.getId() == id;}

    // Returns true if the status of the TimeSlot is CHOSEN
    public boolean isChosen() {return this.status == TimeSlotStatus.CHOSEN;}
    public void setStatus(TimeSlotStatus status) {this.status = status;}
    public LocalDate getStartDate() {return activity.getStartDate();}
    public LocalTime getStartTime() {return activity.getStartTime();}
    public LocalDate getEndDate() {return activity.getEndDate();}
    public LocalTime getEndTime() {return activity.getEndTime();}

    public int getActivityId() {return activity.getId();}
    public String getCourseCode() {return courseCode;}

    // Provides a string representation of the TimeSlot
    @Override
    public String toString() {
        return "TimeSlot{" +
                "day=" + activity.getDay() +
                ", startDate=" + activity.getStartDate() +
                ", startTime=" + activity.getStartTime() +
                ", endDate=" + activity.getEndDate() +
                ", endTime=" + activity.getEndTime() +
                ", courseCode='" + courseCode + '\'' +
                ", activityId=" + activity.getId() +
                ", status=" + status +
                '}';
    }


}
