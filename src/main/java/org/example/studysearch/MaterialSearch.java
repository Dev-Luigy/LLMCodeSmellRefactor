package org.example.studysearch;

import java.util.List;

public class MaterialSearch implements Search<String>{


    private SearchLog searchLog = new SearchLog("Material Search");

    public MaterialSearch() {}

    @Override
    public List<String> search(String text) {
        return handleMaterialSearch(text);
    }

    public SearchLog getSearchLog() {
        return searchLog;
    }

    public List<String> handleMaterialSearch(String searchTerm) {
        searchLog.handleMaterialSearch(searchTerm);
        return null;
    }

}
