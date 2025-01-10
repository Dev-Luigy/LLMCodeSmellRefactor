package org.example.studyregistry;

import java.time.LocalDateTime;
import java.util.Objects;

class TextualInformation {
    private final String title;
    private final String description;
    private final String topic;
    private final String objectiveInOneLine;
    private final String objectiveFullDescription;
    private final String motivation;

    public TextualInformation(String title, String description, String topic,
                              String objectiveInOneLine, String objectiveFullDescription,
                              String motivation) {
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.objectiveInOneLine = objectiveInOneLine;
        this.objectiveFullDescription = objectiveFullDescription;
        this.motivation = motivation;
    }

    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
                description != null && !description.trim().isEmpty();
    }

    public String briefSummary() {
        return String.format("Title: %s, Description: %s", title, description);
    }

    public String detailedSummary() {
        return String.format("Title: %s\nDescription: %s\nTopic: %s\nObjective: %s\nFull Description: %s\nMotivation: %s",
                title, description, topic, objectiveInOneLine, objectiveFullDescription, motivation);
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTopic() { return topic; }
    public String getObjectiveInOneLine() { return objectiveInOneLine; }
    public String getObjectiveFullDescription() { return objectiveFullDescription; }
    public String getMotivation() { return motivation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextualInformation that = (TextualInformation) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(objectiveInOneLine, that.objectiveInOneLine) &&
                Objects.equals(objectiveFullDescription, that.objectiveFullDescription) &&
                Objects.equals(motivation, that.motivation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, topic, objectiveInOneLine, objectiveFullDescription, motivation);
    }

    @Override
    public String toString() {
        return "TextualInformation{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", topic='" + topic + '\'' +
                ", objectiveInOneLine='" + objectiveInOneLine + '\'' +
                ", objectiveFullDescription='" + objectiveFullDescription + '\'' +
                ", motivation='" + motivation + '\'' +
                '}';
    }
}

// Classe para gerenciar informações temporais
class TimeInformation {
    private final Integer practicedDays;
    private final int day;
    private final int month;
    private final int year;
    private final Double duration;

    public TimeInformation(Integer practicedDays, int day, int month, int year, Double duration) {
        this.practicedDays = practicedDays;
        this.day = day;
        this.month = month;
        this.year = year;
        this.duration = duration;
        validate();
    }

    private void validate() {
        if (duration != null && duration <= 0) {
            throw new IllegalStateException("Duration must be positive");
        }
        if (practicedDays != null && practicedDays < 0) {
            throw new IllegalStateException("Practiced days cannot be negative");
        }
        validateDate();
    }

    private void validateDate() {
        if (year != 0) {
            if (month < 1 || month > 12) {
                throw new IllegalStateException("Month must be between 1 and 12");
            }
            if (day < 1 || day > 31) {
                throw new IllegalStateException("Day must be between 1 and 31");
            }
        }
    }

    public LocalDateTime calculateStartDate() {
        return year == 0 ? null : LocalDateTime.of(year, month, day, 0, 0);
    }

    public long calculateDaysRemaining() {
        if (duration == null || practicedDays == null) {
            return 0;
        }
        return Math.max(0, duration.longValue() - practicedDays);
    }

    public boolean isCompleted() {
        return practicedDays != null && duration != null && practicedDays >= duration;
    }

    public double calculateCompletionPercentage() {
        if (practicedDays == null || duration == null || duration == 0) {
            return 0.0;
        }
        return Math.min(100.0, (practicedDays * 100.0) / duration);
    }

    public boolean isOverdue(LocalDateTime currentDate) {
        LocalDateTime startDate = calculateStartDate();
        if (startDate == null || duration == null) {
            return false;
        }
        return currentDate.isAfter(startDate.plusDays(duration.longValue()));
    }

    // Getters
    public Integer getPracticedDays() { return practicedDays; }
    public Double getDuration() { return duration; }
}

// Classe para gerar relatórios de progresso
class ProgressReportGenerator {
    private final TextualInformation textualInfo;
    private final TimeInformation timeInfo;

