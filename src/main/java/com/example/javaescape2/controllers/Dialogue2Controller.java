package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class Dialogue2Controller {

    @FXML
    private Label lblTexte;
    @FXML
    private Label lblNom;

    private String[] dialogues = {
            "Bien joue. Tu as bien avance jusqu'ici. Tu as resolu toutes les enigmes, et maintenant nous avons une meilleure idee de l'endroit ou la bombe pourrait etre.",
            "Maintenant, il te faut localiser l'emplacement exact. Tu vas devoir resoudre une grille de demineur pour trouver la bombe.",
            "Le temps presse. Chaque erreur pourrait nous couter cher. Trouve ou elle se cache.",
            "Tu es notre seul espoir. Trouve la bombe et localise-la avec precision."
    };

    private int index = 0;
    private Timeline timeline;
    private boolean enTrain = false;

    @FXML
    public void initialize() {
        afficher(dialogues[index]);
        javafx.application.Platform.runLater(() -> {
            MainApp.mainStage.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    gererEspace();
                }
            });
        });
    }

    private void afficher(String texte) {
        enTrain = true;
        lblTexte.setText("");
        final int[] i = {0};
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(30), e -> {
            if (i[0] < texte.length()) {
                lblTexte.setText(lblTexte.getText() + texte.charAt(i[0]));
                i[0]++;
            } else {
                timeline.stop();
                enTrain = false;
            }
        });
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private void gererEspace() {
        if (enTrain) {
            timeline.stop();
            lblTexte.setText(dialogues[index]);
            enTrain = false;
        } else {
            index++;
            if (index < dialogues.length) {
                afficher(dialogues[index]);
            } else {
                allerDemineur();
            }
        }
    }

    private void allerDemineur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/demineur.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}