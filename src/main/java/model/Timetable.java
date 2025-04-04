package model;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Timetable {

    private final String studentEmail;
    private final List<TimeSlot> timeSlots;

    // Constructor initializing the studentEmail and the timeSlots list
    public Timetable(String studentEmail) {
        this.studentEmail = studentEmail;
        this.timeSlots = new ArrayList<>();
    }

    public void addTimeSlot(Activity activity, String courseCode) {
        // When adding a new time slot, we default its status to UNCHOSEN.

        TimeSlot newSlot = null;
        if (activity instanceof Lecture) {
            newSlot = new TimeSlot(activity, courseCode, TimeSlotStatus.CHOSEN);
        } else {
            newSlot = new TimeSlot(activity, courseCode, TimeSlotStatus.UNCHOSEN);
        }
        timeSlots.add(newSlot);
    }

    public TimeSlot getTimeSlotByActivityId(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityId(activityId))
                .findFirst()
                .orElse(null);
    }

    public int numChosenActivities(String courseCode) {
        int count = 0;
        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode) && ts.isChosen()) {
                count++;
            }
        }
        return count;
    }


    /**
     * Checks if there is a conflict between the specified time interval and any existing time slots.
     *
     * <p>
     * The method iterates over all {@code TimeSlot} objects stored in {@code timeSlots} and compares their
     * intervals with the provided start and end dates/times. If an overlapping interval is found,
     * it returns an array containing the course code and activity ID of the conflicting time slot.
     * Otherwise, an empty array is returned.
     * </p>
     *
     * @param startTime the start time of the time interval to check
     * @param endTime   the end time of the time interval to check
     * @return an array of two strings: the first element is the course code and the second element is the activity ID
     *         of the conflicting time slot, or an empty array if no conflict is found.
     */
    public String[] checkConflicts(DayOfWeek day,  LocalTime startTime, LocalTime endTime) {

        for (TimeSlot ts : timeSlots) {
           if (ts.getDay().equals(day))
            if (startTime.isBefore(ts.getEndTime()) && endTime.isAfter(ts.getStartTime())) {
                String conflictCourseCode = ts.getCourseCode();
                int conflictID = ts.getActivityId();
                return (new String[] {conflictCourseCode, Integer.toString(conflictID)});
            }
        }

        // Return an empty array if no conflicts are found.
        return (new String[0]);
    }


    public boolean hasStudentEmail(String email) {return (this.studentEmail.equals(email));}

    /**
     * Chooses an activity for a given course by setting its status to CHOSEN.
     *
     * @param courseCode the course code of the activity to choose
     * @param activityId the activity ID of the activity to choose
     * @return true if the activity was found and marked as CHOSEN, false otherwise
     */
    public void chooseActivity(String courseCode, int activityId) {
        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode) && ts.hasActivityId(activityId)) {
                if (!ts.isChosen()) {
                    ts.setStatus(TimeSlotStatus.CHOSEN);
                }
            }
        }
    }

    public boolean hasSlotsForCourse(String courseCode) {

        if (timeSlots == null) {
            return false;
        }

        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode)) {
                return true;
            }
        }
        return false;
    }

    public void removeSlotsForCourse(String courseCode) {
        timeSlots.removeIf(ts -> ts.hasCourseCode(courseCode));
    }

    public int numChosenTutorialInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.isTutorial() && ts.isChosen())
                .count();
    }

    public int numChosenLabInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.isLab() && ts.isChosen())
                .count();
    }





    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Header block with a centered title.
        String headerLine = "==============================================================";
        sb.append(headerLine).append("\n");
        String title = "Timetable for " + studentEmail;
        int totalWidth = headerLine.length();
        int padding = (totalWidth - title.length()) / 2;
        String centeredTitle = " ".repeat(Math.max(0, padding)) + title;
        sb.append(centeredTitle).append("\n");
        sb.append(headerLine).append("\n\n");

        // Define table column widths:
        // Day: 10, Time: 19, CourseCode: 10, ActivityId: 10, Type: 12.
        String tableLine = "+------------+---------------------+------------+------------+--------------+";

        // Print table header.
        sb.append(tableLine).append("\n");
        sb.append(String.format("| %-10s | %-19s | %-10s | %-10s | %-12s |%n",
                "Day", "Time", "CourseCode", "ActivityId", "Type"));
        sb.append(tableLine).append("\n");

        // Formatter for time.
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Filter to only chosen timeslots, sort by day and then by start time.
        List<TimeSlot> sortedSlots = timeSlots.stream()
                .filter(TimeSlot::isChosen)
                .sorted(Comparator.comparing(TimeSlot::getDay)
                        .thenComparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

        // Print each timeslot.
        for (TimeSlot ts : sortedSlots) {
            String day = ts.getDay().toString();
            String time = ts.getStartTime().format(timeFormatter) + " - " + ts.getEndTime().format(timeFormatter);
            String courseCode = ts.getCourseCode();
            int activityId = ts.getActivityId();
            String type = ts.isLecture() ? "Lecture" :
                    ts.isLab() ? "Lab" :
                            ts.isTutorial() ? "Tutorial" : "Unknown";
            sb.append(String.format("| %-10s | %-19s | %-10s | %-10d | %-12s |%n",
                    day, time, courseCode, activityId, type));
        }

        sb.append(tableLine).append("\n");
        return sb.toString();
    }
}
