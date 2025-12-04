package com.example.electionsinformationsystem.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // load the fxml
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/electionsinformationsystem/hello-view.fxml"));

        // put the loaded fxml into a scene
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        // attach the stylesheet
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/electionsinformationsystem/lightStyle.css")).toExternalForm());

        // window setup
        stage.setTitle("Elections Information System");
        stage.setScene(scene);

        // show the window
        stage.show();
    }
}