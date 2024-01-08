package com.ronaldtdas.lightplayer;

import javafx.application.Application;
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
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Audio Player");

        // Create a button to open an audio file
        Button openButton = new Button("Open File");
        openButton.setOnAction(e -> openFile());



        // Create a label to display the status
        statusLabel = new Label("No file selected");

        // Add components to the layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(openButton, statusLabel);

        // Add the layout to the scene
        primaryStage.setScene(new Scene(layout, 300, 150));

        primaryStage.show();
    }



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
