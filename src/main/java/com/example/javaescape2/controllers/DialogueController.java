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
            "Ecoute-moi bien. Une bombe a ete placee quelque part en ville, et tout repose sur toi. Nous n'avons pas de temps a perdre. Chaque seconde compte.",
            "Voici la situation : tu vas devoir resoudre une serie d'enigmes. Chacune te donnera des indices pour localiser la bombe. Le temps presse.",
            "Je sais que ce n'est pas facile, mais je crois en toi. Nous avons les outils necessaires, et tu as l'intelligence pour dechiffrer ces enigmes.",
            "Ne laisse pas la pression te faire trebucher. Resous les enigmes, trouve l'emplacement de la bombe, et nous pourrons la desamorcer."
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
                allerAuQuiz();
            }
        }
    }

    private void allerAuQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/quiz.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}