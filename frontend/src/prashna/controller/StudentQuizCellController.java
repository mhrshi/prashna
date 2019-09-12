package prashna.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentQuizCellController {

    @FXML private Label quizName;
    @FXML private Label topic;
    @FXML private Label course;

    public void setQuizName(String name) {
        quizName.setText(name);
    }

    public void setTopic(String topicName) {
        topic.setText("Topic: " + topicName);
    }

    public void setCourse(String courseName) {
        course.setText("Course: " + courseName);
    }
}
