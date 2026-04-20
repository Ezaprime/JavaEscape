package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class DialogueController {

    @FXML
    private Label lblTexte;
    @FXML
    private Label lblNom;
    @FXML
    private ImageView imgPersonnage;

    private String[] dialogues = {
            "Ecoute-moi bien. Une bombe a ete placee quelque part en ville.",
            "Tu vas devoir resoudre des enigmes pour localiser la bombe.",
            "Je crois en toi. Tu as les outils pour reussir cette mission.",
            "Ne laisse pas la pression te faire trebucher. On compte sur toi."
    };

    private int indexDialogue = 0;
    private Timeline timeline;
    private boolean estEnTrain = false;

    @FXML
    public void initialize() {
        afficherDialogue(dialogues[indexDialogue]);
        javafx.application.Platform.runLater(() -> {
            MainApp.mainStage.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    gererEspace();
                }
            });
        });
    }

    private void afficherDialogue(String texte) {
        estEnTrain = true;
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
                estEnTrain = false;
            }
        });
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private void gererEspace() {
        if (estEnTrain) {
            timeline.stop();
            lblTexte.setText(dialogues[indexDialogue]);
            estEnTrain = false;
        } else {
            indexDialogue++;
            if (indexDialogue < dialogues.length) {
                afficherDialogue(dialogues[indexDialogue]);
            } else {
                allerSuite();
            }
        }
    }

    private void allerSuite() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/dialogue2.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}