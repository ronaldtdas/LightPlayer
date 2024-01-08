package com.ronaldtdas.lightplayer;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;

public class App extends Application {

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }
    private MediaPlayer mediaPlayer;
    @FXML
    private Label statusLabel;
    @FXML
    private Label playedTimeLabel;
    @FXML
    private Label songTimeLabel;
    @FXML
    private Slider seekSlider;
    @FXML
    private ImageView albumArt;
    private Timeline timeline;
    public void initializeMediaPlayer() {
        // Check if mediaPlayer is already initialized
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Initialize the media player with a sample audio file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Audio File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Media media = new Media(selectedFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // Add listener for updating seek slider
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    seekSlider.setValue(newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
                }
            });
        }
    }

    private void updatePlayedTime() {
        if (mediaPlayer != null) {
            playedTimeLabel.setText(formatDuration(mediaPlayer.getCurrentTime()));
        }
    }
    public void bindMediaPlayerProperties() {
        if (mediaPlayer != null) {
            // Bind properties such as the seek slider, played time label, song time label, etc.
            playedTimeLabel.textProperty().bind(Bindings.createStringBinding(() ->
                    formatDuration(mediaPlayer.getCurrentTime()), mediaPlayer.currentTimeProperty()));

            songTimeLabel.textProperty().bind(Bindings.createStringBinding(() ->
                    formatDuration(mediaPlayer.getTotalDuration()), mediaPlayer.totalDurationProperty()));
        }
    }

    private String formatDuration(Duration duration) {
        // Format the duration as "mm:ss"
        long minutes = (long) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public ImageView getAlbumArt() {
        return albumArt;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root, 400, 300);

        App controller = loader.getController();
        controller.setStatusLabel((Label) scene.lookup("#statusLabel"));
        controller.setPlayedTimeLabel((Label) scene.lookup("#playedTimeLabel"));
        controller.setSongTimeLabel((Label) scene.lookup("#songTimeLabel"));
        controller.setSeekSlider((Slider) scene.lookup("#seekSlider"));
        controller.setAlbumArt((ImageView) scene.lookup("#albumArt"));

        primaryStage.setTitle("Light Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load default album art
        String imagePath = "/img/albumart.jpg";
        Image defaultAlbumArt = new Image(getClass().getResourceAsStream(imagePath));
        controller.getAlbumArt().setImage(defaultAlbumArt);


        // Initialize media player and bindings
        controller.initializeMediaPlayer();
        controller.bindMediaPlayerProperties();

        // Start the timeline for updating played time
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updatePlayedTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setPlayedTimeLabel(Label playedTimeLabel) {
        this.playedTimeLabel = playedTimeLabel;
    }

    public void setSongTimeLabel(Label songTimeLabel) {
        this.songTimeLabel = songTimeLabel;
    }

    public void setSeekSlider(Slider seekSlider) {
        this.seekSlider = seekSlider;
    }

    public void setAlbumArt(ImageView albumArt) {
        this.albumArt = albumArt;
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
