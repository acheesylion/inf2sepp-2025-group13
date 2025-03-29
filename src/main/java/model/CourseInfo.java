package model;

public class CourseInfo {
    private String courseCode;
    private String name;
    private String description;
    private boolean requiresComputers;  // This can be a boolean, but keeping it as String for now
    private String courseOrganiserName;
    private String courseOrganiserEmail;
    private String courseSecretaryName;
    private String courseSecretaryEmail;
    private int requiredTutorials;  // This can be an integer, but keeping it as String for now
    private int requiredLabs;  // This can be an integer, but keeping it as String for now

    // Constructor to initialize all private fields
    public CourseInfo() {
        this.courseCode = "";
        this.name = "";
        this.description = "";
        this.requiresComputers = false;
        this.courseOrganiserName = "";
        this.courseOrganiserEmail = "";
        this.courseSecretaryName = "";
        this.courseSecretaryEmail = "";
        this.requiredTutorials = 0;
        this.requiredLabs = 0;
    }
    public void setitem(int num, String item){
        switch (num){
            case 0:
                this.courseCode = item;
                break;
            case 1:
                this.name = item;
                break;
            case 2:
                this.description = item;
                break;
            case 3:
                this.requiresComputers = Boolean.parseBoolean(item);
                break;
            case 4:
                this.courseOrganiserName = item;
                break;
            case 5:
                this.courseOrganiserEmail = item;
                break;
            case 6:
                this.courseSecretaryName = item;
                break;
            case 7:
                this.courseSecretaryEmail = item;
                break;
            case 8:
                this.requiredTutorials = Integer.parseInt(item);
                break;
            case 9:
                this.requiredLabs = Integer.parseInt(item);
                break;

        }
    }

    public String getCourseInfo() {
        return String.format(
                "Course Code: %s\nName: %s\nDescription: %s\nRequires Computers: %b\n" +
                        "Course Organiser: %s (%s)\nCourse Secretary: %s (%s)\n" +
                        "Required Tutorials: %d\nRequired Labs: %d",
                courseCode, name, description, requiresComputers,
                courseOrganiserName, courseOrganiserEmail,
                courseSecretaryName, courseSecretaryEmail,
                requiredTutorials, requiredLabs
        );
    }
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

    public boolean getRequiresComputers() {
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
