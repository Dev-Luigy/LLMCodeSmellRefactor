package org.example.studysearch;

import org.example.studyregistry.StudyMaterial;

import java.util.ArrayList;
import java.util.List;

public class MaterialSearch implements Search<String>{
    private SearchLog searchLog = new SearchLog("Material Search");

    public MaterialSearch() {}

    @Override
    public List<String> search(String text) {
        List<String> response = handleMaterialSearch(text);
        return response;
    }

    private List<String> handleMaterialSearch(String text){
        List<String> results = new ArrayList<>();
        results.addAll(StudyMaterial.getStudyMaterial().searchInMaterials(text));
        this.searchLog.addSearchHistory(text);
        this.searchLog.setNumUsages(this.searchLog.getNumUsages() + 1);
        results.add("Logged in: " + this.searchLog.getLogName());
        return results;
    }

}
