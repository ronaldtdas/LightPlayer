package com.ronaldtdas.lightplayer;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class App extends Application {

    private MediaPlayer mediaPlayer;
    @FXML
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root, 300, 150);

        // Set the controller for the FXML file
        App controller = loader.getController();
        controller.setStatusLabel((Label) scene.lookup("#statusLabel"));

        primaryStage.setTitle("Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }


    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Audio File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Update the status label with the selected file path
            statusLabel.setText("File selected: " + selectedFile.getAbsolutePath());

            // Stop any currently playing audio
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // Play the selected audio file
            playAudio(selectedFile.toURI().toString());
        } else {
            // Update the status label if no file is selected
            statusLabel.setText("No file selected");

            // Stop any currently playing audio
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // Play the default audio file
            playAudio("default.mp3"); // Replace with the path to your actual default audio file
        }
    }
    private void playAudio(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        // Set the audio to play indefinitely
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Start playing the audio
        mediaPlayer.play();
    }




    @Override
    public void stop() {
        // Stop the audio when the application is closed
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
