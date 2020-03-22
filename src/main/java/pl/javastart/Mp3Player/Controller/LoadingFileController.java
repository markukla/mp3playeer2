package pl.javastart.Mp3Player.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadingFileController {

    @FXML
    private Label filePathLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label descriptionLabel;

    public Label getFilePathLabel() {
        return filePathLabel;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Label getDescriptionLabel() {
        return descriptionLabel;
    }

    public MainController getMainController() {
        return mainController;
    }

    MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        System.out.println("loadingFileControllerCreated");
    }
}


