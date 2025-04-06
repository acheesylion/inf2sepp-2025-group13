package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

/**
 * The Activity class represents an event or activity with specific details such as
 * ID, dates, times, location, and day of the week. This class is intended to be
 * extended by other classes that define specific types of activities.
 */
public abstract class Activity {
    private final int id;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final LocalDate endDate;
    private final LocalTime endTime;
    private final String location;
    private final DayOfWeek day;

    /**
     * Constructs an Activity object with the given details.
     *
     * @param id        The unique identifier for the activity.
     * @param startDate The start date of the activity.
     * @param startTime The start time of the activity.
     * @param endDate   The end date of the activity.
     * @param endTime   The end time of the activity.
     * @param location  The location where the activity takes place.
     * @param day       The day of the week the activity occurs.
     */
    public Activity(int id, LocalDate startDate, LocalTime startTime,
                    LocalDate endDate, LocalTime endTime, String location, DayOfWeek day) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.location = location;
        this.day = day;
    }

    /**
     * Checks if the given ID matches the ID of this activity.
     *
     * @param id The ID to check.
     * @return true if the IDs match, false otherwise.
     */
    public boolean hasId(int id) {
        return this.id == id;
    }

    /**
     * Gets the unique identifier for this activity.
     *
     * @return The ID of the activity.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the start time of the activity.
     *
     * @return The start time of the activity.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the activity.
     *
     * @return The end time of the activity.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the day of the week the activity occurs.
     *
     * @return The day of the week of the activity.
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * Gets a formatted string representing the common details of the activity
     * (day, time range, and ID) to be displayed in a table row.
     *
     * @return A formatted string with the common details of the activity.
     */
    protected String getCommonRow() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String dayStr = String.valueOf(this.day);
        String timeStr = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
        return String.format("| %-10s | %-19s | %-14d ", dayStr, timeStr, id);
    }

}
