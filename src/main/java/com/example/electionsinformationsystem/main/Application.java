package com.example.electionsinformationsystem.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        // load the fxml
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/electionsinformationsystem/view.fxml"));

        // put the loaded fxml into a scene (16:9)
        Scene scene = new Scene(fxmlLoader.load(), 1300, 731.25);

        // attach the stylesheet
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/electionsinformationsystem/lightStyle.css")).toExternalForm());

        // window setup
        stage.setTitle("Elections Information System");
        stage.setScene(scene);

        // show the window
        stage.show();
    }
}