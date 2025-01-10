package org.example.studyplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KanbanView {
    public enum State {
        TODO, DOING, DONE;
    }

    HabitTracker habitTracker = null;
    TodoTracker todoTracker = null;
    Map<State, List<PlannerMaterial>> kanban = null;

    public KanbanView(HabitTracker habitTracker, TodoTracker todoTracker) {
        this.habitTracker = habitTracker;
        this.todoTracker = todoTracker;
        this.kanban = new HashMap<>();
        this.kanban.put(State.TODO, new ArrayList<>());
        this.kanban.put(State.DOING, new ArrayList<>());
        this.kanban.put(State.DONE, new ArrayList<>());
    }

    public List<PlannerMaterial> getKanbanByState(State state) {
        return kanban.get(state);
    }

    public void addHabitToKanban(State state, Integer id) throws Exception {
        try {
            Habit toAdd = this.habitTracker.getHabitById(id);
            validateMaterial(toAdd, "Habit", id);
            kanban.get(state).add(toAdd);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void addToDoToKanban(State state, Integer id) throws Exception {
        try {
            ToDo toAdd = this.todoTracker.getToDoById(id);
            validateMaterial(toAdd, "ToDo", id);
            kanban.get(state).add(toAdd);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void removeHabitFromKanban(State state, Integer id) throws Exception {
        try {
            Habit toRemove = this.habitTracker.getHabitById(id);
            validateMaterial(toRemove, "habit", id);
            kanban.get(state).remove(toRemove);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void removeToDoFromKanban(State state, Integer id) throws Exception {
        try {
            ToDo toRemove = this.todoTracker.getToDoById(id);
            validateMaterial(toRemove, "todo", id);
            kanban.get(state).remove(toRemove);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void validateMaterial(PlannerMaterial material, String type, Integer id) throws Exception {
        if (material == null) {
            throw new Exception(type + " not found with id: " + id);
        }
    }

    public String kanbanView() throws Exception {
        try {
            validateKanban();
            return buildKanbanView();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String buildKanbanView() {
        StringBuilder sb = new StringBuilder();
        appendKanbanHeader(sb);
        appendToDoSection(sb);
        appendDoingSection(sb);
        appendDoneSection(sb);
        sb.append("]");
        return sb.toString();
    }

    private void appendKanbanHeader(StringBuilder sb) {
        sb.append("[ Material ToDo: ")
                .append(System.lineSeparator());
    }

    private void appendToDoSection(StringBuilder sb) {
        appendStateSection(sb, State.TODO);
        sb.append(System.lineSeparator());
    }

    private void appendDoingSection(StringBuilder sb) {
        sb.append("Material in progress:")
                .append(System.lineSeparator());
        appendStateSection(sb, State.DOING);
        sb.append(System.lineSeparator());
    }

    private void appendDoneSection(StringBuilder sb) {
        sb.append("Material completed:")
                .append(System.lineSeparator());
        appendStateSection(sb, State.DONE);
    }

    private void appendStateSection(StringBuilder sb, State state) {
        List<PlannerMaterial> materials = kanban.get(state);
        if (materials.isEmpty()) {
            sb.append("No material found");
        } else {
            appendMaterials(sb, materials);
        }
    }

    private void appendMaterials(StringBuilder sb, List<PlannerMaterial> materials) {
        for (PlannerMaterial material : materials) {
            sb.append(", ").append(material.toString());
        }
    }

    private void validateKanban() throws Exception {
        if (kanban.isEmpty()) {
            throw new Exception("No material found");
        }
    }
}
