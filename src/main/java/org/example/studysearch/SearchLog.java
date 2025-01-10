package org.example.studysearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchLog {
    private List<String> searchHistory;
    private Map<String, Integer> searchCount;
    private boolean isLocked;
    private Integer numUsages;
    private String logName;

    public SearchLog(String logName) {
        searchHistory = new ArrayList<>();
        searchCount = new HashMap<>();
        this.logName = logName;
        numUsages = 0;
        isLocked = false;
    }

    // Método para gerenciar busca geral
    public void handleSearch(String searchTerm) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        addSearchHistory(searchTerm);
        searchCount.merge(searchTerm, 1, Integer::sum);
        numUsages++;
    }

    // Método para gerenciar busca de materiais
    public void handleMaterialSearch(String searchTerm) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        String materialSearchTerm = "MATERIAL:" + searchTerm;
        addSearchHistory(materialSearchTerm);
        searchCount.merge(materialSearchTerm, 1, Integer::sum);
        numUsages++;
    }

    // Método para gerenciar busca de registros
    public void handleRegistrySearch(String searchTerm) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        String registrySearchTerm = "REGISTRY:" + searchTerm;
        addSearchHistory(registrySearchTerm);
        searchCount.merge(registrySearchTerm, 1, Integer::sum);
        numUsages++;
    }

    public void addSearchHistory(String searchHistory) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }
        this.searchHistory.add(searchHistory);
    }

    public List<String> getSearchHistory() {
        return new ArrayList<>(searchHistory); // Retorna uma cópia para proteger a lista original
    }

    public void setSearchHistory(List<String> searchHistory) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }
        this.searchHistory = new ArrayList<>(searchHistory); // Cria uma cópia da lista
    }

    public Map<String, Integer> getSearchCount() {
        return new HashMap<>(searchCount); // Retorna uma cópia para proteger o mapa original
    }

    public void setSearchCount(Map<String, Integer> searchCount) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }
        this.searchCount = new HashMap<>(searchCount); // Cria uma cópia do mapa
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
            throw new IllegalStateException("Search log is locked");
        }
        this.numUsages = numUsages;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        if (isLocked) {
            throw new IllegalStateException("Search log is locked");
        }
        this.logName = logName;
    }
}