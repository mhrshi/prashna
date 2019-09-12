package prashna.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import prashna.core.Store;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final int N_SLIDES = 5;

    @FXML private StackPane rootStackPane;
    @FXML private AnchorPane childAnchorPane;
    @FXML private StackPane slideStack;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private JFXButton loginButton;

    private HBox[] slides = new HBox[N_SLIDES];
    private int currentSlide = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        childAnchorPane.minWidthProperty().bind(rootStackPane.widthProperty());
        childAnchorPane.minHeightProperty().bind(rootStackPane.heightProperty());

        try {
            for (int i = 0; i < N_SLIDES; i++) {
                slides[i] = FXMLLoader.load(getClass().getResource("../fxml/slide" + (i + 1) + ".fxml"));
                slides[i].prefHeightProperty().bind(slideStack.heightProperty());
                slides[i].prefWidthProperty().bind(slideStack.widthProperty());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        slideStack.getChildren().setAll(getNextSlide());
        Timeline slideshow = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> {
                    Node nextSlide = getNextSlide();
                    double windowWidth = slideStack.getScene().getWindow().getWidth();
                    nextSlide.translateXProperty().set(windowWidth);
                    slideStack.getChildren().add(nextSlide);
                    Timeline slideInFromLeft = new Timeline();
                    KeyValue kv = new KeyValue(
                            nextSlide.translateXProperty(),
                            0,
                            Interpolator.EASE_BOTH
                    );
                    KeyFrame kf = new KeyFrame(Duration.seconds(1.2), kv);
                    slideInFromLeft.getKeyFrames().add(kf);
                    slideInFromLeft.setOnFinished(e -> slideStack.getChildren().remove(getPrevSlide()));
                    slideInFromLeft.play();
                }
        ));
        slideshow.setCycleCount(Timeline.INDEFINITE);
        slideshow.play();

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Required");
        usernameTextField.getValidators().add(validator);
        passwordTextField.getValidators().add(validator);
    }

    private Node getNextSlide() {
        currentSlide = (currentSlide + 1) % N_SLIDES;
        return slides[currentSlide];
    }

    private Node getPrevSlide() {
        int prevSlide = currentSlide - 1;
        prevSlide = prevSlide < 0 ? N_SLIDES - 1 : prevSlide;
        return slides[prevSlide];
    }

    @FXML
    void logIn() throws UnirestException {
        if (usernameTextField.validate() && passwordTextField.validate()) {
            HttpResponse<String> response =
                    Unirest.post("http://localhost:8080/Prashna/login")
                           .field("username", usernameTextField.getText())
                           .field("password", passwordTextField.getText())
                           .asString();
            boolean valid = response.getBody().trim().equals("valid");
            if (valid) {
                goToNext();
            } else {
                showInvalidDialog();
            }
        }
    }

    private void showInvalidDialog() {
        try {
            Parent errorDialog = FXMLLoader.load(getClass().getResource("../fxml/loginErrorDialog.fxml"));
            JFXDialogLayout layout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(rootStackPane, layout, JFXDialog.DialogTransition.CENTER);
            Label headingLabel = new Label();
            headingLabel.setFont(new Font("Roboto", 20));
            headingLabel.setText("Invalid credentials :(");
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

    private void goToNext() {
        String layout = "";
        if (usernameTextField.getText().startsWith("IU")) {
            Store store = Store.get();
            store.setUserId(usernameTextField.getText());
            layout = "student";
        } else {
            layout = "faculty";
        }
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/" + layout + ".fxml"));
            Stage stage = new Stage();
            stage.setTitle("Prashna - " + usernameTextField.getText());
            stage.setScene(new Scene(root, 1200, 700));
            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("../../resources/logo32.png"))
                                );
            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("../../resources/logo64.png"))
                                );
            stage.show();
            slideStack.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
