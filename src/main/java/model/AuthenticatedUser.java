package model;

/**
 * The AuthenticatedUser class represents a user who has been authenticated in the system.
 * It extends the User class and includes additional properties such as email and role.
 * The role is restricted to one of three valid types: AdminStaff, TeachingStaff, or Student.
 */
public class AuthenticatedUser extends User {
    private final String email;
    private final String role;

    /**
     * Constructs an AuthenticatedUser object with the specified email and role.
     *
     * @param email The email of the authenticated user. Cannot be null.
     * @param role  The role of the user, which must be one of the following:
     *              "AdminStaff", "TeachingStaff", or "Student". Cannot be null.
     * @throws IllegalArgumentException If the email is null or the role is unsupported.
     */
    public AuthenticatedUser(String email, String role) {
        if (email == null) {
            throw new IllegalArgumentException("User email cannot be null!");
        }
        if (role == null || (!role.equals("AdminStaff") && !role.equals("TeachingStaff") && !role.equals("Student"))) {
            throw new IllegalArgumentException("Unsupported user role " + role);
        }
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the email of the authenticated user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the role of the authenticated user.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }
}
