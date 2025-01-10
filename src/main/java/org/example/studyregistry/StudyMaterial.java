package org.example.studyregistry;
import org.example.studymaterial.AudioReference;
import org.example.studymaterial.Reference;
import org.example.studymaterial.TextReference;
import org.example.studymaterial.VideoReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudyMaterial {
    private List<Reference> references = new ArrayList<>();
    private static StudyMaterial studyMaterial;
    private Map<String, Integer> referenceCount;

    private StudyMaterial() {}

    public static StudyMaterial getStudyMaterial() {
        return studyMaterial == null ? studyMaterial = new StudyMaterial() : studyMaterial;
    }

    public void addReference(Reference ref) {
        references.add(ref);
    }

    List<Reference> getReferences() {
        return references;
    }

    public List<Reference> getTypeReference(Reference type) {
        return references.stream()
                .filter(reference -> reference.getClass() == type.getClass())
                .collect(Collectors.toList());
    }

    public void setReferenceCount(Map<String, Integer> referenceCount) {
        this.referenceCount = referenceCount;
    }

    public List<String> searchInMaterials(String text) {
        return references.stream()
                .filter(reference -> getCombinedText(reference)
                        .toLowerCase()
                        .contains(text.toLowerCase()))
                .map(Reference::getTitle)
                .collect(Collectors.toList());
    }

    private String getCombinedText(Reference reference) {
        return (reference.getTitle() != null ? reference.getTitle() : "") +
                (reference.getDescription() != null ? reference.getDescription() : "");
    }

    public Map<String, Integer> getReferenceCountMap() {
        Map<String, Integer> response = initializeCountMap();

        references.forEach(reference -> updateReferenceCount(response, reference));
        setReferenceCount(response);

        return response;
    }

    private Map<String, Integer> initializeCountMap() {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("Audio References", 0);
        countMap.put("Video References", 0);
        countMap.put("Text References", 0);
        return countMap;
    }

    private void updateReferenceCount(Map<String, Integer> countMap, Reference reference) {
        if (reference instanceof AudioReference) {
            incrementCount(countMap, "Audio References");
        } else if (reference instanceof VideoReference && ((VideoReference) reference).handleStreamAvailability()) {
            incrementCount(countMap, "Video References");
        } else if (reference instanceof TextReference && ((TextReference) reference).handleTextAccess()) {
            incrementCount(countMap, "Text References");
        }
    }

    private void incrementCount(Map<String, Integer> map, String key) {
        map.put(key, map.get(key) + 1);
    }
}
