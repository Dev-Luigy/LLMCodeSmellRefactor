package org.example.studyregistry;

import java.time.LocalDateTime;
import java.time.Duration;

public class Task extends Registry {
    private String title;
    private String description;
    private String author;
    private LocalDateTime date;
    private boolean completed;

    public Task(String title, String description, String author, LocalDateTime date) {
        validateInputs(title, author, date);
        initializeTask(title, description, author, date);
    }

    private void validateInputs(String title, String author, LocalDateTime date) {
        validateTitle(title);
        validateAuthor(author);
        validateDate(date);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }

    private void validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }

    private void initializeTask(String title, String description, String author, LocalDateTime date) {
        this.title = title;
        this.name = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.completed = false;
    }

    public boolean isOverdue() {
        return !completed && LocalDateTime.now().isAfter(date);
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public void markAsIncomplete() {
        this.completed = false;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Duration timeUntilDue() {
        return Duration.between(LocalDateTime.now(), date);
    }

    public void updateDeadline(LocalDateTime newDate) {
        validateDate(newDate);
        if (newDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("New date cannot be in the past");
        }
        this.date = newDate;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getDeadline() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Task: %s (Due: %s) - %s [%s]",
                title,
                date,
                completed ? "Completed" : "Pending",
                author);
    }
}