package model;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {

    // Private fields (as indicated by '-' in UML)
    private DayOfWeek day;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    public String courseCode;
    public int activityId;
    public TimeSlotStatus status;

    // Constructor initializing all fields
    public TimeSlot(DayOfWeek day, LocalDate startDate, LocalTime startTime,
                    LocalDate endDate, LocalTime endTime, String courseCode,
                    int activityId, TimeSlotStatus status) {

        this.day = day;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.courseCode = courseCode;
        this.activityId = activityId;
        this.status = status;
    }

    public TimeSlot(Activity activity, String courseCode, TimeSlotStatus status) {
        // Use Activity getters to initialize the time-related fields.
        this(activity.getDay(),
                activity.getStartDate(),
                activity.getStartTime(),
                activity.getEndDate(),
                activity.getEndTime(),
                courseCode,
                activity.getId(), // Use the activity's id for the timeslot's activityId.
                status);
    }

    // Checks if the TimeSlot has the given course code
    public boolean hasCourseCode(String courseCode) {
        if (this.courseCode == null) {
            return false;
        }
        return this.courseCode.equals(courseCode);
    }

    // Checks if the TimeSlot has the given activity ID
    public boolean hasActivityId(int id) {return this.activityId == id;}

    // Returns true if the status of the TimeSlot is CHOSEN
    public boolean isChosen() {return this.status == TimeSlotStatus.CHOSEN;}
    public void setStatus(TimeSlotStatus status) {this.status = status;}
    public LocalDate getStartDate() {return startDate;}
    public LocalTime getStartTime() {return startTime;}
    public LocalDate getEndDate() {return endDate;}
    public LocalTime getEndTime() {return endTime;}

    public int getActivityId() {return activityId;}
    public String getCourseCode() {return courseCode;}

    // Provides a string representation of the TimeSlot
    @Override
    public String toString() {
        return "TimeSlot{" +
                "day=" + day +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", courseCode='" + courseCode + '\'' +
                ", activityId=" + activityId +
                ", status=" + status +
                '}';
    }


}
