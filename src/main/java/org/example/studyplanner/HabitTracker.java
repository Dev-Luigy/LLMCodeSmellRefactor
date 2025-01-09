package org.example.studyplanner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class HabitDateTimeBuilder {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer seconds;
    private Integer dailyHoursDedication;
    private Integer dailyMinutesDedication;

    public HabitDateTimeBuilder setYear(Integer year) {
        this.year = year;
        return this;
    }

    public HabitDateTimeBuilder setMonth(Integer month) {
        this.month = month;
        return this;
    }

    public HabitDateTimeBuilder setDay(Integer day) {
        this.day = day;
        return this;
    }

    public HabitDateTimeBuilder setHour(Integer hour) {
        this.hour = hour;
        return this;
    }

    public HabitDateTimeBuilder setMinute(Integer minute) {
        this.minute = minute;
        return this;
    }

    public HabitDateTimeBuilder setSeconds(Integer seconds) {
        this.seconds = seconds;
        return this;
    }

    public HabitDateTimeBuilder setDailyHoursDedication(Integer dailyHoursDedication) {
        this.dailyHoursDedication = dailyHoursDedication;
        return this;
    }

    public HabitDateTimeBuilder setDailyMinutesDedication(Integer dailyMinutesDedication) {
        this.dailyMinutesDedication = dailyMinutesDedication;
        return this;
    }

    public LocalDateTime buildStartDate() {
        if (year == null) return null;
        return LocalDateTime.of(year, month, day, hour, minute, seconds);
    }

    public LocalTime buildDedicationTime() {
        return LocalTime.of(
                dailyHoursDedication != null ? dailyHoursDedication : 0,
                dailyMinutesDedication != null ? dailyMinutesDedication : 0
        );
    }
}

class HabitDTO {
    private String name;
    private String motivation;
    private HabitDateTimeBuilder dateTimeBuilder;
    private Boolean isConcluded;

    private HabitDTO() {
        this.dateTimeBuilder = new HabitDateTimeBuilder();
    }

    public static class Builder {
        private final HabitDTO habitDTO;

        public Builder() {
            habitDTO = new HabitDTO();
        }

        public Builder withName(String name) {
            habitDTO.name = name;
            return this;
        }

        public Builder withMotivation(String motivation) {
            habitDTO.motivation = motivation;
            return this;
        }

        public Builder withDailyMinutesDedication(Integer dailyMinutesDedication) {
            habitDTO.dateTimeBuilder.setDailyMinutesDedication(dailyMinutesDedication);
            return this;
        }

        public Builder withDailyHoursDedication(Integer dailyHoursDedication) {
            habitDTO.dateTimeBuilder.setDailyHoursDedication(dailyHoursDedication);
            return this;
        }

        public Builder withYear(Integer year) {
            habitDTO.dateTimeBuilder.setYear(year);
            return this;
        }

        public Builder withMonth(Integer month) {
            habitDTO.dateTimeBuilder.setMonth(month);
            return this;
        }

        public Builder withDay(Integer day) {
            habitDTO.dateTimeBuilder.setDay(day);
            return this;
        }

        public Builder withHour(Integer hour) {
            habitDTO.dateTimeBuilder.setHour(hour);
            return this;
        }

        public Builder withMinute(Integer minute) {
            habitDTO.dateTimeBuilder.setMinute(minute);
            return this;
        }

        public Builder withSeconds(Integer seconds) {
            habitDTO.dateTimeBuilder.setSeconds(seconds);
            return this;
        }

        public Builder withIsConcluded(Boolean isConcluded) {
            habitDTO.isConcluded = isConcluded;
            return this;
        }

        public HabitDTO build() {
            return habitDTO;
        }
    }

    public Habit createHabit(int id) {
        LocalDateTime startDate = dateTimeBuilder.buildStartDate();
        LocalTime dedicationTime = dateTimeBuilder.buildDedicationTime();

        if (startDate != null) {
            return new Habit(name, motivation, dedicationTime, id, startDate, isConcluded);
        } else {
            return new Habit(name, motivation, id);
        }
    }

