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

public class Dialogue3Controller {

    @FXML
    private Label lblTexte;
    @FXML
    private Label lblNom;

    private String[] dialogues = {
            "Tu as trouve l'emplacement de la bombe. C'est un soulagement, mais ne te repose pas encore. Le plus difficile reste a venir.",
            "Nous savons maintenant ou elle se trouve, mais il faut encore la desamorcer. C'est une course contre la montre.",
            "Tu as deja prouve que tu es capable de prendre les bonnes decisions. Ne laisse pas cette derniere etape t'intimider.",
            "Bonne chance. Et rappelle-toi, le destin de tout le monde est entre tes mains."
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
                allerMastermind();
            }
        }
    }

    private void allerMastermind() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/mastermind.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}