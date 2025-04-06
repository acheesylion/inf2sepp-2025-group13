package model;

import java.util.*;
import view.View;

/**
 * The SharedContext class represents a shared environment for managing various elements within the application,
 * including user authentication, inquiries, FAQs, and course management. It maintains the current user session,
 * handles FAQ subscriptions, and provides access to the course manager and other shared resources.
 */
public class SharedContext {
    public static final String ADMIN_STAFF_EMAIL = "inquiries@hindeburg.ac.nz";

    public static String COURSE_ORGANISER_EMAIL;

    public User currentUser;

    public final List<Inquiry> inquiries;
    public final FAQ faq;

    public final CourseManager courseManager;
    private final Map<String, Set<String>> faqTopicsUpdateSubscribers;

    View view;

    /**
     * Constructs a SharedContext object with the specified view.
     * Initializes the current user to a Guest, sets up inquiries, FAQ, and course manager,
     * and prepares the subscription map for FAQ topic updates.
     *
     * @param view The view used for user interaction.
     */
    public SharedContext(View view) {
        this.currentUser = new Guest();
        this.inquiries = new ArrayList<>();
        this.view = view;
        faq = new FAQ();
        courseManager = new CourseManager(view);
        faqTopicsUpdateSubscribers = new HashMap<>();
    }

    /**
     * Gets the FAQ object associated with the shared context.
     *
     * @return The FAQ object containing FAQ sections and items.
     */
    public FAQ getFAQ() {
        return faq;
    }

    /**
     * Gets the email of the current user, if they are authenticated.
     *
     * @return The email of the authenticated user, or null if the user is a guest.
     */
    public String getCurrentUserEmail() {
        if (this.currentUser instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) this.currentUser).getEmail();
        }
        return null;
    }

    /**
     * Registers a user for updates on a specific FAQ topic.
     * If the user is already registered, they are added to the topic's subscriber list.
     *
     * @param email The email of the user subscribing for updates.
     * @param topic The FAQ topic the user wants to subscribe to.
     * @return true if the user was successfully added to the topic's subscriber list, false otherwise.
     */
    public boolean registerForFAQUpdates(String email, String topic) {
        if (faqTopicsUpdateSubscribers.containsKey(topic)) {
            return faqTopicsUpdateSubscribers.get(topic).add(email);
        } else {
            Set<String> subscribers = new HashSet<>();
            subscribers.add(email);
            faqTopicsUpdateSubscribers.put(topic, subscribers);
            return true;
        }
    }

    /**
     * Unregisters a user from updates on a specific FAQ topic.
     *
     * @param email The email of the user unsubscribing from updates.
     * @param topic The FAQ topic the user wants to unsubscribe from.
     * @return true if the user was successfully removed from the topic's subscriber list, false otherwise.
     */
    public boolean unregisterForFAQUpdates(String email, String topic) {
        return faqTopicsUpdateSubscribers.getOrDefault(topic, new HashSet<>()).remove(email);
    }

    /**
     * Gets the set of users subscribed to updates on a specific FAQ topic.
     *
     * @param topic The FAQ topic to get subscribers for.
     * @return A set of emails of users subscribed to updates on the topic.
     */
    public Set<String> usersSubscribedToFAQTopic(String topic) {
        return faqTopicsUpdateSubscribers.getOrDefault(topic, new HashSet<>());
    }

    /**
     * Gets the CourseManager associated with the shared context.
     *
     * @return The CourseManager object for managing courses.
     */
    public CourseManager getCourseManager() {
        return courseManager;
    }
}
