package prashna.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QuizCellController {

    @FXML private Label quizName;
    @FXML private Label branch;
    @FXML private Label semester;

    public void setQuizName(String name) {
        quizName.setText(name);
    }

    public void setBranch(String branchName) {
        branch.setText("Branch: " + branchName);
    }

    public void setSemester(String sem) {
        semester.setText("Semester: " + sem);
    }
}
