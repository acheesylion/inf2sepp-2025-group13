package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class Course {
        // Instance variables
        private String courseCode;
        private String name;
        private String description;
        private boolean requiresComputers;
        private String courseOrganiserName;
        private String courseOrganiserEmail;
        private String courseSecretaryName;
        private String courseSecretaryEmail;
        private int requiredTutorials;
        private int requiredLabs;

        // Constructor
        public Course(String courseCode, String name, String description, boolean requiresComputers,
                      String courseOrganiserName, String courseOrganiserEmail,
                      String courseSecretaryName, String courseSecretaryEmail,
                      int requiredTutorials, int requiredLabs) {
            this.courseCode = courseCode;
            this.name = name;
            this.description = description;
            this.requiresComputers = requiresComputers;
            this.courseOrganiserName = courseOrganiserName;
            this.courseOrganiserEmail = courseOrganiserEmail;
            this.courseSecretaryName = courseSecretaryName;
            this.courseSecretaryEmail = courseSecretaryEmail;
            this.requiredTutorials = requiredTutorials;
            this.requiredLabs = requiredLabs;
        }

        // Methods to manage activities
        public void addActivity(LocalDate startDate, LocalTime startTime, LocalDate endDate,
                                LocalTime endTime, String location, DayOfWeek day) {
            // Implement the functionality to add an activity
        }

        public void removeActivities() {
            // Implement the functionality to remove all activities
        }

        public boolean hasCode(String code) {
            return this.courseCode.equals(code);
        }

        public boolean hasActivityWithId(int id) {
            // Implement functionality to check if the activity with the given id exists
            return false;
        }

        public String getActivitiesAsString() {
            // Implement functionality to return activities as a String
            return "";
        }

        public boolean isUnrecordedLecture(int activityId) {
            // Implement the functionality to check if the activity with the given ID is an unrecorded lecture
            return false;
        }

        // Overriding the toString method to represent the Course object as a string
        @Override
        public String toString() {
            return "Course Code: " + courseCode + "\n" +
                    "Name: " + name + "\n" +
                    "Description: " + description + "\n" +
                    "Requires Computers: " + requiresComputers + "\n" +
                    "Course Organiser: " + courseOrganiserName + " (" + courseOrganiserEmail + ")\n" +
                    "Course Secretary: " + courseSecretaryName + " (" + courseSecretaryEmail + ")\n" +
                    "Required Tutorials: " + requiredTutorials + "\n" +
                    "Required Labs: " + requiredLabs;
        }

        // Getters and setters for instance variables (if needed)
        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isRequiresComputers() {
            return requiresComputers;
        }

        public void setRequiresComputers(boolean requiresComputers) {
            this.requiresComputers = requiresComputers;
        }

        public String getCourseOrganiserName() {
            return courseOrganiserName;
        }

        public void setCourseOrganiserName(String courseOrganiserName) {
            this.courseOrganiserName = courseOrganiserName;
        }

        public String getCourseOrganiserEmail() {
            return courseOrganiserEmail;
        }

        public void setCourseOrganiserEmail(String courseOrganiserEmail) {
            this.courseOrganiserEmail = courseOrganiserEmail;
        }

        public String getCourseSecretaryName() {
            return courseSecretaryName;
        }

        public void setCourseSecretaryName(String courseSecretaryName) {
            this.courseSecretaryName = courseSecretaryName;
        }

        public String getCourseSecretaryEmail() {
            return courseSecretaryEmail;
        }

        public void setCourseSecretaryEmail(String courseSecretaryEmail) {
            this.courseSecretaryEmail = courseSecretaryEmail;
        }

        public int getRequiredTutorials() {
            return requiredTutorials;
        }

        public void setRequiredTutorials(int requiredTutorials) {
            this.requiredTutorials = requiredTutorials;
        }

        public int getRequiredLabs() {
            return requiredLabs;
        }

        public void setRequiredLabs(int requiredLabs) {
            this.requiredLabs = requiredLabs;
        }

}
