package prashna.adapter;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import prashna.controller.ScoreCellController;
import prashna.model.Score;

import java.io.IOException;

public class ScoreListCell extends JFXListCell<Score> {

    private ScoreCellController controller;
    private Node node;

    public ScoreListCell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/scoreCell.fxml"));
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
    }

    @Override
    protected void updateItem(Score score, boolean empty) {
        super.updateItem(score, empty);
        if (empty) {
            setGraphic(null);
        } else {
            controller.setScore(score.getScore());
            controller.setStudentId(score.getStudentId());
            setGraphic(node);
        }
    }
}
