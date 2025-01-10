package org.example.studyplanner;

import java.util.Objects;

public class CompoundHabitInfo {
    private final String name;
    private final String motivation;
    private Integer dailyMinutes;
    private Integer dailyHours;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer seconds;
    private final Boolean isConcluded;

    private CompoundHabitInfo(Builder builder) {
        this.name = Objects.requireNonNull(builder.name, "Name cannot be null");
        this.motivation = Objects.requireNonNull(builder.motivation, "Motivation cannot be null");
        setTimeFields(builder);
        setDateFields(builder);
        this.isConcluded = builder.isConcluded != null ? builder.isConcluded : false;
    }

    private void setTimeFields(Builder builder) {
        this.dailyMinutes = builder.dailyMinutes;
        this.dailyHours = builder.dailyHours;
    }

    private void setDateFields(Builder builder) {
        this.year = builder.year;
        this.month = builder.month;
        this.day = builder.day;
        this.hour = builder.hour;
        this.minute = builder.minute;
        this.seconds = builder.seconds;
    }

    public static class Builder {
        private String name;
        private String motivation;
        private Integer dailyMinutes;
        private Integer dailyHours;
        private Integer year;
        private Integer month;
        private Integer day;
        private Integer hour;
        private Integer minute;
        private Integer seconds;
        private Boolean isConcluded;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMotivation(String motivation) {
            this.motivation = motivation;
            return this;
        }

        public Builder withDailyDedication(Integer minutes, Integer hours) {
            this.dailyMinutes = minutes;
            this.dailyHours = hours;
            return this;
        }

        public Builder withDateTime(Integer year, Integer month, Integer day,
                                    Integer hour, Integer minute, Integer seconds) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.seconds = seconds;
            return this;
        }

        public Builder withStatus(Boolean isConcluded) {
            this.isConcluded = isConcluded;
            return this;
        }

        public CompoundHabitInfo build() {
            return new CompoundHabitInfo(this);
        }
    }
}
