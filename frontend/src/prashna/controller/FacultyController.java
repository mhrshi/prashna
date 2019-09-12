package prashna.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import prashna.adapter.QuizListCell;
import prashna.adapter.ScoreListCell;
import prashna.core.ExcelHandler;
import prashna.model.NoSelectionModel;
import prashna.model.Quiz;
import prashna.model.Score;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.ResourceBundle;

public class FacultyController implements Initializable {

    private static final String XLSX_MIME = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @FXML private JFXTabPane tabPane;
    @FXML private HBox fileArea;
    @FXML private StackPane stackPane;
    @FXML private JFXListView<Quiz> quizListView;
    @FXML private JFXListView<Score> scoreListView;
    private ObservableList<Quiz> quizList;
    private ObservableList<Score> scoreList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tabPane.widthProperty().addListener((obs, newVal, oldVal) -> {
            tabPane.setTabMinWidth((tabPane.getWidth() / 2) - 10);
            tabPane.setTabMaxWidth((tabPane.getWidth() / 2) - 10);
        });

        attachDragAndDrop();

        quizList = FXCollections.observableArrayList();
        quizListView.setCellFactory(lv -> new QuizListCell());
        quizListView.setItems(quizList);
        quizListView.setExpanded(true);
        quizListView.setDepth(1);

        scoreList = FXCollections.observableArrayList();
        scoreListView.setCellFactory(lv -> new ScoreListCell());
        scoreListView.setItems(scoreList);
        scoreListView.setSelectionModel(new NoSelectionModel<>());

        checkQuizzes();
    }

    private void checkQuizzes() {
        boolean present;
        HttpResponse<String> response = null;
        try {
            response = Unirest.post("http://localhost:8080/Prashna/getFacultyQuizzes")
                              .asString();
            present = !response.getBody().trim().startsWith("none");
        } catch (UnirestException e) {
            present = false;
            e.printStackTrace();
        }
        if (present) {
            JsonParser parser = new JsonParser();
            JsonObject masterJson = parser.parse(response.getBody().trim()).getAsJsonObject();
            JsonObject questions = masterJson.get("content").getAsJsonObject();
            quizList.add(new Quiz(questions));
            if (masterJson.get("scores").isJsonObject()) {
                JsonObject scores = masterJson.get("scores").getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : scores.entrySet()) {
                    scoreList.add(new Score(
                            entry.getKey(),
                            entry.getValue().getAsString()
                    ));
                }
            }
        }
    }

    private void attachDragAndDrop() {
        fileArea.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        fileArea.setOnDragDropped(event -> {
            File file = event.getDragboard().getFiles().get(0);
            String mime = "";
            try {
                mime = Files.probeContentType(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!file.getName().endsWith(".xlsx") || !mime.equals(XLSX_MIME)) {
                showErrorDialog();
            } else {
                ExcelHandler handler = new ExcelHandler(file);
                try {
                    handler.toJson();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                checkQuizzes();
                tabPane.getSelectionModel().select(0);
            }
        });
    }

    private void showErrorDialog() {
        try {
            Parent errorDialog = FXMLLoader.load(getClass().getResource("../fxml/fileErrorDialog.fxml"));
            JFXDialogLayout layout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
            Label headingLabel = new Label();
            headingLabel.setFont(new Font("Roboto", 20));
            headingLabel.setText("Invalid file :(");
            headingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4d4d4d;");
            layout.setHeading(headingLabel);
            layout.setBody(errorDialog);
            JFXButton closeButton = new JFXButton("UNDERSTOOD");
            closeButton.setStyle("-fx-text-fill: #2196f3;" +
                                 "-fx-padding: 16;" +
                                 "-fx-font-weight: bold;" +
                                 "-fx-font-family: Roboto;" +
                                 "-fx-font-size: 16");
            closeButton.setOnAction(evt -> dialog.close());
            layout.setActions(closeButton);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
