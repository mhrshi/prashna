package prashna.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXListView;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import prashna.adapter.QuestionListCell;
import prashna.adapter.StudentQuizListCell;
import prashna.core.Store;
import prashna.model.NoSelectionModel;
import prashna.model.Question;
import prashna.model.StudentQuiz;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML private JFXListView<StudentQuiz> quizListView;
    @FXML private JFXListView<Question> questionListView;
    @FXML private HBox holderHBox;
    @FXML private ImageView submitImageView;
    private ObservableList<StudentQuiz> quizList;
    private ObservableList<Question> questionList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        quizList = FXCollections.observableArrayList();
        quizListView.setCellFactory(lv -> new StudentQuizListCell());
        quizListView.setItems(quizList);
        quizListView.setExpanded(true);
        quizListView.setDepth(1);

        questionList = FXCollections.observableArrayList();
        questionListView.setCellFactory(lv -> new QuestionListCell());
        questionListView.setItems(questionList);
        questionListView.setSelectionModel(new NoSelectionModel<>());

        checkQuizzes();

        quizListView.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((obs, oldVal, newVal) -> {
            questionList.setAll(newVal.getQuestions());
        });

        holderHBox.setOnMouseClicked(event -> {
            Store store = Store.get();
            store.setQuizId(quizListView.getSelectionModel()
                                        .getSelectedItem()
                                        .getQuizId());
            try {
                Unirest.post("http://localhost:8080/Prashna/takeResponse")
                       .field("answers", store.getJsonString())
                       .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            Platform.exit();
        });
    }

    private void checkQuizzes() {
        boolean present;
        HttpResponse<String> response = null;
        try {
            response = Unirest.post("http://localhost:8080/Prashna/getStudentQuizzes")
                              .asString();
            present = !response.getBody().trim().startsWith("none");
        } catch (UnirestException e) {
            present = false;
            e.printStackTrace();
        }
        holderHBox.setVisible(present);
        if (present) {
            JsonParser parser = new JsonParser();
            JsonObject masterJson = parser.parse(response.getBody().trim()).getAsJsonObject();
            int returned = masterJson.get("returned").getAsInt();
            for (int i = 1; i <= returned; i++) {
                JsonObject questions = masterJson.get(String.valueOf(i)).getAsJsonObject();
                quizList.add(new StudentQuiz(questions));
            }
        }
    }
}
