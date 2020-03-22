package pl.javastart.Mp3Player.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;


public class PlayListNameController implements Serializable {
public static final Long serialVersionUID=925875872385L;


        @FXML
        private Label playlistNameLabel;

        @FXML
        private TextField playListNameTextField;

    public void setPlaylistNameLabel(Label playlistNameLabel) {
        this.playlistNameLabel = playlistNameLabel;
    }

    public void setPlayListNameTextField(TextField playListNameTextField) {
        this.playListNameTextField = playListNameTextField;
    }

    public void setZastosujButton(Button zastosujButton) {
        this.zastosujButton = zastosujButton;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    @FXML
         private Button zastosujButton;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
        private MainController mainController;





    public Button getZastosujButton() {
        return zastosujButton;
    }
    public Label getPlaylistNameLabel() {
        return playlistNameLabel;
    }


    public TextField getPlayListNameTextField() {
        return playListNameTextField;
    }
    private String playlistName=null;

    public String getPlaylistName() {
        return playlistName;

    }





    public void initialize(){
        System.out.println("PlayListNameController Created");
        playListNameTextField.setText("wprowadż nazwe playlisty");


    }

}
