package model;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Timetable {

    private final String studentEmail;
    private final List<TimeSlot> timeSlots;

    public Timetable(String studentEmail) {
        this.studentEmail = studentEmail;
        this.timeSlots = new ArrayList<>();
    }

    public void addTimeSlot(Activity activity, String courseCode) {
        // When adding a new time slot, we default its status to UNCHOSEN.
        if (activity instanceof Lecture) {
            TimeSlot newSlot = new TimeSlot(activity, courseCode, TimeSlotStatus.CHOSEN);
            timeSlots.add(newSlot);
        } else {
            TimeSlot newSlot = new TimeSlot(activity, courseCode, TimeSlotStatus.UNCHOSEN);
            timeSlots.add(newSlot);
        }

    }

    public boolean hasStudentEmail(String email) {return (this.studentEmail.equals(email));}


    public String[] checkConflicts(DayOfWeek day, LocalTime startTime, LocalTime endTime) {

        List<TimeSlot> conflicts = new ArrayList<>();
        for (TimeSlot ts : timeSlots) {
            if (ts.isChosen()) {
                if (ts.getDay().equals(day)) {
                    if (startTime.isBefore(ts.getEndTime()) && endTime.isAfter(ts.getStartTime())) {
                        conflicts.add(ts);
                    }
                }
            }

        }
        if (conflicts.isEmpty()) {
            return new String[0];
        } else {
            for (TimeSlot conflict : conflicts) {
                if (conflict.isLecture()) {
                    String conflictCourseCode = conflict.getCourseCode();
                    int conflictID = conflict.getActivityId();
                    return (new String[] {conflictCourseCode, Integer.toString(conflictID)});
                }
            }
            // Get Head of Conflicts List
            String conflictCourseCode = conflicts.get(0).getCourseCode();
            int conflictID = conflicts.get(0).getActivityId();
            return (new String[] {conflictCourseCode, Integer.toString(conflictID)});
        }
    }


    public void chooseActivity(String courseCode, int activityId) {
        timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.hasActivityId(activityId) && !ts.isChosen())
                .forEach(ts -> ts.setStatus(TimeSlotStatus.CHOSEN));
    }

    public boolean isIdTutorial(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityId(activityId))
                .anyMatch(TimeSlot::isTutorial);
    }

    public boolean isIdLab(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityId(activityId))
                .anyMatch(TimeSlot::isLab);
    }

    // Contains a timeslot with the given courseCode
    public boolean hasSlotsForCourse(String courseCode) {
        return timeSlots.stream().anyMatch(ts -> ts.hasCourseCode(courseCode));
    }

    // Contains a timeslot with the given activityId
    public boolean hasSlotsForActivityId(int activityId) {
        return timeSlots.stream().anyMatch(ts -> ts.hasActivityId(activityId));
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
        String headerLine = "==============================================================";
        sb.append(headerLine).append("\n");
        String title = "Timetable for " + studentEmail;
        int totalWidth = headerLine.length();
        int padding = (totalWidth - title.length()) / 2;
        String centeredTitle = " ".repeat(Math.max(0, padding)) + title;
        sb.append(centeredTitle).append("\n");
        sb.append(headerLine).append("\n\n");
        String tableLine = "+------------+---------------------+------------+------------+--------------+";
        sb.append(tableLine).append("\n");
        sb.append(String.format("| %-10s | %-19s | %-10s | %-10s | %-12s |%n",
                "Day", "Time", "CourseCode", "ActivityId", "Type"));
        sb.append(tableLine).append("\n");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<TimeSlot> sortedSlots = timeSlots.stream()
                .filter(TimeSlot::isChosen)
                .sorted(Comparator.comparing(TimeSlot::getDay)
                        .thenComparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

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
