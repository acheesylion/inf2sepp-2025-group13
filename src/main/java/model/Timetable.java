package model;
import java.time.DayOfWeek;
import java.time.LocalTime;
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

    public void addTimeSlot(Activity activity, String courseCode, ActivityType type) {
        // When adding a new time slot, we default its status to UNCHOSEN.
        if (type == ActivityType.LECTURE) {
            TimeSlot newSlot = new TimeSlot(
                    activity.getId(),
                    activity.getStartTime(), activity.getEndTime(),
                    activity.getDay(), ActivityType.LECTURE,
                    courseCode, TimeSlotStatus.CHOSEN);
            timeSlots.add(newSlot);
        }
        if (type == ActivityType.LAB) {
            TimeSlot newSlot = new TimeSlot(
                    activity.getId(),
                    activity.getStartTime(), activity.getEndTime(),
                    activity.getDay(), ActivityType.LAB,
                    courseCode, TimeSlotStatus.UNCHOSEN);
            timeSlots.add(newSlot);
        }

        if (type == ActivityType.TUTORIAL) {
            TimeSlot newSlot = new TimeSlot(
                    activity.getId(),
                    activity.getStartTime(), activity.getEndTime(),
                    activity.getDay(), ActivityType.TUTORIAL,
                    courseCode, TimeSlotStatus.UNCHOSEN);
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
                if (conflict.isType(ActivityType.LECTURE)) {
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
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.hasActivityIdTimeSlot(activityId) && !ts.isChosen())
                .forEach(ts -> ts.setStatus(TimeSlotStatus.CHOSEN));
    }

    public boolean isIdTutorial(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityIdTimeSlot(activityId))
                .anyMatch(ts->ts.isType(ActivityType.TUTORIAL));
    }

    public boolean isIdLab(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityIdTimeSlot(activityId))
                .anyMatch(ts->ts.isType(ActivityType.LAB));
    }

    // Contains a timeslot with the given courseCode
    public boolean hasSlotsForCourse(String courseCode) {
        return timeSlots.stream().anyMatch(ts -> ts.hasCourseCode(courseCode));
    }

    // Contains a timeslot with the given activityId
    public boolean hasSlotsForActivityId(int activityId) {
        return timeSlots.stream().anyMatch(ts -> ts.hasActivityIdTimeSlot(activityId));
    }

    public void removeSlotsForCourse(String courseCode) {
        timeSlots.removeIf(ts -> ts.hasCourseCode(courseCode));
    }

    public int numChosenTutorialInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.isType(ActivityType.TUTORIAL) && ts.isChosen())
                .count();
    }

    public int numChosenLabInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.isType(ActivityType.LAB) && ts.isChosen())
                .count();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================\n");
        sb.append(String.format("Timetable for %s\n", studentEmail));
        sb.append("========================================================================\n\n");
        String tableLine = "+------------+---------------------+----------------+-------------------------+\n";
        sb.append("Timeslots:\n");
        sb.append(tableLine);
        sb.append(String.format("| %-10s | %-19s | %-14s | %-10s | %-10s |\n",
                "Day", "Time", "CourseCode", "ActivityId", "Type"));
        sb.append(tableLine);

        List<TimeSlot> sortedSlots = timeSlots.stream()
                .filter(TimeSlot::isChosen)
                .sorted(Comparator.comparing(TimeSlot::getDay)
                        .thenComparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

        for (TimeSlot ts : sortedSlots) {
            sb.append(ts.toString()).append("\n");
        }
        sb.append(tableLine);
        return sb.toString();
    }


}
