package org.example.studysearch;

import java.util.List;

public class GeneralSearch implements Search<String> {
    private SearchLog searchLog = new SearchLog("General Search");

    public GeneralSearch() {}

    @Override
    public List<String> search(String text) {
        return handleSearch(text);
    }

    public SearchLog getSearchLog(){
        return searchLog;
    }

    public GeneralSearch(SearchLog searchLog) {
        this.searchLog = searchLog;
    }

    public List<String> handleSearch(String searchTerm) {
        searchLog.handleSearch(searchTerm);
        return null;
    }



}
