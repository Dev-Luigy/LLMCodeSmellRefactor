package org.example.studyregistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudyGoal extends Registry {
    private String goal;
    private List<String> goalRequirements;
    private Boolean isCompleted;
    private LocalDateTime createdDate;
    private Double goalCompletion;
    private StudyObjective studyObjective;
    private StudyPlan studyPlan;
    private String summary;

    public StudyGoal(String name, StudyObjective objective, StudyPlan plan) {
        this.name = name;
        this.studyObjective = objective;
        this.studyPlan = plan;
        goalRequirements = new ArrayList<>();
    }

    public void editActiveCompleted(boolean active, boolean completed) {
        this.isActive = active;
        this.isCompleted = completed;
    }

    private void appendGoalStatus(StringBuilder builder) {
        if (this.isActive) builder.append("Active Goal:\n").append(goal).append("\n\n");
        if (this.isCompleted) builder.append("Completed Goal:\n").append(goal).append("\n\n");
    }

    private void appendRequirements(StringBuilder builder) {
        if (this.goalRequirements != null) {
            builder.append("Requirements:\n");
            this.goalRequirements.forEach(requirement -> builder.append(requirement).append(", "));
        }
    }

    private void appendPlanAndObjective(StringBuilder builder) {
        if (this.studyPlan != null) builder.append("Plan:\n").append(this.studyPlan.toString());
        if (this.studyObjective != null) builder.append("Objective:\n").append(this.studyObjective.toString());
    }

    public String setGoalSummary() {
        StringBuilder summaryBuilder = new StringBuilder().append("Goal Summary:\n\n");
        appendGoalStatus(summaryBuilder);
        appendRequirements(summaryBuilder);
        appendPlanAndObjective(summaryBuilder);
        this.summary = summaryBuilder.toString();
        return this.summary;
    }

    public void addRequirement(String requirement) { this.goalRequirements.add(requirement); }

    public void resetRequirements() { this.goalRequirements.clear(); }

    public boolean isCompleted() { return isCompleted; }

    public void toggleIsCompleted() { this.isCompleted = !this.isCompleted; }

    public LocalDateTime getLimitDate() { return createdDate; }

    public void setLimitDate(LocalDateTime limitDate) { this.createdDate = limitDate; }

    public void addDaysLimitDate(int days) { this.createdDate = this.createdDate.plusDays(days); }

    public void setGoal(String goal) { this.goal = goal; }
}
