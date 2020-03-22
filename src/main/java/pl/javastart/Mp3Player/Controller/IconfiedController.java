package pl.javastart.Mp3Player.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class IconfiedController {


    @FXML
    private Button previousButton;

    @FXML
    private ToggleButton playButton;

    @FXML
    private Button nextButton;

    @FXML
    private ToggleButton playAllSongsButton;

    @FXML
    private ToggleButton ReplayPlaylistButton;

    @FXML
    private ToggleButton loopSong;

    @FXML
    private ToggleButton random;


    @FXML
    private Slider progressSlider;

    @FXML
    private ToggleButton reverseTimeDisplay;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label songLengthLabel;

    @FXML
    private TextArea messageTextArea;

    public Button getPreviousButton() {
        return previousButton;
    }

    public ToggleButton getPlayButton() {
        return playButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public ToggleButton getPlayAllSongsButton() {
        return playAllSongsButton;
    }

    public ToggleButton getReplayPlaylistButton() {
        return ReplayPlaylistButton;
    }

    public ToggleButton getLoopSong() {
        return loopSong;
    }

    public ToggleButton getRandom() {
        return random;
    }


    public Slider getProgressSlider() {
        return progressSlider;
    }

    public ToggleButton getReverseTimeDisplay() {
        return reverseTimeDisplay;
    }

    public Label getCurrentTimeLabel() {
        return currentTimeLabel;
    }

    public Label getSongLengthLabel() {
        return songLengthLabel;
    }

    public TextArea getMessageTextArea() {
        return messageTextArea;
    }

    public MainController getMainController() {
        return mainController;
    }

    @FXML
    MainController mainController;

    public void setPreviousButton(Button previousButton) {
        this.previousButton = previousButton;
    }

    public void setPlayButton(ToggleButton playButton) {
        this.playButton = playButton;
    }

    public void setNextButton(Button nextButton) {
        this.nextButton = nextButton;
    }

    public void setPlayAllSongsButton(ToggleButton playAllSongsButton) {
        this.playAllSongsButton = playAllSongsButton;
    }

    public void setReplayPlaylistButton(ToggleButton replayPlaylistButton) {
        ReplayPlaylistButton = replayPlaylistButton;
    }

    public void setLoopSong(ToggleButton loopSong) {
        this.loopSong = loopSong;
    }

    public void setRandom(ToggleButton random) {
        this.random = random;
    }


    public void setProgressSlider(Slider progressSlider) {
        this.progressSlider = progressSlider;
    }

    public void setReverseTimeDisplay(ToggleButton reverseTimeDisplay) {
        this.reverseTimeDisplay = reverseTimeDisplay;
    }

    public void setCurrentTimeLabel(Label currentTimeLabel) {
        this.currentTimeLabel = currentTimeLabel;
    }

    public void setSongLengthLabel(Label songLengthLabel) {
        this.songLengthLabel = songLengthLabel;
    }

    public void setMessageTextArea(TextArea messageTextArea) {
        this.messageTextArea = messageTextArea;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        System.out.println("iconfied Controller Created");


    }


}