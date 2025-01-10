package org.example.studyregistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Group parameters related to the objective details
class ObjectiveDetails {
    private String mainObjectiveTitle;
    private String mainGoalTitle;
    private String mainMaterialTopic;
    private String mainTask;

    public ObjectiveDetails(String mainObjectiveTitle, String mainGoalTitle, String mainMaterialTopic, String mainTask) {
        this.mainObjectiveTitle = mainObjectiveTitle;
        this.mainGoalTitle = mainGoalTitle;
        this.mainMaterialTopic = mainMaterialTopic;
        this.mainTask = mainTask;
    }

    // Getters
    public String getMainObjectiveTitle() { return mainObjectiveTitle; }
    public String getMainGoalTitle() { return mainGoalTitle; }
    public String getMainMaterialTopic() { return mainMaterialTopic; }
    public String getMainTask() { return mainTask; }

    // Add behavior methods
    public String summarizeObjective() {
        return String.format("Objective: %s, Goal: %s", mainObjectiveTitle, mainGoalTitle);
    }

    public String detailedDescription() {
        return String.format("Objective: %s\nGoal: %s\nMaterial Topic: %s\nTask: %s",
                mainObjectiveTitle, mainGoalTitle, mainMaterialTopic, mainTask);
    }
}

// Main StepDetails class
class StepDetails {
    private String firstStep;
    private String resetStudyMechanism;
    private String consistentStep;
    private String seasonalSteps;
    private String basicSteps;
    private Integer numberOfSteps;
    private boolean isImportant;
    private ObjectiveDetails objectiveDetails;

    public StepDetails(String firstStep, String resetStudyMechanism, String consistentStep, String seasonalSteps,
                       String basicSteps, Integer numberOfSteps, boolean isImportant,
                       ObjectiveDetails objectiveDetails) {
        this.firstStep = firstStep;
        this.resetStudyMechanism = resetStudyMechanism;
        this.consistentStep = consistentStep;
        this.seasonalSteps = seasonalSteps;
        this.basicSteps = basicSteps;
        this.numberOfSteps = numberOfSteps;
        this.isImportant = isImportant;
        this.objectiveDetails = objectiveDetails;
    }

    // Getters
    public String getFirstStep() { return firstStep; }
    public String getResetStudyMechanism() { return resetStudyMechanism; }
    public String getConsistentStep() { return consistentStep; }
    public String getSeasonalSteps() { return seasonalSteps; }
    public String getBasicSteps() { return basicSteps; }
    public Integer getNumberOfSteps() { return numberOfSteps; }
    public boolean isImportant() { return isImportant; }
    public ObjectiveDetails getObjectiveDetails() { return objectiveDetails; }

    // Behavior methods
    public String summarizeSteps() {
        return String.format("First Step: %s, Consistent Step: %s, Seasonal Steps: %s", firstStep, consistentStep, seasonalSteps);
    }

    public String detailedSteps() {
        return String.format("First Step: %s\nReset Mechanism: %s\nConsistent Step: %s\nSeasonal Steps: %s\nBasic Steps: %s\nNumber of Steps: %d\nImportant: %b",
                firstStep, resetStudyMechanism, consistentStep, seasonalSteps, basicSteps, numberOfSteps, isImportant);
    }

    public boolean validateSteps() {
        return firstStep != null && !firstStep.isEmpty() &&
                consistentStep != null && !consistentStep.isEmpty() &&
                numberOfSteps != null && numberOfSteps > 0;
    }

    public String formatForDisplay() {
        return String.format("Step Details:\n- %s\n- %s\n- %s\n- %s\n- %s\n- Number of Steps: %d\n- Important: %b\n%s",
                firstStep, resetStudyMechanism, consistentStep, seasonalSteps, basicSteps, numberOfSteps, isImportant, objectiveDetails.detailedDescription());
    }
}

class DateRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
}

public class StudyPlan extends Registry {
    private StudyObjective objective;
    private List<String> steps;

    public StudyPlan(String planName, StudyObjective objective, List<StudyMaterial> materials) {
        this.name = planName;
        this.objective = objective;
        this.steps = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Plan: " + name + ",\nObjective: " + objective.getDescription() + ",\nSteps: " + String.join(", ", steps);
    }

    public List<String> getSteps() {
        return steps;
    }

    public StudyObjective getObjective() {
        return objective;
    }

    public void assignObjective(StudyObjective objective) {
        this.objective = objective;
    }

    public void addSingleStep(String toAdd) {
        steps.add(toAdd);
    }

    public void assignSteps(StepDetails stepDetails, DateRange dateRange) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        if (!stepDetails.validateSteps()) {
            throw new IllegalArgumentException("Invalid step details");
        }

        this.steps = new ArrayList<>(Arrays.asList(
                stepDetails.getFirstStep(),
                stepDetails.getResetStudyMechanism(),
                stepDetails.getConsistentStep(),
                stepDetails.getSeasonalSteps(),
                stepDetails.getBasicSteps(),
                "Number of steps: " + stepDetails.getNumberOfSteps(),
                "Is it important to you? " + stepDetails.isImportant(),
                dateRange.getStartDate().format(formatter),
                dateRange.getEndDate().format(formatter),
                stepDetails.getObjectiveDetails().getMainObjectiveTitle(),
                stepDetails.getObjectiveDetails().getMainGoalTitle(),
                stepDetails.getObjectiveDetails().getMainMaterialTopic(),
                stepDetails.getObjectiveDetails().getMainTask()
        ));
    }

    public void handleAssignSteps(List<String> stringProperties, Integer numberOfSteps, boolean isImportant, LocalDateTime startDate, LocalDateTime endDate) {
        ObjectiveDetails objectiveDetails = new ObjectiveDetails(
                stringProperties.get(5), stringProperties.get(6), stringProperties.get(7), stringProperties.get(8));
        StepDetails stepDetails = new StepDetails(
                stringProperties.get(0), stringProperties.get(1), stringProperties.get(2), stringProperties.get(3),
                stringProperties.get(4), numberOfSteps, isImportant, objectiveDetails);
        DateRange dateRange = new DateRange(startDate, endDate);
        assignSteps(stepDetails, dateRange);
    }
}