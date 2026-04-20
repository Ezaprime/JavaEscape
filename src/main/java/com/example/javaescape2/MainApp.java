package com.example.javaescape2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/menu.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("Java Escape");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}