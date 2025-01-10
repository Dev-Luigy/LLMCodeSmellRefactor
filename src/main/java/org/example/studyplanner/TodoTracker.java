package org.example.studyplanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class TodoTracker {
    private final List<ToDo> toDos;
    private final Map<Integer, List<LocalDateTime>> tracker;
    private Integer nextId;
    private static TodoTracker instance;

    private TodoTracker() {
        this.tracker = new HashMap<>();
        this.toDos = new ArrayList<>();
        this.nextId = 1;
    }

    public static TodoTracker getInstance() {
        if (instance == null) {
            instance = new TodoTracker();
        }
        return instance;
    }

    @Override
    public String toString() {
        if (toDos.isEmpty()) {
            return "No ToDos found";
        }
        return buildToDoString();
    }

    private String buildToDoString() {
        StringBuilder str = new StringBuilder();
        for (ToDo toDo : toDos) {
            appendToDoInfo(str, toDo);
        }
        return str.toString();
    }

    private void appendToDoInfo(StringBuilder str, ToDo toDo) {
        str.append(toDo.toString()).append("\n");
        appendTrackerDates(str, toDo.getId());
    }

    private void appendTrackerDates(StringBuilder str, Integer id) {
        List<LocalDateTime> todosDate = this.tracker.get(id);
        if (todosDate == null) {
            str.append("No tracks found\n");
            return;
        }
        appendFormattedDates(str, todosDate);
    }

    private void appendFormattedDates(StringBuilder str, List<LocalDateTime> dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (LocalDateTime ldt : dates) {
            str.append(formatter.format(ldt)).append("\n");
        }
    }

    public void addToDoExecutionTime(Integer id) {
        List<LocalDateTime> et = tracker.computeIfAbsent(id, k -> new ArrayList<>());
        et.add(LocalDateTime.now());
    }

    public List<ToDo> getToDos() {
        return new ArrayList<>(toDos);
    }

    public ToDo getToDoById(Integer id) {
        return toDos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Integer addToDo(String title, String description, Integer priority) {
        ToDo newToDo = new ToDo(nextId, title, description, priority);
        toDos.add(newToDo);
        return nextId++;
    }

    public void removeToDo(Integer id) {
        toDos.removeIf(toDo -> toDo.getId().equals(id));
    }

    public List<ToDo> sortTodosByPriority() {
        return toDos.stream()
                .sorted(Comparator.comparingInt(ToDo::getPriority))
                .collect(Collectors.toList());
    }

    public List<String> searchInTodos(String search) {
        return toDos.stream()
                .filter(todo -> containsSearchTerm(todo, search.toLowerCase()))
                .map(ToDo::toString)
                .collect(Collectors.toList());
    }

    private boolean containsSearchTerm(ToDo todo, String searchTerm) {
        return todo.getTitle().toLowerCase().contains(searchTerm) ||
                todo.getDescription().toLowerCase().contains(searchTerm);
    }
}
