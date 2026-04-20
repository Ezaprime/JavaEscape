package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class DefaiteController {

    @FXML
    private void rejouer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/menu.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}