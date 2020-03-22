package pl.javastart.Mp3Player.mp3;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import pl.javastart.Mp3Player.Controller.ContentPaneController;
import pl.javastart.Mp3Player.Controller.LoadingFileController;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

public class Mp3Parser implements Serializable {
    public static final Long serialVersionUID = 492481425839593L;
    @FXML
    private LoadingFileController loadingFileController;

    public LoadingFileController getLoadingFileController() {
        return loadingFileController;
    }

    public List<Mp3Song> getSongList() {
        return songList;
    }

    public Task<List<Mp3Song>> getProgressTaskG() {
        return progressTaskG;
    }

    private List<Mp3Song> songList;
    private Task<List<Mp3Song>> progressTaskG;

    public Mp3Song createMp3Song(File file) throws IOException, TagException {
        MP3File mp3File = new MP3File(file);
        String absolutePath = file.getAbsolutePath();
        String title = null;
        String author = null;
        String album = null;
        CheckBox checkBox = null;
        if (mp3File.getID3v2Tag() != null && file != null && mp3File != null) {
            title = mp3File.getID3v2Tag().getSongTitle();
            author = mp3File.getID3v2Tag().getLeadArtist();
            album = mp3File.getID3v2Tag().getAlbumTitle();
            checkBox = new CheckBox();
            return new Mp3Song(title, author, album, absolutePath, checkBox);
        } else {
            return null;
        }

    }


    public List<Mp3Song> createMp3List(File dir) throws IOException, TagException, FileNotFoundException {

        List<File> fileList = new ArrayList<>();

        createFileListFromComplexedDirectories(dir, fileList);  // metoda do przeszukiwania podfolderów


        Task<List<Mp3Song>> progressTask = new Task<>() {
            @Override
            protected List<Mp3Song> call() throws Exception {
                List<Mp3Song> songListT = new ArrayList<>();

                int i;
                final int totalIterations = fileList.size();
                for (i = 0; i < totalIterations; i++) {
                    try {

                        String fileExtension = fileList.get(i).getName().substring(fileList.get(i).getName().lastIndexOf(".") + 1);


                        if (fileExtension.equals("mp3") && createMp3Song(fileList.get(i)) != null) {

                            songListT.add(createMp3Song(fileList.get(i)));


                            updateProgress(i, fileList.size() - 1);
                            updateMessage(fileList.get(i).getName());

                        }

                    } catch (FileNotFoundException fnf) {

                        continue;// przejdź do następnej iteracji
                    }
                }


                return songListT;
            }
        };






        Thread thread=new Thread(progressTask);

        progressTask.setOnSucceeded(workerStateEvent -> {

            songList = progressTask.getValue();
            songList.forEach(System.out::println);
           thread.notify();

        });



        try {

            thread.start();
            thread.wait();
            createNewWidnowWithProgressBarofAddingMp3Song(progressTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return songList;
    }

    private void createFileListFromComplexedDirectories(File dir, List<File> fileList) throws FileNotFoundException {

        if (dir.listFiles() != null) {
            File[] temporaryFilesTable = dir.listFiles();

            for (File file : temporaryFilesTable) {


                try {

                    if (file.isDirectory()) {

                        createFileListFromComplexedDirectories(file, fileList);// wywłujemy jeszcze raz rekurencyjnie dla kazdego podfolderu

                    } else if (!file.isDirectory()) {
                        fileList.add(file);
                    }


                } catch (FileNotFoundException e) {


                    continue;

                }
            }


        }
    }


    private void createNewWidnowWithProgressBarofAddingMp3Song(Task<List<Mp3Song>> task
    ) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/fxml/loadingFilePane.fxml"));

        Parent parent = null;
        try {
            parent = loader.load();
            loadingFileController = loader.getController(); // zwraca obiekt kontrolera

            System.out.println(loadingFileController);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Wczytywanie plików");

        ProgressBar progressBar = loadingFileController.getProgressBar();
        Label filePathLabel = loadingFileController.getFilePathLabel();

        progressBar.progressProperty().bind(task.progressProperty());
        filePathLabel.textProperty().bind(task.messageProperty());


    }
}