    public ProgressReportGenerator(TextualInformation textualInfo, TimeInformation timeInfo) {
        this.textualInfo = textualInfo;
        this.timeInfo = timeInfo;
    }

    public String generateReport(LocalDateTime currentDate) {
        StringBuilder report = new StringBuilder();
        appendHeader(report);
        appendBasicInfo(report);
        appendProgressInfo(report);
        appendTimeInfo(report, currentDate);
        return report.toString();
    }

    private void appendHeader(StringBuilder report) {
        report.append("Study Objective Progress Report\n");
    }

    private void appendBasicInfo(StringBuilder report) {
        report.append("Title: ").append(textualInfo.getTitle()).append("\n");
        report.append("Topic: ").append(textualInfo.getTopic() != null ? textualInfo.getTopic() : "Not specified").append("\n");
    }

    private void appendProgressInfo(StringBuilder report) {
        report.append("Completion: ")
                .append(String.format("%.1f%%", timeInfo.calculateCompletionPercentage()))
                .append("\n");
    }

    private void appendTimeInfo(StringBuilder report, LocalDateTime currentDate) {
        LocalDateTime startDate = timeInfo.calculateStartDate();
        if (startDate != null) {
            long daysSinceStart = calculateDaysBetween(startDate, currentDate);
            report.append("Days since start: ").append(daysSinceStart).append("\n");
            report.append("Days remaining: ").append(timeInfo.calculateDaysRemaining()).append("\n");
        }
    }

    private long calculateDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime start = startDate.toLocalDate().atStartOfDay();
        LocalDateTime end = endDate.toLocalDate().atStartOfDay();

        long days = 0;
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            days++;
            current = current.plusDays(1);
        }

        return days;
    }
}

// Classe que contém as informações do objetivo de estudo
class StudyObjectiveInfo {
    private final TextualInformation textualInfo;
    private final TimeInformation timeInfo;
    private final ProgressReportGenerator reportGenerator;
    private final Registry registry;

    private StudyObjectiveInfo(Builder builder) {
        this.textualInfo = new TextualInformation(
                builder.title, builder.description, builder.topic,
                builder.objectiveInOneLine, builder.objectiveFullDescription,
                builder.motivation
        );

        this.timeInfo = new TimeInformation(
                builder.practicedDays, builder.day,
                builder.month, builder.year,
                builder.duration
        );

        this.reportGenerator = new ProgressReportGenerator(textualInfo, timeInfo);

        this.registry = new Registry() {};
        this.registry.id = builder.id;
        this.registry.name = builder.name;
        this.registry.priority = builder.priority;
        this.registry.isActive = builder.isActive;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer priority;
        private boolean isActive;
        private String title;
        private String description;
        private String topic;
        private String objectiveInOneLine;
        private String objectiveFullDescription;
        private String motivation;
        private Integer practicedDays;
        private int day;
        private int month;
        private int year;
        private Double duration;

        public Builder withRegistry(Integer id, String name, Integer priority, boolean isActive) {
            this.id = id;
            this.name = name;
            this.priority = priority;
            this.isActive = isActive;
            return this;
        }

        public Builder withTextualInfo(String title, String description, String topic,
                                       String objectiveInOneLine, String objectiveFullDescription,
                                       String motivation) {
            this.title = title;
            this.description = description;
            this.topic = topic;
            this.objectiveInOneLine = objectiveInOneLine;
            this.objectiveFullDescription = objectiveFullDescription;
            this.motivation = motivation;
            return this;
        }

        public Builder withTimeInfo(Integer practicedDays, int day, int month, int year, Double duration) {
            this.practicedDays = practicedDays;
            this.day = day;
            this.month = month;
            this.year = year;
            this.duration = duration;
            return this;
        }

        public StudyObjectiveInfo build() {
            return new StudyObjectiveInfo(this);
        }
    }

    public boolean isValid() {
        return textualInfo.isValid();
    }

    public boolean isInProgress() {
        return registry.isActive && !timeInfo.isCompleted();
    }

