package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

public abstract class Activity {
    private final int id;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final LocalDate endDate;
    private final LocalTime endTime;
    private final String location;
    private final DayOfWeek day;

    // Constructor
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

    public boolean hasId(int id) {
        return this.id == id;
    }

    public int getId() {return id;}
    public LocalDate getStartDate() {return startDate;}
    public LocalTime getStartTime() {return startTime;}
    public LocalDate getEndDate() {return endDate;}
    public LocalTime getEndTime() {return endTime;}
    public String getLocation() {return location;}
    public DayOfWeek getDay() {return day;}

    protected String getCommonRow() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String dayStr = String.valueOf(this.day);
        String timeStr = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
        return String.format("| %-10s | %-19s | %-14d ", dayStr, timeStr, id);
    }

}
