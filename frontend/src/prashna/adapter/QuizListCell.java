package prashna.adapter;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import prashna.model.Quiz;
import prashna.controller.QuizCellController;

import java.io.IOException;

public class QuizListCell extends JFXListCell<Quiz> {

    private QuizCellController controller;
    private Node node;

    public QuizListCell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/quizCell.fxml"));
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
    }

    @Override
    protected void updateItem(Quiz quiz, boolean empty) {
        super.updateItem(quiz, empty);
        if (empty) {
            setGraphic(null);
        } else {
            controller.setQuizName(quiz.getName());
            controller.setBranch(quiz.getBranch());
            controller.setSemester(quiz.getSemester());
            setGraphic(node);
        }
    }
}
