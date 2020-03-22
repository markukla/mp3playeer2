package pl.javastart.Mp3Player.Controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

public class MenuPaneController implements Serializable {
    public static final Long serialVesrionUID = 7429749828738484L;
    @FXML
    private MenuItem fileMenuItem;

    @FXML
    private MenuItem dirMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem openPlayListMenuItem;

    public MenuItem getOpenPlayListMenuItem() {
        return openPlayListMenuItem;
    }

    public MenuItem getSavePlayListMenuItem() {
        return savePlayListMenuItem;
    }

    @FXML
    private MenuItem savePlayListMenuItem;

    @FXML
    private TextField search;
    @FXML
    private MenuItem removeSongFromPlaylist;
    @FXML
    private Button openMp3File;

    @FXML
    private Button removeSongs;

    @FXML
    private Button addDirectory;

    @FXML
    private Button savePlaylist;

    @FXML
    private Button openPlaylist;

    public MenuItem getRemoveSongFromPlaylist() {
        return removeSongFromPlaylist;
    }

    public TextField getSearch() {
        return search;
    }

    public MenuItem getFileMenuItem() {
        return fileMenuItem;
    }

    public MenuItem getDirMenuItem() {
        return dirMenuItem;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public Button getOpenMp3File() {
        return openMp3File;
    }

    public Button getRemoveSongs() {
        return removeSongs;
    }

    public Button getAddDirectory() {
        return addDirectory;
    }

    public Button getSavePlaylist() {
        return savePlaylist;
    }

    public Button getOpenPlaylist() {
        return openPlaylist;
    }

    public void initialize() {
        System.out.println("Menu controller created");
        configureMenu();
    }

    private void configureMenu() {
        closeMenuItem.setOnAction(actionEvent ->
                Platform.exit());

        aboutMenuItem.setOnAction(actionEvent -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/fxml/aboutPane.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                stage.setTitle("Mp3Player v1.0 - about");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}