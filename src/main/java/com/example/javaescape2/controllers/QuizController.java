package com.example.javaescape2.controllers;

import com.example.javaescape2.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizController {

    @FXML
    private Label lblScore;
    @FXML
    private Label lblQuestion;
    @FXML
    private HBox hboxReponses;
    @FXML
    private Label lblFeedback;
    @FXML
    private Button btnSuivant;

    private int bonnesReponses = 0;
    private String bonneReponse = "";

    // Questions de secours si l'API ne marche pas
    private String[][] questionsSecours = {
            {"Quelle est la capitale de la France ?", "Paris", "Lyon", "Marseille", "Bordeaux"},
            {"Combien font 7 x 8 ?", "56", "54", "48", "63"},
            {"Quel est le plus grand ocean ?", "Pacifique", "Atlantique", "Indien", "Arctique"},
            {"En quelle annee a eu lieu la Revolution francaise ?", "1789", "1776", "1804", "1815"},
            {"Quel element chimique a le symbole O ?", "Oxygene", "Or", "Osmium", "Oganesson"},
            {"Combien de continents y a-t-il ?", "7", "5", "6", "8"},
            {"Quelle est la planete la plus proche du Soleil ?", "Mercure", "Venus", "Mars", "Terre"},
            {"Qui a peint la Joconde ?", "Leonard de Vinci", "Michel-Ange", "Raphael", "Botticelli"}
    };

    private int indexSecours = 0;
    private boolean modeSecours = false;

    @FXML
    public void initialize() {
        chargerQuestion();
    }

    private void chargerQuestion() {
        lblFeedback.setText("");
        btnSuivant.setVisible(false);
        hboxReponses.getChildren().clear();
        lblQuestion.setText("Chargement...");

        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://opentdb.com/api.php?amount=1&type=multiple&language=fr"))
                        .timeout(java.time.Duration.ofSeconds(5))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();

                String question = extraireValeur(body, "question");
                String bonne = extraireValeur(body, "correct_answer");
                List<String> mauvaises = extraireTableau(body, "incorrect_answers");

                if (question.isEmpty() || bonne.isEmpty() || mauvaises.size() < 3) {
                    javafx.application.Platform.runLater(() -> afficherQuestionSecours());
                    return;
                }

                bonneReponse = bonne;
                List<String> reponses = new ArrayList<>();
                reponses.add(bonne);
                reponses.addAll(mauvaises);
                Collections.shuffle(reponses);

                javafx.application.Platform.runLater(() -> {
                    lblQuestion.setText(decodeHtml(question));
                    afficherBoutons(reponses);
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> afficherQuestionSecours());
            }
        }).start();
    }

    private void afficherQuestionSecours() {
        if (indexSecours >= questionsSecours.length) {
            indexSecours = 0;
        }
        String[] q = questionsSecours[indexSecours];
        indexSecours++;

        lblQuestion.setText(q[0]);
        bonneReponse = q[1];

        List<String> reponses = new ArrayList<>();
        for (int i = 1; i < q.length; i++) {
            reponses.add(q[i]);
        }
        Collections.shuffle(reponses);
        afficherBoutons(reponses);
    }

    private void afficherBoutons(List<String> reponses) {
        hboxReponses.getChildren().clear();
        for (String rep : reponses) {
            Button btn = new Button(decodeHtml(rep));
            btn.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 15; -fx-border-color: #e94560; -fx-border-width: 1;");
            btn.setWrapText(true);
            btn.setMaxWidth(160);
            btn.setOnAction(e -> verifierReponse(rep, btn));
            hboxReponses.getChildren().add(btn);
        }
    }

    private String extraireValeur(String json, String cle) {
        Pattern p = Pattern.compile("\"" + cle + "\"\\s*:\\s*\"(.*?)\"");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private List<String> extraireTableau(String json, String cle) {
        List<String> liste = new ArrayList<>();
        Pattern p = Pattern.compile("\"" + cle + "\"\\s*:\\s*\\[(.*?)\\]");
        Matcher m = p.matcher(json);
        if (m.find()) {
            String contenu = m.group(1);
            Pattern pItem = Pattern.compile("\"(.*?)\"");
            Matcher mItem = pItem.matcher(contenu);
            while (mItem.find()) {
                liste.add(mItem.group(1));
            }
        }
        return liste;
    }

    private void verifierReponse(String reponse, Button btn) {
        for (Node node : hboxReponses.getChildren()) {
            node.setDisable(true);
        }

        if (reponse.equals(bonneReponse)) {
            bonnesReponses++;
            btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 15;");
            lblFeedback.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 14px;");
            lblFeedback.setText("Correct !");
        } else {
            btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 15;");
            lblFeedback.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            lblFeedback.setText("Incorrect ! Bonne reponse : " + decodeHtml(bonneReponse));
        }

        lblScore.setText(bonnesReponses + "/5");
        btnSuivant.setVisible(true);
    }

    @FXML
    private void questionSuivante() {
        if (bonnesReponses >= 5) {
            allerAuDialogueIntermediaire();
        } else {
            chargerQuestion();
        }
    }

    private void allerAuDialogueIntermediaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaescape2/views/dialogue2.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            MainApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String decodeHtml(String text) {
        return text.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&eacute;", "é")
                .replace("&egrave;", "è")
                .replace("&agrave;", "à")
                .replace("&ugrave;", "ù")
                .replace("&ocirc;", "ô");
    }
}