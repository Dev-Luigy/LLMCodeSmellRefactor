package org.example.studyplanner;

import java.text.MessageFormat;
import java.util.Objects;

public class ToDo implements PlannerMaterial, Comparable<ToDo> {
    private Integer id;
    private String title;
    private String description;
    private int priority;

    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 5;
    private static final int HIGH_PRIORITY_THRESHOLD = 4;

    public ToDo(Integer id, String title, String description, int priority) {
        validateId(id);
        validateTitle(title);
        validateDescription(description);
        validatePriority(priority);

        this.id = id;
        this.title = title.trim();
        this.description = description.trim();
        this.priority = priority;
    }

    // Comportamentos de validação
    private void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
    }

    private void validatePriority(int priority) {
        if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new IllegalArgumentException(
                    String.format("Priority must be between %d and %d", MIN_PRIORITY, MAX_PRIORITY)
            );
        }
    }

    // Comportamentos de negócio
    public boolean isHighPriority() {
        return priority >= HIGH_PRIORITY_THRESHOLD;
    }

    public boolean hasSamePriority(ToDo other) {
        return this.priority == other.priority;
    }

    public boolean isMoreImportantThan(ToDo other) {
        return this.priority > other.priority;
    }

    public void escalatePriority() {
        if (priority < MAX_PRIORITY) {
            priority++;
        }
    }

    public void deescalatePriority() {
        if (priority > MIN_PRIORITY) {
            priority--;
        }
    }

    public boolean containsText(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return false;
        }
        String search = searchText.toLowerCase().trim();
        return title.toLowerCase().contains(search) ||
                description.toLowerCase().contains(search);
    }

    // Implementação da interface Comparable
    @Override
    public int compareTo(ToDo other) {
        // Ordena primeiro por prioridade (maior primeiro)
        int priorityCompare = Integer.compare(other.priority, this.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        // Se prioridades iguais, ordena por ID
        return Integer.compare(this.id, other.id);
    }

    // Sobrescrita de equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo toDo = (ToDo) o;
        return id.equals(toDo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[(Priority:{0}) ToDo {1}: {2}, {3}]",
                priority, id, title, description);
    }

    // Getters e Setters com validação
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        validateId(id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        validateDescription(description);
        this.description = description.trim();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        validatePriority(priority);
        this.priority = priority;
    }
}