package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Random;

public class DemineurController {

    @FXML
    private GridPane grid;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnRecommencer;

    private static final int LIGNES = 8;
    private static final int COLS = 8;
    private static final int BOMBES = 8;

    private Button[][] boutons;
    private boolean[][] estBombe;
    private boolean[][] revele;
    private boolean[][] drapeau;
    private int[][] voisins;
    private int nbReveles;
    private boolean gameOver;
    private boolean premierClic;

    @FXML
    public void initialize() {
        nouvellePartie();
    }

    private void nouvellePartie() {
        grid.getChildren().clear();
        boutons = new Button[LIGNES][COLS];
        estBombe = new boolean[LIGNES][COLS];
        revele = new boolean[LIGNES][COLS];
        drapeau = new boolean[LIGNES][COLS];
        voisins = new int[LIGNES][COLS];
        nbReveles = 0;
        gameOver = false;
        premierClic = true;
        lblStatus.setText("Trouve toutes les cases sans bombe !");
        lblStatus.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");

        for (int r = 0; r < LIGNES; r++) {
            for (int c = 0; c < COLS; c++) {
                Button btn = new Button();
                btn.setPrefSize(48, 48);
                btn.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-border-color: #e94560; -fx-border-width: 1; -fx-font-size: 13px;");
                final int row = r;
                final int col = c;
                btn.setOnMouseClicked(e -> {
                    if (gameOver) return;
                    if (e.getButton() == MouseButton.SECONDARY ||
                            (e.getButton() == MouseButton.PRIMARY && e.isControlDown())) {
                        toggleDrapeau(row, col);
                    } else if (e.getButton() == MouseButton.PRIMARY) {
                        reveler(row, col);
                    }
                });
                boutons[r][c] = btn;
                grid.add(btn, c, r);
            }
        }
    }

    private void placerBombes(int safeR, int safeC) {
        Random rng = new Random();
        int placed = 0;
        while (placed < BOMBES) {
            int r = rng.nextInt(LIGNES);
            int c = rng.nextInt(COLS);
            if (estBombe[r][c]) continue;
            if (Math.abs(r - safeR) <= 1 && Math.abs(c - safeC) <= 1) continue;
            estBombe[r][c] = true;
            placed++;
        }

        for (int r = 0; r < LIGNES; r++) {
            for (int c = 0; c < COLS; c++) {
                if (estBombe[r][c]) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr;
                        int nc = c + dc;
                        if (nr >= 0 && nr < LIGNES && nc >= 0 && nc < COLS && estBombe[nr][nc]) {
                            count++;
                        }
                    }
                }
                voisins[r][c] = count;
            }
        }
    }

    private void reveler(int r, int c) {
        if (revele[r][c] || drapeau[r][c]) return;

        if (premierClic) {
            placerBombes(r, c);
            premierClic = false;
        }

        if (estBombe[r][c]) {
            boutons[r][c].setText("💣");
            boutons[r][c].setStyle("-fx-background-color: #e74c3c; -fx-font-size: 16px;");
            lblStatus.setText("Bombe ! Clique sur Recommencer.");
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            gameOver = true;
            revelerToutesBombes();
            return;
        }

        revelerFlood(r, c);

        if (nbReveles == LIGNES * COLS - BOMBES) {
            lblStatus.setText("Bravo ! Tu as localise la bombe !");
            lblStatus.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 14px;");
            gameOver = true;
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> allerDialogue3());
            pause.play();
        }
    }

    private void revelerFlood(int r, int c) {
        if (r < 0 || r >= LIGNES || c < 0 || c >= COLS) return;
        if (revele[r][c] || drapeau[r][c] || estBombe[r][c]) return;

        revele[r][c] = true;
        nbReveles++;
        boutons[r][c].setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-border-color: #333; -fx-border-width: 1; -fx-font-size: 13px;");
        boutons[r][c].setDisable(true);

        if (voisins[r][c] > 0) {
            boutons[r][c].setText(String.valueOf(voisins[r][c]));
            return;
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                revelerFlood(r + dr, c + dc);
            }
        }
    }

    private void toggleDrapeau(int r, int c) {
        if (revele[r][c]) return;
        drapeau[r][c] = !drapeau[r][c];
        boutons[r][c].setText(drapeau[r][c] ? "🚩" : "");
    }

    private void revelerToutesBombes() {
        for (int r = 0; r < LIGNES; r++) {
            for (int c = 0; c < COLS; c++) {
                if (estBombe[r][c]) {
                    boutons[r][c].setText("💣");
                    boutons[r][c].setStyle("-fx-background-color: #e74c3c; -fx-font-size: 16px;");
                }
            }
        }
    }

    @FXML
    private void recommencer() {
        nouvellePartie();
    }

    private void allerDialogue3() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/dialogue3.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}