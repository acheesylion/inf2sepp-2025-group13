package model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The TimeSlot class represents a specific time slot for a particular activity in a course.
 * It includes details such as the course code, activity ID, start and end time, the day of the week,
 * the type of activity (e.g., lecture, lab), and the status of the time slot (whether it is chosen or not).
 */
public class TimeSlot {
    public String courseCode;
    public TimeSlotStatus status;
    public int activityId;
    public LocalTime startTime;
    public LocalTime endTime;
    public DayOfWeek day;
    public ActivityType type;

    /**
     * Constructs a TimeSlot object with the specified details.
     *
     * @param activityId The unique identifier for the activity associated with the time slot.
     * @param startTime  The start time of the time slot.
     * @param endTime    The end time of the time slot.
     * @param day        The day of the week the time slot occurs.
     * @param type       The type of activity (e.g., lecture, lab).
     * @param courseCode The course code associated with the time slot.
     * @param status     The current status of the time slot (e.g., chosen or not).
     */
    public TimeSlot(int activityId,
                    LocalTime startTime, LocalTime endTime,
                    DayOfWeek day, ActivityType type,
                    String courseCode, TimeSlotStatus status) {

        this.courseCode = courseCode;
        this.activityId = activityId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.type = type;
    }

    /**
     * Checks if the time slot is associated with a given course code.
     *
     * @param courseCode The course code to check.
     * @return true if the time slot matches the given course code, false otherwise.
     */
    public boolean hasCourseCode(String courseCode) {
        return this.courseCode.equals(courseCode);
    }

    /**
     * Checks if the time slot has the given activity ID.
     *
     * @param id The activity ID to check.
     * @return true if the time slot matches the given activity ID, false otherwise.
     */
    public boolean hasActivityIdTimeSlot(int id) {
        return (activityId == id);
    }

    /**
     * Checks if the time slot has been chosen.
     *
     * @return true if the time slot is marked as chosen, false otherwise.
     */
    public boolean isChosen() {
        return this.status == TimeSlotStatus.CHOSEN;
    }

    /**
     * Sets the status of the time slot.
     *
     * @param status The new status to set for the time slot.
     */
    public void setStatus(TimeSlotStatus status) {
        this.status = status;
    }

    /**
     * Gets the start time of the time slot.
     *
     * @return The start time of the time slot.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the time slot.
     *
     * @return The end time of the time slot.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the day of the week the time slot occurs.
     *
     * @return The day of the week for the time slot.
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Gets the activity ID associated with the time slot.
     *
     * @return The activity ID of the time slot.
     */
    public int getActivityId() {
        return activityId;
    }

    /**
     * Gets the course code associated with the time slot.
     *
     * @return The course code for the time slot.
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Checks if the time slot matches the given activity type.
     *
     * @param type The activity type to check (e.g., lab, tutorial, lecture).
     * @return true if the time slot matches the given type, false otherwise.
     */
    public boolean isType(ActivityType type) {
        return type == this.type;
    }

    /**
     * Provides a string representation of the time slot, including details such as the course code,
     * activity ID, time range, day, and activity type.
     *
     * @return A formatted string representing the time slot.
     */
    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String day = String.valueOf(this.day);
        String time = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
        return (String.format("| %-10s | %-19s | %-14s | %-10s | %-10s |",
                day, time, courseCode, activityId, type));
    }
}
