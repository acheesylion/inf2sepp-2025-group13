package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The Lecture class represents a lecture activity. It extends the Activity class and includes
 * an additional property to specify whether the lecture is recorded. This class defines a lecture session
 * within a course or schedule.
 */
public class Lecture extends Activity {
    private final boolean recorded;

    /**
     * Constructs a Lecture object with the specified details.
     *
     * @param id         The unique identifier for the activity.
     * @param startDate  The start date of the lecture activity.
     * @param startTime  The start time of the lecture activity.
     * @param endDate    The end date of the lecture activity.
     * @param endTime    The end time of the lecture activity.
     * @param location   The location where the lecture activity takes place.
     * @param day        The day of the week the lecture activity occurs.
     * @param recorded   A boolean indicating if the lecture is recorded.
     */
    public Lecture(int id, LocalDate startDate, LocalTime startTime,
                   LocalDate endDate, LocalTime endTime, String location,
                   DayOfWeek day, boolean recorded) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.recorded = recorded;
    }

    /**
     * Gets whether the lecture is recorded.
     *
     * @return true if the lecture is recorded, false otherwise.
     */
    public boolean getRecorded() {
        return (this.recorded);
    }

    /**
     * Returns a string representation of the lecture activity, including its type and recorded status,
     * along with the common details inherited from the Activity class.
     *
     * @return A formatted string representing the lecture activity.
     */
    @Override
    public String toString() {
        String typeInfo = recorded ? "Lecture (recorded)" : "Lecture (unrecorded)";
        return getCommonRow() + String.format("| %-25s |", typeInfo);
    }
}
