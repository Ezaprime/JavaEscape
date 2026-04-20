package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.File;

public class MenuController {

    @FXML
    private Button btnReprendre;

    @FXML
    public void initialize() {
        File save = new File("save.json");
        if (!save.exists()) {
            btnReprendre.setDisable(true);
        }
    }

    @FXML
    private void nouvellePartie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/dialogue.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void reprendrePartie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/dialogue.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quitter() {
        System.exit(0);
    }
}