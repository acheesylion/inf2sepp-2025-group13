package model;

/**
 * The TimeSlotStatus enum represents the possible statuses of a time slot in a timetable.
 * A time slot can either be:
 * - UNCHOSEN: The time slot has not been selected.
 * - CHOSEN: The time slot has been selected.
 */
public enum TimeSlotStatus {
    UNCHOSEN,
    CHOSEN;
}
