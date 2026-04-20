package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class VictoireController {

    @FXML
    private Label lblTexte;

    @FXML
    public void initialize() {
        lblTexte.setText("Tu l'as fait ! Tu as reussi a desamorcer la bombe et a sauver la ville.\nGrace a toi, des vies ont ete sauvees aujourd'hui.\nBien joue, vraiment. Tu as prouve qu'il n'y a rien que tu ne puisses accomplir.");
    }

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