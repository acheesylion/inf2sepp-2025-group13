package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Lecture extends Activity {
    private final boolean recorded;

    public Lecture(int id, LocalDate startDate, LocalTime startTime,
                   LocalDate endDate, LocalTime endTime, String location,
                   DayOfWeek day, boolean recorded) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.recorded = recorded;
    }

    public Lecture(Lecture other) {
        super(other.getId(), other.getStartDate(), other.getStartTime(),
                other.getEndDate(), other.getEndTime(), other.getLocation(), other.getDay());
        this.recorded = other.recorded;
    }

    public boolean getRecorded() {
        return (this.recorded);
    }

    @Override
    public String toString() {
        String typeInfo = recorded ? "Lecture (recorded)" : "Lecture (unrecorded)";
        return getCommonRow() + String.format("| %-25s |", typeInfo);
    }

}
