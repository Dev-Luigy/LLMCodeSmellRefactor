package org.example.studyplanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class TodoTracker {
    private List<ToDo> toDos = new ArrayList<>();
    private Map<Integer, List<LocalDateTime>> tracker;
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
        StringBuilder str = new StringBuilder();
        for (ToDo toDo : toDos) {
            String todoInfo = toDo.toString();
            str.append(todoInfo);
            str.append("\n");
            Integer id = toDo.getId();
            List<LocalDateTime> todosDate = this.tracker.get(id);
            if(todosDate == null){
                str.append("No tracks found\n");
            }else{
                for (LocalDateTime ldt : todosDate) {
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    String formattedDate = formatter.format(ldt);
                    str.append(formattedDate);
                    str.append("\n");
                }
            }
        }
        String response = str.toString();
        if(response.isEmpty()){
            return "No ToDos found";
        }
        return response;
    }

    public void addToDoExecutionTime(Integer id){
        List<LocalDateTime> et = tracker.computeIfAbsent(id, k -> new ArrayList<>());
        LocalDateTime now = LocalDateTime.now();
        et.add(now);
    }

    public List<ToDo> getToDos() {
        return toDos;
    }

    public ToDo getToDoById(Integer id) {
        for (ToDo toDo : toDos) {
            if (toDo.getId() == id) {
                return toDo;
            }
        }
        return null;
    }

    public Integer addToDo(String title, String description, Integer priority) {
        ToDo toAdd = new ToDo(nextId, title, description, priority);
        nextId++;
        this.toDos.add(toAdd);
        return toAdd.getId();
    }

    public void removeToDo(Integer id) {
        toDos.removeIf(toDo -> toDo.getId() == id);
    }

    public List<ToDo> sortTodosByPriority() {
        List<ToDo> sortedToDos = new ArrayList<>(toDos);
        sortedToDos.sort(Comparator.comparingInt(ToDo::getPriority));
        return sortedToDos;
    }

    public List<String> searchInTodos(String search) {
        List<String> todos = new ArrayList<>();
        for (ToDo toDo : toDos) {
            if (toDo.getTitle().toLowerCase().contains(search.toLowerCase()) || toDo.getDescription().toLowerCase().contains(search.toLowerCase())) {
                todos.add(toDo.toString());
            }
        }
        return todos;
    }


}