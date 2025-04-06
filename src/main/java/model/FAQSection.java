package model;

import java.util.LinkedList;
import java.util.List;

/**
 * The FAQSection class represents a section of the FAQ, which contains a list of FAQ items
 * and potentially sub-sections. Each section has a topic and can have a parent section.
 */
public class FAQSection {
    private final String topic;
    private final List<FAQItem> items = new LinkedList<>();
    private FAQSection parent;
    private final List<FAQSection> subsections = new LinkedList<>();

    /**
     * Constructs a FAQSection with the given topic.
     *
     * @param topic The topic or title of the FAQ section.
     */
    public FAQSection(String topic) {
        this.topic = topic;
    }

    /**
     * Adds a new subsection to the current FAQ section.
     * The parent of the subsection is set to this FAQ section.
     *
     * @param section The FAQ section to add as a subsection.
     */
    public void addSubsection(FAQSection section) {
        subsections.add(section);
        section.parent = this;
    }

    /**
     * Gets the list of subsections within this FAQ section.
     *
     * @return A list of FAQSection objects that represent the subsections of this section.
     */
    public List<FAQSection> getSubsections() {
        return subsections;
    }

    /**
     * Gets the topic of the FAQ section.
     *
     * @return The topic of the FAQ section.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Gets the list of FAQ items in this FAQ section.
     *
     * @return A list of FAQItem objects representing the items in this section.
     */
    public List<FAQItem> getItems() {
        return items;
    }

    /**
     * Gets the parent section of this FAQ section.
     *
     * @return The parent FAQSection object, or null if this section has no parent.
     */
    public FAQSection getParent() {
        return parent;
    }

    /**
     * Sets the parent section for this FAQ section.
     *
     * @param parent The FAQSection to set as the parent of this section.
     */
    public void setParent(FAQSection parent) {
        this.parent = parent;
    }
}
