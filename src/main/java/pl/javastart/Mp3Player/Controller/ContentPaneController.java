package pl.javastart.Mp3Player.Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import pl.javastart.Mp3Player.mp3.Mp3Song;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


public class ContentPaneController implements Serializable {
    public static final Long serialVesrionUID = 7429749828424L;

    public static final String TITLE_COLUMN = "Tytuł";
    public static final String AUTHOR_COLUMN = "Autor";
    public static final String ALBUM_COLUMN = "Album";
    public static final String CHECKBOX_COLUMN = "checkBox";


    @FXML
    private TableView<Mp3Song> contentTable; // parametryzujemy obiekt TableViev typem utworzonej przez nas klasy Mp3Song, bo te obikety będzie zawierać tabela

    public TableView<Mp3Song> getContentTable() {
        return contentTable;
    }


    public void initialize() {
        System.out.println("Content controller created");  // metoda initalize jest automatycznie wywoływana jeśli
        // ContentPaneController jesr powiązany z MainController przez fx:include, fx:id. dzieje się tak dlatego że
        // w klasie main odwołujemy się przy tworzeniu sceny do mainPane.fxml, który z kolei wiązę się z MainController

        configureTableColumns();


    }


    private void configureTableColumns() {

        // tworzymy 3 kolumny rozprezentowane przez obiekty klasy TableColumn<T,R>
        TableColumn<Mp3Song, CheckBox> checkBoxTableColumn = new TableColumn<>(CHECKBOX_COLUMN);
        checkBoxTableColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        TableColumn<Mp3Song, String> titleColumn = new TableColumn<>(TITLE_COLUMN); //parametry okreslają z jakiej klasy pobieramy dane=Mp3Song i w jakiej formie wyświetlamy= String
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));// określamy jakie pole z klasy Mp3Song będzie wyświetlane w komórkach tej kolumny, nazwę pola podajemu w konstruktorze obiektu PropertyValueFactory

        TableColumn<Mp3Song, String> authorColumn = new TableColumn<>(AUTHOR_COLUMN);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Mp3Song, String> albumColumn = new TableColumn<>(ALBUM_COLUMN);
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        //utworzone kolumny dodajemey do tabeli repreznetowanej przez nasz wcześniej utworzony obiekt contentTable odpowiadjący kontrolce TableViev
        contentTable.getColumns().add(checkBoxTableColumn);
        contentTable.getColumns().add(titleColumn); // logiczniej byłoby setColumn, ale nie ma takiej metody więc trzeba użyć zestawu tych dwóch metod
        contentTable.getColumns().add(authorColumn);
        contentTable.getColumns().add(albumColumn);
        contentTable.getColumns();
    }

    private void createTestData() {

        //TableView przechowuje dane w kolekcji typu ObservableList<>. Wszelkie zmiany dokonane w tej kolekcji będą automatycznie odzwierciedlone w tabeli, którą widzi użytkownik, dlatego observable=sledząca tablea
        ObservableList<Mp3Song> items = contentTable.getItems();
        Mp3Song mp3SongFromPath = createMp3SongFromPath("target/Lady Gaga, Bradley Cooper - Shallow (A Star Is Born Soundtrack).mp3");
        items.add(mp3SongFromPath);


    }

    private Mp3Song createMp3SongFromPath(String filePath) {
        File file = new File(filePath);

        try {
            String absolutePath = file.getAbsolutePath();
            MP3File testMp3 = new MP3File(file);
            String title = testMp3.getID3v2Tag().getSongTitle();
            String author = testMp3.getID3v2Tag().getLeadArtist();
            String album = testMp3.getID3v2Tag().getAlbumTitle();

            Mp3Song testSong = new Mp3Song(title, author, album, absolutePath);
            return testSong;
        } catch (IOException | TagException e) {
            e.printStackTrace();
            return null; // ignore
        }

    }


}