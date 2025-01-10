package org.example.studysearch;

import org.example.studycards.CardManager;
import org.example.studyplanner.HabitTracker;
import org.example.studyplanner.TodoTracker;
import org.example.studyregistry.StudyTaskManager;

import java.util.ArrayList;
import java.util.List;

public class RegistrySearch implements Search<String> {
    private SearchLog searchLog = new SearchLog("Registry Search");

    public RegistrySearch() {}

    @Override
    public List<String> search(String text) {
        return handleRegistrySearch(text);
    }

    public SearchLog getSearchLog() {
        return searchLog;
    }

    private List<String> handleRegistrySearch(String text){
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        List<String> results = new ArrayList<>();
        results.addAll(CardManager.getCardManager().searchInCards(text));
        results.addAll(HabitTracker.getHabitTracker().searchInHabits(text));
        results.addAll(TodoTracker.getInstance().searchInTodos(text));
        results.addAll(StudyTaskManager.getStudyTaskManager().searchInRegistries(text));

        return searchLog.handleSearchAndGetResults(text, results);
    }
}
