package model;

/**
 * The CourseInfo class represents detailed information about a course, including its code, name, description,
 * and details about the course organiser, course secretary, tutorials, and labs.
 */
public class CourseInfo {
    private String courseCode;
    private String name;
    private String description;
    private Boolean requiresComputers;
    private String courseOrganiserName;
    private String courseOrganiserEmail;
    private String courseSecretaryName;
    private String courseSecretaryEmail;
    private String requiredTutorials;
    private String requiredLabs;

    /**
     * Constructs a new CourseInfo object.
     */
    public CourseInfo() {}

    /**
     * Sets the specified field of the course information.
     *
     * @param fieldName The name of the field to set.
     * @param item      The value to assign to the field.
     */
    public void setField(String fieldName, String item) {
        switch (fieldName) {
            case "courseCode":
                this.courseCode = item;
                break;
            case "name":
                this.name = item;
                break;
            case "description":
                this.description = item;
                break;
            case "courseOrganiserName":
                this.courseOrganiserName = item;
                break;
            case "courseOrganiserEmail":
                this.courseOrganiserEmail = item;
                break;
            case "courseSecretaryName":
                this.courseSecretaryName = item;
                break;
            case "courseSecretaryEmail":
                this.courseSecretaryEmail = item;
                break;
            case "requiredTutorials":
                this.requiredTutorials = item;
                break;
            case "requiredLabs":
                this.requiredLabs = item;
                break;
        }
    }

    /**
     * Sets whether the course requires computers.
     *
     * @param requiresComputers true if the course requires computers, false otherwise.
     */
    public void setRequiresComputers(Boolean requiresComputers) {
        this.requiresComputers = requiresComputers;
    }

    /**
     * Retrieves the course information as a formatted string.
     *
     * @return A string representing the course's details.
     */
    public String getCourseInfo() {
        return String.format(
                "Course Code: %s\nName: %s\nDescription: %s\nRequires Computers: %s\n" +
                        "Course Organiser: %s (%s)\nCourse Secretary: %s (%s)\n" +
                        "Required Tutorials: %s\nRequired Labs: %s",
                courseCode, name, description, requiresComputers,
                courseOrganiserName, courseOrganiserEmail,
                courseSecretaryName, courseSecretaryEmail,
                requiredTutorials, requiredLabs
        );
    }

    /**
     * Gets the course code.
     *
     * @return The course code.
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Gets the name of the course.
     *
     * @return The name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the course.
     *
     * @return The description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if the course requires computers.
     *
     * @return true if the course requires computers, false otherwise.
     */
    public Boolean getRequiresComputers() {
        return requiresComputers;
    }

    /**
     * Gets the name of the course organiser.
     *
     * @return The name of the course organiser.
     */
    public String getCourseOrganiserName() {
        return courseOrganiserName;
    }

    /**
     * Gets the email of the course organiser.
     *
     * @return The email of the course organiser.
     */
    public String getCourseOrganiserEmail() {
        return courseOrganiserEmail;
    }

    /**
     * Gets the name of the course secretary.
     *
     * @return The name of the course secretary.
     */
    public String getCourseSecretaryName() {
        return courseSecretaryName;
    }

    /**
     * Gets the email of the course secretary.
     *
     * @return The email of the course secretary.
     */
    public String getCourseSecretaryEmail() {
        return courseSecretaryEmail;
    }

    /**
     * Gets the required number of tutorials for the course.
     *
     * @return The required number of tutorials for the course.
     */
    public String getRequiredTutorials() {
        return requiredTutorials;
    }

    /**
     * Gets the required number of labs for the course.
     *
     * @return The required number of labs for the course.
     */
    public String getRequiredLabs() {
        return requiredLabs;
    }
}
