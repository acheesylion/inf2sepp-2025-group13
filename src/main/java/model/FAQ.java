package model;

import java.util.LinkedList;
import java.util.List;

/**
 * The FAQ class represents a Frequently Asked Questions (FAQ) collection,
 * which contains multiple FAQ sections. It provides methods to add and retrieve FAQ sections.
 */
public class FAQ {
    private final List<FAQSection> sections = new LinkedList<>();

    /**
     * Adds a new FAQ section to the FAQ collection.
     * The section is added to the list, and its parent is set to null.
     *
     * @param section The FAQ section to add.
     */
    public void addSection(FAQSection section) {
        sections.add(section);
        section.setParent(null);
    }

    /**
     * Retrieves the list of FAQ sections in the FAQ collection.
     *
     * @return A list of FAQSection objects representing the sections of the FAQ.
     */
    public List<FAQSection> getSections() {
        return sections;
    }
}
