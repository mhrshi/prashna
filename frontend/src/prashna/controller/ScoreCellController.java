package prashna.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ScoreCellController {

    @FXML private Label studentId;
    @FXML private Label score;

    public void setStudentId(String id) {
        studentId.setText(id);
    }

    public void setScore(String marks) {
        score.setText(marks);
    }
}
