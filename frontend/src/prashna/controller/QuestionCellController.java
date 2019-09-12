package prashna.controller;

import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import prashna.core.Store;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionCellController implements Initializable {

    @FXML private Label question;
    @FXML private JFXRadioButton alpha;
    @FXML private JFXRadioButton beta;
    @FXML private JFXRadioButton gamma;
    @FXML private JFXRadioButton delta;
    private int questionNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        alpha.setToggleGroup(group);
        beta.setToggleGroup(group);
        gamma.setToggleGroup(group);
        delta.setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            Store store = Store.get();
            String option = "";
            if (newVal == alpha) {
                option = "alpha";
            } else if (newVal == beta) {
                option = "beta";
            } else if (newVal == gamma) {
                option = "gamma";
            } else {
                option = "delta";
            }
            store.saveAnswer(questionNumber,
                             option);
        });
    }

    public void setQuestion(String questionText) {
        question.setText(questionText);
    }

    public void setAlpha(String alphaText) {
        alpha.setText(alphaText);
    }

    public void setBeta(String betaText) {
        beta.setText(betaText);
    }

    public void setGamma(String gammaText) {
        gamma.setText(gammaText);
    }

    public void setDelta(String deltaText) {
        delta.setText(deltaText);
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
