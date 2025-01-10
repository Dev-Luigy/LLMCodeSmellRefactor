package org.example.studyplanner;

import java.text.MessageFormat;
import java.util.Objects;

public class ToDo implements PlannerMaterial {
    private Integer id;
    private String title;
    private String description;
    private int priority;

    public ToDo(Integer id, String title, String description, int priority) {
        validatePriority(priority);
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[(Priority:{3}) ToDo {0}: {1}, {2}]", id, title, description, priority);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        validatePriority(priority);
        this.priority = priority;
    }

    private void validatePriority(int priority) {
        if (priority < 0 || priority > 10) {
            throw new IllegalArgumentException("Priority must be between 0 and 10");
        }
    }

    // Additional behavior methods
    public void markAsCompleted() {
        // Example of domain-specific behavior
        // Implementation could include setting a 'completed' flag, updating status, etc.
        System.out.println("ToDo item marked as completed");
    }

    public void updatePriority(int newPriority) {
        validatePriority(newPriority);
        this.priority = newPriority;
    }

    public void appendToDescription(String additionalInfo) {
        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            this.description += " " + additionalInfo;
        }
    }
}