package org.example.studysearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class SearchLog {
    private final List<String> searchHistory;
    private final Map<String, Integer> searchCount;
    private boolean isLocked;
    private Integer numUsages;
    private String logName;

    public SearchLog(String logName) {
        this.searchHistory = new ArrayList<>();
        this.searchCount = new HashMap<>();
        this.logName = logName;
        this.numUsages = 0;
        this.isLocked = false;
    }

    // Mantido para compatibilidade com código existente
    public void addSearchHistory(String searchTerm) {
        if (isLocked) {
            throw new IllegalStateException("Cannot add search to a locked log");
        }
        searchHistory.add(searchTerm);
        searchCount.merge(searchTerm, 1, Integer::sum);
    }

    public List<String> getSearchHistory() {
        return new ArrayList<>(searchHistory); // Retorna uma cópia para evitar modificação direta
    }

    // Mantido para compatibilidade
    public void setSearchHistory(List<String> searchHistory) {
        if (isLocked) {
            throw new IllegalStateException("Cannot modify a locked log");
        }
        this.searchHistory.clear();
        if (searchHistory != null) {
            this.searchHistory.addAll(searchHistory);
        }
    }

    public Map<String, Integer> getSearchCount() {
        return new HashMap<>(searchCount); // Retorna uma cópia para evitar modificação direta
    }

    // Mantido para compatibilidade
    public void setSearchCount(Map<String, Integer> searchCount) {
        if (isLocked) {
            throw new IllegalStateException("Cannot modify a locked log");
        }
        this.searchCount.clear();
        if (searchCount != null) {
            this.searchCount.putAll(searchCount);
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Integer getNumUsages() {
        return numUsages;
    }

    public void setNumUsages(Integer numUsages) {
        if (isLocked) {
            throw new IllegalStateException("Cannot modify a locked log");
        }
        this.numUsages = numUsages;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        if (isLocked) {
            throw new IllegalStateException("Cannot modify a locked log");
        }
        this.logName = logName;
    }

    // Novos métodos úteis que podem ser usados em futuras implementações
    public void clearHistory() {
        if (isLocked) {
            throw new IllegalStateException("Cannot clear history of a locked log");
        }
        searchHistory.clear();
        searchCount.clear();
        numUsages = 0;
    }

    public int getSearchFrequency(String searchTerm) {
        return searchCount.getOrDefault(searchTerm, 0);
    }
}