    public String generateProgressReport(LocalDateTime currentDate) {
        return reportGenerator.generateReport(currentDate);
    }

    // Getters delegados para as classes específicas
    public Integer getId() { return registry.id; }
    public String getName() { return registry.name; }
    public Integer getPriority() { return registry.priority; }
    public boolean isActive() { return registry.isActive; }
    public String getTitle() { return textualInfo.getTitle(); }
    public String getDescription() { return textualInfo.getDescription(); }
    public String getTopic() { return textualInfo.getTopic(); }
    public String getObjectiveInOneLine() { return textualInfo.getObjectiveInOneLine(); }
    public String getObjectiveFullDescription() { return textualInfo.getObjectiveFullDescription(); }
    public String getMotivation() { return textualInfo.getMotivation(); }
    public Integer getPracticedDays() { return timeInfo.getPracticedDays(); }
    public Double getDuration() { return timeInfo.getDuration(); }

    // Métodos delegados para TimeInformation
    public LocalDateTime calculateStartDate() { return timeInfo.calculateStartDate(); }
    public boolean isCompleted() { return timeInfo.isCompleted(); }
    public double calculateCompletionPercentage() { return timeInfo.calculateCompletionPercentage(); }
    public boolean isOverdue(LocalDateTime currentDate) { return timeInfo.isOverdue(currentDate); }
}

// Classe principal que gerencia objetivos de estudo
public class StudyObjective extends Registry {
    private final StudyObjectiveInfo info;
    private LocalDateTime startDate;

    public StudyObjective(String title, String description) {
        this.info = new StudyObjectiveInfo.Builder()
                .withTextualInfo(title, description, null, null, null, null)
                .withTimeInfo(0, 0, 0, 0, 0.0)
                .build();
        this.name = title;
    }

    public void handleSetObjective(StudyObjectiveInfo newInfo) {
        if (!newInfo.isValid()) {
            throw new IllegalArgumentException("Invalid study objective information");
        }
        updateRegistry(newInfo);
        updateDates(newInfo);
    }

    private void updateRegistry(StudyObjectiveInfo newInfo) {
        this.id = newInfo.getId();
        this.name = newInfo.getName();
        this.priority = newInfo.getPriority();
        this.isActive = newInfo.isActive();
    }

    private void updateDates(StudyObjectiveInfo newInfo) {
        if (newInfo.getPracticedDays() != null && newInfo.getDuration() != null) {
            this.startDate = newInfo.calculateStartDate();
        }
    }

    public boolean isCompleted() {
        return info.isCompleted();
    }

    public double calculateProgress() {
        return info.calculateCompletionPercentage();
    }

    public boolean isOverdue() {
        return info.isOverdue(LocalDateTime.now());
    }

    // Getters
    public String getTitle() { return info.getTitle(); }
    public String getDescription() { return info.getDescription(); }
    public String getTopic() { return info.getTopic(); }
    public Integer getPracticedDays() { return info.getPracticedDays(); }
    public LocalDateTime getStartDate() { return startDate; }
    public Double getDuration() { return info.getDuration(); }
    public String getObjectiveInOneLine() { return info.getObjectiveInOneLine(); }
    public String getObjectiveFullDescription() { return info.getObjectiveFullDescription(); }
    public String getMotivation() { return info.getMotivation(); }

    @Override
    public String toString() {
        return String.format("StudyObjective [title:%s, description:%s%s%s%s%s%s%s]",
                info.getTitle(),
                info.getDescription(),
                formatOptionalField("topic", info.getTopic()),
                formatOptionalField("practicedDays", info.getPracticedDays()),
                formatOptionalField("duration", info.getDuration()),
                formatOptionalField("objective summary", info.getObjectiveInOneLine()),
                formatOptionalField("objective full description", info.getObjectiveFullDescription()),
                formatOptionalField("motivation", info.getMotivation())
        );
    }

    private String formatOptionalField(String fieldName, Object value) {
        return value != null ? String.format(", %s:%s", fieldName, value) : "";
    }
}