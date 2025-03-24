package model;

public class CourseInfo {
    private String courseCode;
    private String name;
    private String description;
    private String requiresComputers;  // This can be a boolean, but keeping it as String for now
    private String courseOrganiserName;
    private String courseOrganiserEmail;
    private String courseSecretaryName;
    private String courseSecretaryEmail;
    private String requiredTutorials;  // This can be an integer, but keeping it as String for now
    private String requiredLabs;  // This can be an integer, but keeping it as String for now

    // Constructor to initialize all private fields
    public CourseInfo() {
        this.courseCode = "";
        this.name = "";
        this.description = "";
        this.requiresComputers = "";
        this.courseOrganiserName = "";
        this.courseOrganiserEmail = "";
        this.courseSecretaryName = "";
        this.courseSecretaryEmail = "";
        this.requiredTutorials = "";
        this.requiredLabs = "";
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
                this.requiresComputers = item;
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
                this.requiredTutorials = item;
                break;
            case 9:
                this.requiredLabs = item;
                break;

        }
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

    public String getRequiresComputers() {
        return requiresComputers;
    }

    public void setRequiresComputers(String requiresComputers) {
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

    public String getRequiredTutorials() {
        return requiredTutorials;
    }

    public void setRequiredTutorials(String requiredTutorials) {
        this.requiredTutorials = requiredTutorials;
    }

    public String getRequiredLabs() {
        return requiredLabs;
    }

    public void setRequiredLabs(String requiredLabs) {
        this.requiredLabs = requiredLabs;
    }
}
