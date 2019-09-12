package prashna.adapter;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import prashna.model.StudentQuiz;
import prashna.controller.StudentQuizCellController;

import java.io.IOException;

public class StudentQuizListCell extends JFXListCell<StudentQuiz> {

    private StudentQuizCellController controller;
    private Node node;

    public StudentQuizListCell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/studentQuizCell.fxml"));
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
    }

    @Override
    protected void updateItem(StudentQuiz quiz, boolean empty) {
        super.updateItem(quiz, empty);
        if (empty) {
            setGraphic(null);
        } else {
            controller.setQuizName(quiz.getName());
            controller.setTopic(quiz.getTopic());
            controller.setCourse(quiz.getCourse());
            setGraphic(node);
        }
    }
}
