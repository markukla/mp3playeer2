package pl.javastart.Mp3Player.mp3;

import javafx.scene.control.CheckBox;

import java.io.Serializable;

public class Mp3Song implements Serializable {
    public static final Long serialVersionUID = 74822749824289L;

    private String title;
    private String author;
    private String album;
    private String filePath;
    private transient CheckBox checkBox;


    public Mp3Song(String title, String author, String album, String filePath) {
        this.title = title;
        this.author = author;
        this.album = album;
        this.filePath = filePath;

    }

    public Mp3Song(String title, String author, String album, String filePath, CheckBox checkBox) {
        this.title = title;
        this.author = author;
        this.album = album;
        this.filePath = filePath;
        this.checkBox = checkBox;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public String toString() {
        return "Mp3Song{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", album='" + album + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
