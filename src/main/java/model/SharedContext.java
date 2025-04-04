package model;

import java.util.*;
import view.View;

public class SharedContext {
    public static final String ADMIN_STAFF_EMAIL = "inquiries@hindeburg.ac.nz";

    public static String COURSE_ORGANISER_EMAIL;

    public User currentUser;

    public final List<Inquiry> inquiries;
    public final FAQ faq;

    public final CourseManager courseManager;
    private final Map<String, Set<String>> faqTopicsUpdateSubscribers;

    View view;

    public SharedContext(View view) {
        this.currentUser = new Guest();
        this.inquiries = new ArrayList<>();
        this.view = view;
        faq = new FAQ();
        courseManager = new CourseManager(view);
        faqTopicsUpdateSubscribers = new HashMap<>();
    }

    public FAQ getFAQ() {
        return faq;
    }
    public String getCurrentUserRole(){
        if (this.currentUser instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) this.currentUser).getRole();
        }
        return null;
    }
    public String getCurrentUserEmail(){
        if (this.currentUser instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) this.currentUser).getEmail();
        }
        return null;
    }

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

    public boolean unregisterForFAQUpdates(String email, String topic) {
        return faqTopicsUpdateSubscribers.getOrDefault(topic, new HashSet<>()).remove(email);
    }

    public Set<String> usersSubscribedToFAQTopic(String topic) {
        return faqTopicsUpdateSubscribers.getOrDefault(topic, new HashSet<>());
    }

    public CourseManager getCourseManager(){
        return courseManager;
    }
}
