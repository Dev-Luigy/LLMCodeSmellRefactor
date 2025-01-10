package org.example.studycards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeitnerSystem extends StudyMethod {
    private List<Box> boxes = null;

    public LeitnerSystem(String methodName) {
        super(methodName);
        initializeBoxes();
    }

    private void initializeBoxes() {
        boxes = new ArrayList<>(Arrays.asList(
                new Box(), new Box(), new Box(), new Box(), new Box()
        ));
    }

    @Override
    public String getMethodName() {
        return this.methodName;
    }

    @Override
    void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        buildBoxesString(response);
        return response.toString();
    }

    private void buildBoxesString(StringBuilder response) {
        for (int i = 0; i < boxes.size(); i++) {
            response.append("Box ")
                    .append(i)
                    .append(": ")
                    .append(boxes.get(i).toString())
                    .append("\n");
        }
    }

    public void clearBoxes() {
        boxes.clear();
        initializeBoxes();
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public String getRandomCard(List<Box> otherBoxes) {
        if (otherBoxes == null || otherBoxes.isEmpty()) {
            return null;
        }

        Integer randomCardId = getRandomCardFromBoxes(otherBoxes);
        if (randomCardId == null) {
            return "No card found";
        }

        return formatCardResponse(randomCardId);
    }

    private Integer getRandomCardFromBoxes(List<Box> otherBoxes) {
        Box allBoxes = new Box();
        for (Box box : otherBoxes) {
            allBoxes.addCards(box.getCards());
        }
        return allBoxes.getRandomCard();
    }

    private String formatCardResponse(Integer cardId) {
        CardManager manager = CardManager.getCardManager();
        Card card = manager.getCard(cardId);
        return String.format("[%d] The random question was: %s | The answer is: %s",
                cardId, card.getQuestion(), card.getAnswer());
    }

    public void addCardToBox(Integer id, Integer boxId) {
        boxes.get(boxId).addCard(id);
    }

    public void removeCardFromBox(Integer id, Integer boxId) {
        boxes.get(boxId).removeCard(id);
    }

    public Card takeCardFromBox(Integer boxId) {
        Integer cardId = boxes.get(boxId).getRandomCard();
        return this.cardManager.getCard(cardId);
    }

    public void boxIdValidation(Integer boxId) throws Exception {
        if (isInvalidBoxId(boxId)) {
            throw new Exception("Invalid box ID");
        }
    }

    private boolean isInvalidBoxId(Integer boxId) {
        return boxId == null || boxId > (boxes.size() - 1) || boxId <= 0;
    }

    public void upgradeCard(Integer cardId, Integer boxId) throws Exception {
        moveCard(cardId, boxId, true);
    }

    public void downgradeCard(Integer cardId, Integer boxId) throws Exception {
        moveCard(cardId, boxId, false);
    }

    private void moveCard(Integer cardId, Integer boxId, boolean isUpgrade) throws Exception {
        boxIdValidation(boxId);
        Box refBox = boxes.get(boxId);

        if (refBox.hasCard(cardId)) {
            throw new Exception("No card Found");
        }

        refBox.removeCard(cardId);
        int newBoxId = isUpgrade ?
                Math.min(boxId + 1, 4) :
                Math.max(boxId - 1, 0);
        boxes.get(newBoxId).addCard(cardId);
    }
}
