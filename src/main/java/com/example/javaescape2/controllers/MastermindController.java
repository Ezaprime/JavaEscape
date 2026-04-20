package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class MastermindController {

    @FXML
    private VBox historique;
    @FXML
    private HBox ligneActuelle;
    @FXML
    private Label lblTentatives;
    @FXML
    private Button btnValider;

    private static final int SLOTS = 4;
    private static final int MAX_ESSAIS = 10;
    private static final Color[] COULEURS = {
            Color.CRIMSON, Color.FORESTGREEN, Color.ROYALBLUE,
            Color.GOLD, Color.DARKORCHID, Color.DARKORANGE
    };

    private int[] secret;
    private int[] courant = new int[SLOTS];
    private int essaisRestants = MAX_ESSAIS;
    private int slotSelectionne = 0;

    @FXML
    public void initialize() {
        genererSecret();
        rafraichirLigne();
        lblTentatives.setText("Tentatives restantes : " + MAX_ESSAIS);
    }

    private void genererSecret() {
        Random rng = new Random();
        secret = new int[SLOTS];
        for (int i = 0; i < SLOTS; i++) {
            secret[i] = rng.nextInt(COULEURS.length);
        }
    }

    private void rafraichirLigne() {
        ligneActuelle.getChildren().clear();
        for (int i = 0; i < SLOTS; i++) {
            Circle cercle = new Circle(18);
            if (courant[i] == -1) {
                cercle.setFill(Color.LIGHTGRAY);
            } else {
                cercle.setFill(COULEURS[courant[i]]);
            }
            final int index = i;
            cercle.setOnMouseClicked(e -> slotSelectionne = index);
            ligneActuelle.getChildren().add(cercle);
        }
    }

    @FXML
    public void choisirCouleur0() { choisirCouleur(0); }
    @FXML
    public void choisirCouleur1() { choisirCouleur(1); }
    @FXML
    public void choisirCouleur2() { choisirCouleur(2); }
    @FXML
    public void choisirCouleur3() { choisirCouleur(3); }
    @FXML
    public void choisirCouleur4() { choisirCouleur(4); }
    @FXML
    public void choisirCouleur5() { choisirCouleur(5); }

    private void choisirCouleur(int couleurIndex) {
        courant[slotSelectionne] = couleurIndex;
        slotSelectionne = (slotSelectionne + 1) % SLOTS;
        rafraichirLigne();
    }

    @FXML
    private void valider() {
        for (int v : courant) {
            if (v == -1) {
                lblTentatives.setText("Choisis une couleur pour chaque slot !");
                return;
            }
        }

        int bienPlaces = 0;
        int bonnesCouleurs = 0;
        boolean[] usedSecret = new boolean[SLOTS];
        boolean[] usedGuess = new boolean[SLOTS];

        for (int i = 0; i < SLOTS; i++) {
            if (courant[i] == secret[i]) {
                bienPlaces++;
                usedSecret[i] = true;
                usedGuess[i] = true;
            }
        }

        for (int i = 0; i < SLOTS; i++) {
            if (usedGuess[i]) continue;
            for (int j = 0; j < SLOTS; j++) {
                if (!usedSecret[j] && courant[i] == secret[j]) {
                    bonnesCouleurs++;
                    usedSecret[j] = true;
                    break;
                }
            }
        }

        HBox ligne = new HBox(10);
        ligne.setAlignment(javafx.geometry.Pos.CENTER);
        for (int v : courant) {
            Circle c = new Circle(14, COULEURS[v]);
            ligne.getChildren().add(c);
        }
        Label indice = new Label("Bien places : " + bienPlaces + "  |  Bonnes couleurs : " + bonnesCouleurs);
        indice.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px;");
        ligne.getChildren().add(indice);
        historique.getChildren().add(ligne);

        if (bienPlaces == SLOTS) {
            lblTentatives.setText("Bombe desamorcee !");
            lblTentatives.setStyle("-fx-text-fill: #2ecc71;");
            btnValider.setDisable(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> allerVictoire());
            pause.play();
            return;
        }

        essaisRestants--;
        lblTentatives.setText("Tentatives restantes : " + essaisRestants);

        if (essaisRestants <= 0) {
            lblTentatives.setText("Echec ! La bombe a explose !");
            lblTentatives.setStyle("-fx-text-fill: #e74c3c;");
            btnValider.setDisable(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> allerDefaite());
            pause.play();
            return;
        }

        for (int i = 0; i < SLOTS; i++) {
            courant[i] = -1;
        }
        slotSelectionne = 0;
        rafraichirLigne();
    }

    private void allerVictoire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/victoire.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allerDefaite() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/defaite.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}