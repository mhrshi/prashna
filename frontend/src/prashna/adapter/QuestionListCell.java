package prashna.adapter;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import prashna.model.Question;
import prashna.controller.QuestionCellController;

import java.io.IOException;

public class QuestionListCell extends JFXListCell<Question> {

    private QuestionCellController controller;
    private Node node;

    public QuestionListCell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/questionCell.fxml"));
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
    }

    @Override
    protected void updateItem(Question question, boolean empty) {
        super.updateItem(question, empty);
        if (empty) {
            setGraphic(null);
        } else {
            controller.setQuestion(question.getQuestion());
            controller.setAlpha(question.getAlpha());
            controller.setBeta(question.getBeta());
            controller.setGamma(question.getGamma());
            controller.setDelta(question.getDelta());
            controller.setQuestionNumber(getIndex() + 1);
            setGraphic(node);
        }
    }
}
