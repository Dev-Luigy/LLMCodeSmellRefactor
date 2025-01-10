package org.example.studyregistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// Class to encapsulate plan details
class PlanDetails {
    private String planName;
    private String objectiveTitle;
    private String objectiveDescription;
    private String materialTopic;
    private String materialFormat;
    private String goal;

    public PlanDetails(String planName, String objectiveTitle, String objectiveDescription, String materialTopic,
                       String materialFormat, String goal) {
        this.planName = planName;
        this.objectiveTitle = objectiveTitle;
        this.objectiveDescription = objectiveDescription;
        this.materialTopic = materialTopic;
        this.materialFormat = materialFormat;
        this.goal = goal;
    }

    // Getters
    public String getPlanName() { return planName; }
    public String getObjectiveTitle() { return objectiveTitle; }
    public String getObjectiveDescription() { return objectiveDescription; }
    public String getMaterialTopic() { return materialTopic; }
    public String getMaterialFormat() { return materialFormat; }
    public String getGoal() { return goal; }

    // Behavior methods
    public String summarizePlan() {
        return String.format("Plan: %s, Objective: %s, Goal: %s", planName, objectiveTitle, goal);
    }

    public String detailedDescription() {
        return String.format("Plan: %s\nObjective: %s\nDescription: %s\nMaterial Topic: %s\nMaterial Format: %s\nGoal: %s",
                planName, objectiveTitle, objectiveDescription, materialTopic, materialFormat, goal);
    }

    // Additional behavior method
    public boolean isGoalAchievable() {
        return goal != null && !goal.isEmpty();
    }
}

// Class to encapsulate reminder details
class ReminderDetails {
    private String reminderTitle;
    private String reminderDescription;

    public ReminderDetails(String reminderTitle, String reminderDescription) {
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
    }

    // Getters
    public String getReminderTitle() { return reminderTitle; }
    public String getReminderDescription() { return reminderDescription; }

    // Behavior methods
    public String summarizeReminder() {
        return String.format("Reminder: %s, Description: %s", reminderTitle, reminderDescription);
    }

    public String detailedDescription() {
        return String.format("Reminder: %s\nDescription: %s", reminderTitle, reminderDescription);
    }

    // Additional behavior method
    public boolean isReminderClear() {
        return reminderTitle != null && !reminderTitle.isEmpty();
    }
}

public class StudyTaskManager {
    private static StudyTaskManager instance;
    private StudyMaterial studyMaterial = StudyMaterial.getStudyMaterial();
    List<Registry> registryList;
    List<String> weekResponsibilities = List.of();

    private StudyTaskManager(){
        this.registryList = new ArrayList<Registry>();
    }

    public static StudyTaskManager getStudyTaskManager(){
        if (instance == null) {
            instance = new StudyTaskManager();
        }
        return instance;
    }

    public List<String> getWeekResponsibilities() {
        return weekResponsibilities;
    }

    public void setUpWeek(WeekPlan weekPlan){
        this.weekResponsibilities = new ArrayList<>();
        this.weekResponsibilities.addAll(Arrays.asList(
                weekPlan.getPlanDetails().getPlanName(),
                weekPlan.getPlanDetails().getObjectiveTitle(),
                weekPlan.getPlanDetails().getObjectiveDescription(),
                weekPlan.getPlanDetails().getMaterialTopic(),
                weekPlan.getPlanDetails().getMaterialFormat(),
                weekPlan.getPlanDetails().getGoal(),
                weekPlan.getReminderDetails().getReminderTitle(),
                weekPlan.getReminderDetails().getReminderDescription(),
                weekPlan.getMainTaskTitle(),
                weekPlan.getMainHabit(),
                weekPlan.getMainCardStudy()
        ));
    }

    public void handleSetUpWeek(List<String> stringProperties){
        PlanDetails planDetails = new PlanDetails(
                stringProperties.get(0), stringProperties.get(1), stringProperties.get(2), stringProperties.get(3),
                stringProperties.get(4), stringProperties.get(5)
        );
        ReminderDetails reminderDetails = new ReminderDetails(
                stringProperties.get(6), stringProperties.get(7)
        );
        WeekPlan weekPlan = new WeekPlan(
                planDetails, reminderDetails, stringProperties.get(8), stringProperties.get(9), stringProperties.get(10)
        );
        setUpWeek(weekPlan);
    }

    public void addRegistry(Registry registry){
        registryList.add(registry);
    }
    public void removeRegistry(Registry registry){
        registryList.remove(registry);
    }
    public List<Registry> getRegistryList(){
        return registryList;
    }

    public List<String> searchInRegistries(String text){
        List<String> response = new ArrayList<>();
        for(Registry registry : registryList){
            String mix = (registry.getName() != null ? registry.getName() : "");
            if (mix.toLowerCase().contains(text.toLowerCase())){
                response.add(registry.getName());
            }
        }
        return response;
    }
}

class WeekPlan {
    private PlanDetails planDetails;
    private ReminderDetails reminderDetails;
    private String mainTaskTitle;
    private String mainHabit;
    private String mainCardStudy;

    public WeekPlan(PlanDetails planDetails, ReminderDetails reminderDetails, String mainTaskTitle, String mainHabit, String mainCardStudy) {
        this.planDetails = planDetails;
        this.reminderDetails = reminderDetails;
        this.mainTaskTitle = mainTaskTitle;
        this.mainHabit = mainHabit;
        this.mainCardStudy = mainCardStudy;
    }

    // Getters
    public PlanDetails getPlanDetails() { return planDetails; }
    public ReminderDetails getReminderDetails() { return reminderDetails; }
    public String getMainTaskTitle() { return mainTaskTitle; }
    public String getMainHabit() { return mainHabit; }
    public String getMainCardStudy() { return mainCardStudy; }

    // Add behavior methods
    public String summarizeWeek() {
        return String.format("Plan: %s, Objective: %s, Main Task: %s",
                planDetails.getPlanName(), planDetails.getObjectiveTitle(), mainTaskTitle);
    }

    public String detailedDescription() {
        return String.format("Plan: %s\nObjective: %s\nDescription: %s\nMaterial Topic: %s\nMaterial Format: %s\nGoal: %s\nReminder: %s\nReminder Description: %s\nMain Task: %s\nMain Habit: %s\nMain Card Study: %s",
                planDetails.getPlanName(), planDetails.getObjectiveTitle(), planDetails.getObjectiveDescription(),
                planDetails.getMaterialTopic(), planDetails.getMaterialFormat(), planDetails.getGoal(),
                reminderDetails.getReminderTitle(), reminderDetails.getReminderDescription(),
                mainTaskTitle, mainHabit, mainCardStudy);
    }

    // Additional behavior method
    public boolean isWeekPlanComplete() {
        return planDetails.isGoalAchievable() && reminderDetails.isReminderClear() &&
                mainTaskTitle != null && !mainTaskTitle.isEmpty() &&
                mainHabit != null && !mainHabit.isEmpty() &&
                mainCardStudy != null && !mainCardStudy.isEmpty();
    }
}