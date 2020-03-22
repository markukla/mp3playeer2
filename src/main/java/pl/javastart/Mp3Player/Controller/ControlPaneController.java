package pl.javastart.Mp3Player.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.farng.mp3.TagException;

import java.io.IOException;
import java.io.Serializable;


public class ControlPaneController implements Serializable {
    public static final Long serialVesrionUID = 74297498283747484L;


    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // dla referencji oznaczonych adnotacją @Fxml są automatycznie tworzone obiekty przez FXMLLoader, nie trzeba ich tworzyć w kodzie poprzez operator new
    @FXML
    private MainController mainController;


    @FXML
    private Button previousButton;

    @FXML
    private ToggleButton playButton;

    @FXML
    private Button nextButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressSlider;
    @FXML
    private ToggleButton random;
    @FXML
    private ImageView volumeIcon;

    @FXML
    private Label volumeLevel;


    public ToggleButton getPlayAllSongsButton() {
        return playAllSongsButton;
    }

    public ToggleButton getReplayPlaylistButton() {
        return ReplayPlaylistButton;
    }

    @FXML
    private ToggleButton playAllSongsButton;

    @FXML
    private ToggleButton ReplayPlaylistButton;

    public ToggleButton getLoopSong() {
        return loopSong;
    }

    @FXML
    private ToggleButton loopSong;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Label currentTimeLabel;


    @FXML
    private Label songLengthLabel;

    @FXML
    private ToggleButton reverseTimeDisplay;

    public ToggleButton getRandom() {
        return random;
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


    public Button getPreviousButton() {
        return previousButton;
    }

    public ToggleButton getPlayButton() {
        return playButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public Slider getProgressSlider() {
        return progressSlider;
    }

    public TextArea getMessageTextArea() {
        return messageTextArea;
    }

    public ImageView getVolumeIcon() {
        return volumeIcon;
    }

    public Label getVolumeLevel() {
        return volumeLevel;
    }

    public void initialize() {
        System.out.println("Control controller created");
        configureButtons();
        configureVolume();
        configureSliders();


    }

    private void configureSliders() {
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNumber, Number newNumber) {
                System.out.println("zmiana głośności  " + newNumber.doubleValue());

            }
        });


    }

    private void configureVolume() {
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                System.out.println("zmiana głośności " + newValue.doubleValue());
            }
        });

    }

    private void configureButtons() {
        previousButton.setOnAction(event -> System.out.println("Poprzednia piosenka"));
        nextButton.setOnAction(x -> System.out.println("Następna piosenka"));
        playButton.setOnAction(event -> {
            if (playButton.isSelected()) {
                System.out.println("Play");
            } else {
                System.out.println("Stop");
            }
        });
    }


}