    public String getName() { return name; }
    public String getMotivation() { return motivation; }
    public Boolean getIsConcluded() { return isConcluded; }
}

public class HabitTracker {
    private List<Habit> habits;
    private Map<Integer, List<LocalDateTime>> tracker;
    private Integer nextId;

    private static volatile HabitTracker instance;

    public static HabitTracker getHabitTracker() {
        HabitTracker result = instance;
        if (result == null) {
            synchronized (HabitTracker.class) {
                result = instance;
                if (result == null) {
                    instance = result = new HabitTracker();
                }
            }
        }
        return result;
    }

    private HabitTracker() {
        this.habits = new ArrayList<>();
        this.tracker = new HashMap<>();
        this.nextId = 1;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        for (Habit habit : habits) {
            response.append(habit.toString()).append(", ");
        }
        return "Habits: " + response;
    }

    public Habit getHabitById(Integer id) {
        return this.habits.stream()
                .filter(habit -> Objects.equals(habit.getId(), id))
                .findFirst().orElse(null);
    }

    public List<Habit> getHabits() {
        return this.habits;
    }

    public String formatHabitDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(formatter);
    }

    public List<Integer> getTrackerKeys() {
        return this.tracker.keySet().stream().toList();
    }

    private int addHabit(HabitDTO habitDTO) {
        Objects.requireNonNull(habitDTO, "HabitDTO cannot be null");
        Objects.requireNonNull(habitDTO.getName(), "Habit name cannot be null");
        Objects.requireNonNull(habitDTO.getMotivation(), "Habit motivation cannot be null");

        Habit habit = habitDTO.createHabit(this.nextId);
        this.habits.add(habit);

        int response = nextId;
        this.tracker.put(nextId, new ArrayList<>());
        this.nextId++;
        return response;
    }

    public int addHabit(CompoundHabitInfo habitInfo) {
        return addHabit(habitInfo.toHabitDTO());
    }

    public int handleAddHabitAdapter(List<String> stringProperties, List<Integer> intProperties, boolean isConcluded) {
        CompoundHabitInfo habitInfo = new CompoundHabitInfo.Builder()
                .withName(stringProperties.get(0))
                .withMotivation(stringProperties.get(1))
                .withDailyDedication(intProperties.get(0), intProperties.get(1))
                .withDateTime(
                        intProperties.get(2), intProperties.get(3), intProperties.get(4),
                        intProperties.get(5), intProperties.get(6), intProperties.get(7)
                )
                .withStatus(isConcluded)
                .build();

        return addHabit(habitInfo.toHabitDTO());
    }

    public int addHabit(String name, String motivation) {
        CompoundHabitInfo habitInfo = new CompoundHabitInfo.Builder()
                .withName(name)
                .withMotivation(motivation)
                .build();

        return addHabit(habitInfo.toHabitDTO());
    }

    public void addHabitRecord(Integer id) {
        tracker.get(id).add(LocalDateTime.now());
    }

    public void toggleConcludeHabit(Integer id) {
        habits.stream()
                .filter(habit -> habit.getId().equals(id))
                .findFirst()
                .ifPresent(habit -> habit.setIsConcluded(!habit.getIsConcluded()));
    }

    public void removeHabit(Integer id) {
        this.habits.removeIf(habit -> habit.getId().equals(id));
        this.tracker.remove(id);
    }

    public List<LocalDateTime> getHabitRecords(Integer id) {
        return this.tracker.get(id);
    }

    public List<String> searchInHabits(String search) {
        String searchLower = search.toLowerCase();
        return habits.stream()
                .filter(habit -> habit.getName().toLowerCase().contains(searchLower) ||
                        habit.getMotivation().toLowerCase().contains(searchLower))
                .map(Habit::toString)
                .collect(Collectors.toList());
    }
}