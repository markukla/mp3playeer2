package pl.javastart.Mp3Player.player;

import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pl.javastart.Mp3Player.mp3.Mp3Song;

import java.io.File;
import java.io.Serializable;

public class Mp3Player implements Serializable {
    public static final Long serialVersionUID = 84627847294243L;
    private ObservableList<Mp3Song> songList;


    private Media media;
    private MediaPlayer mediaPlayer;

    public Mp3Player(ObservableList<Mp3Song> songList) {
        this.songList = songList;

    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void play() {
        if ((mediaPlayer != null && (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) || (mediaPlayer.getStatus() == MediaPlayer.Status.READY))) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public double getLoadedSongLength() {
        if (media != null) {// piosenka jest załadowana, zabezpieczenie przez NPE, żeby nie odwołwać się do nulla
            return media.getDuration().toSeconds();
        } else {
            return 0;
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }

    }

    public void loadSong(int index) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) { // chcąc załadować nową piosnekę trzeba zatrzymać aktualnie uruchomioną
            mediaPlayer.stop();
// zabezpieczenie przed nulem jest kluczowe, bo gdy uruchamiamy pierwszą piosenkę nie ma jeszcze obiektu mediaPlayer=null jest tworzony w dalszej częsci tej metody, więc powyższa instrukcja powoduje NPE

        }
        Mp3Song mp3s = songList.get(index);  // metoda get z metod dostępnych dla klasy listy
        media = new Media(new File(mp3s.getFilePath()).toURI().toString()); //media wymaga obiektu file, tworzymy go na bazie ścieżki pobranej z obkektu Mp3Song
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.statusProperty().addListener((observable, oldStatus, newStatus) -> {
            if (newStatus == MediaPlayer.Status.READY) {
                mediaPlayer.setAutoPlay(true);
            }
        });
    }
}