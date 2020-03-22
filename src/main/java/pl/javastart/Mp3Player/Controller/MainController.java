package pl.javastart.Mp3Player.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import pl.javastart.Mp3Player.mp3.Mp3Parser;
import pl.javastart.Mp3Player.mp3.Mp3Song;
import pl.javastart.Mp3Player.player.Mp3Player;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainController implements Serializable {
    public static final Long SerialVersionUID = 42478274858935L;


    // tworzymy ręcznie zmienne odpowiadające fx:id z mainPane.fxml + dodanie słowa Controller
    // zmienne te bedą referencją na obiekty kontrolerów, które zostaną automatycznie utworzone przez FXMLLoader

    @FXML
    private MenuPaneController menuPaneController;  // gdy klasa z dużych liter inteli automatycznie generuje prawidłową nazwę zmiennej

    @FXML
    private ControlPaneController controlPaneController;

    @FXML
    private ContentPaneController contentPaneController;


    public Mp3Player getPlayer() {
        return player;
    }

    private Mp3Player player;
    private Mp3Parser parser;
    private LocalTime timeLeftToEndOfSong; // zadeklarowane jako globalne zeby uniknąć problemów z efektywnie finalnymi zmiennymi lokanymi wewnątrz wyrażeń lambda
    public int songLength = 0;                      // zadeklarowane jako globalne zeby uniknąć problemów z efektywnie finalnymi zmiennymi lokanymi wewnątrz wyrażeń lambda
    int minusSecondParameterToReverseTimeDisplay = 0;
    private LocalTime durationOfSong;
    @FXML
    private PlayListNameController playListNameController;
    private int secondsLeftToendOfSong;
    private List<String> songNamesList;
    private List<Mp3Song> playList;
    @FXML
    private LoadingFileController loadingFileController;

    public LoadingFileController getLoadingFileController() {
        return loadingFileController;
    }

    public MenuPaneController getMenuPaneController() {
        return menuPaneController;
    }

    public ControlPaneController getControlPaneController() {
        return controlPaneController;
    }

    public ContentPaneController getContentPaneController() {
        return contentPaneController;
    }

    public IconfiedController getIconfiedController() {
        return iconfiedController;
    }

    public void setIconfiedController(IconfiedController iconfiedController) {
        this.iconfiedController = iconfiedController;
    }

    @FXML
    private IconfiedController iconfiedController;






    /*We wczesnych wersjach Javy FX klasa kontrolera powinna implementować interfejs Initializable,
     który definiuje jedną metodę initialize(). Jest ona wywoływana po utworzeniu obiektu kontrolera
     i wstrzyknięciu do niego obiektów odpowiadających kontrolkom z powiązanego z nim pliku fxml.
     W nowych wersjach JavyFX implementacja interfejsu Initializable nie jest już wymagana. Wystarczy,
    że w klasie kontrolera zdefiniujemy metodę o sygnaturze public void initialize(), a zostanie ona wywołana automatycznie.
    W tej metodzie definiujemy instrukcje zmieniające stan obiektów kontrolek*/

    public void initialize() throws IOException, TagException {

        System.out.println("Main controller created");
        System.out.println(contentPaneController);
        System.out.println(controlPaneController);
        System.out.println(menuPaneController);
        ToggleButton playButton = controlPaneController.getPlayButton();
        Button nextButton = controlPaneController.getNextButton();
        Button previousButton = controlPaneController.getPreviousButton();
        ToggleButton replayPlayList = controlPaneController.getReplayPlaylistButton();
        ToggleButton playAllPlaylist = controlPaneController.getPlayAllSongsButton();
        Slider progressSlider = controlPaneController.getProgressSlider();
        ToggleButton loopSong = controlPaneController.getLoopSong();
        ToggleButton randomButton = controlPaneController.getRandom();
        createPlayer();
        configureTabelClick();

        configureButtons(playButton, nextButton, previousButton);
        configurePlayAllPlaylistandRepeatPlaylistAndLoopSongAndRandomButtons(playButton, replayPlayList, playAllPlaylist, loopSong, randomButton, progressSlider);


        configureMenu();
        configureMouseEnteredOnControls();
        configureVolumeSlider();


    }

    private void createPlayer() {
        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
        player = new Mp3Player(items);

    }

    private void configureTabelClick() {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable(); // przypisujemy do innej nowej referencji contentTable obiekkt contentTable który zostanie utworzony wraz z panel contentPaneController przez FXMl loader- nie tworzymy innego obiektu będzie tyko jeden taki obiekt
        int primaryIndex = 0;
        if (contentTable.getItems().size() > 0) {
            contentTable.getSelectionModel().select(primaryIndex);
        }
        contentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                int selectedIndex = contentTable.getSelectionModel().getSelectedIndex();
                playSelectedSong(selectedIndex);

            }
        });


    }


    public void playSelectedSong(int selectedIndex) {
        player.loadSong(selectedIndex);

        configureVolume();
        configureProgressBar();
        controlPaneController.getPlayButton().setSelected(true);  // ta metoda powoduje wywołanie metody play z mklasy MP3 player, bo na przycisku jest ustawiony słuchacz (metoda configureButtons) ( w metodzie configureButons) i jeśli jest selected to odpalana jest metoda  mediaPlayer. play

    }

    private void configureVolume() {
        Slider volumeSlider = controlPaneController.getVolumeSlider();
        volumeSlider.valueProperty().unbind(); // z czym był orginalnie zbidnowany ? że teraz nie chcemy tego bindingu ?
        volumeSlider.setMax(100);
        player.getMediaPlayer().volumeProperty().bindBidirectional(volumeSlider.valueProperty()); // podpinamy slider żeby nasledzał warotść z mediaPlayera obiektu player(klasy Mp3Player)


    }

    private void configureVolumeSlider() {
        Slider volumeSlider = controlPaneController.getVolumeSlider();
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);

        Label volumeValue = controlPaneController.getVolumeLevel();
        volumeValue.setText("" + 100);

        volumeSlider.valueProperty().addListener((arg, old, newValue) -> {
            double volumeLevel = newValue.doubleValue();
            long volumeLevelRound = Math.round(volumeLevel);
            volumeValue.setText("" + volumeLevelRound);
            ImageView volumeIcon = controlPaneController.getVolumeIcon();
            if (newValue.intValue() == 0) {
                Image noVolume = new Image("file:/C:/Users/Marcin/IdeaProjects/mp3player/src/main/resources/img/noVolume.png");

                volumeIcon.setImage(noVolume);
            } else {
                Image volume = new Image("file:/C:/Users/Marcin/IdeaProjects/mp3player/src/main/resources/img/glosnik.jpeg");
                volumeIcon.setImage(volume);
            }
        });
    }

    public void configureProgressBar() {
        Slider progressSlider = controlPaneController.getProgressSlider();
        ToggleButton randomButton = controlPaneController.getRandom();
        ToggleButton timeDisplayRevers = controlPaneController.getReverseTimeDisplay();
        Label timeLabelleft = controlPaneController.getCurrentTimeLabel();
        Label timeLabelRight = controlPaneController.getSongLengthLabel();


        player.getMediaPlayer().setOnReady(() -> {
            progressSlider.setMax(player.getLoadedSongLength());
            timeLabelRight.setText("");

            songLength = (int) player.getLoadedSongLength();


            LocalTime songLengthTime = LocalTime.of(0, 0, 0).plusSeconds(songLength);

            timeLabelRight.setText(timeLabelRight.getText() + songLengthTime);


        }); //ustawinie maksymalnej długości suwaka, dla każdej wczytanej piosneki maksymalny czas jest inny
        player.getMediaPlayer().currentTimeProperty().addListener((arg, oldValue, newValue) -> {
            progressSlider.valueProperty().setValue(newValue.toSeconds());  //zmiana czasu w odtwarzaczu automatycznie będzie aktualizowała suwak
        });
        configureMouseActionOnProgressSlider("aktualny postep piosenki", player);
        progressSlider.valueProperty().addListener((arg, oldValue, newValue) -> {
            if (progressSlider.isValueChanging()) {
                player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue())); //seek ustawia postep piosenki zgodnie z postepem suwaka, przelicza sobie procenty czy jakoś tak
            }
        });


        configureTimeDisplay(timeDisplayRevers, timeLabelleft);


    }


    public void configureTimeDisplay(ToggleButton timeDisplayRevers, Label timeLabelleft) {
        player.getMediaPlayer().currentTimeProperty().addListener((arg, oldValue, newValue) -> {


            timeLabelleft.setText("");
            int hour = 0;
            int minut = 0;
            int second = 0;
            int currentSongSecond = (int) newValue.toSeconds();
            String currenSecondString = "0" + currentSongSecond;

            int currentSongMinut = (int) newValue.toMinutes();
            String currentMinutString = "0" + currentSongMinut;
            String minutSecondToTimeFormater = currentMinutString + "-" + currenSecondString;
            int secondInHouer = 60 * 60;

            LocalTime curentSongTime = LocalTime.of(hour, minut, second);

            durationOfSong = LocalTime.of(0, 0, 0).plusSeconds(songLength);


            DateTimeFormatter minustAndSeconds = DateTimeFormatter.ofPattern("mm-ss");
            if (timeDisplayRevers.isSelected() == false) {
                curentSongTime = curentSongTime.plusSeconds(currentSongSecond);
                timeLabelleft.setText(timeLabelleft.getText() + curentSongTime);
            } else if (timeDisplayRevers.isSelected() == true) {

                timeLabelleft.setText("");

                System.out.println("Duration" + durationOfSong);
                secondsLeftToendOfSong = songLength - currentSongSecond;
                System.out.println("sekundy do końca piosenki" + secondsLeftToendOfSong);

                timeLeftToEndOfSong = curentSongTime.plusSeconds(secondsLeftToendOfSong);
                // timeLeftToEndOfSong-przypisanie wartości koniecznie poza ifem, w przeciwnym wypadku dobra wartośc będzie wyświetlana tylko wtedy gdy będzie dokładny moment zwiększenia o sekundę
                timeLabelleft.setText("" + timeLeftToEndOfSong);
            }

        });

    }

    public void configureButtons(ToggleButton playButton, Button nextButton, Button previousButton) {

        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();

        TextArea messsageWindow = controlPaneController.getMessageTextArea();

        playButton.setOnAction(actionEvent -> {
            if (playButton.isSelected()) {
                if (contentTable.getItems().size() > 0) {
                    if (!contentTable.getSelectionModel().isEmpty()) {
                        player.play();
                    } else if (contentTable.getSelectionModel().isEmpty()) {
                        contentTable.getSelectionModel().select(0);
                        playSelectedSong(0);
                    }

                } else {
                    messsageWindow.setText("aby włączyć odtwarzanie najpierw dodaj piosenki do tabeli");
                    playButton.setSelected(false);
                }


            } else {
                player.stop();// teżwłasna metoda
            }

        });

        nextButton.setOnAction(actionEvent -> {
            int maxIndex = contentTable.getItems().size() - 1;


            if (contentTable.getItems().size() > 1 && contentTable.getSelectionModel().getSelectedIndex() < maxIndex) {
                //selectionModel to obiekt klasy obrazującej selekcje elementów w tabeli a selectedIndex to jego pole
                contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1); //ustawiamy index na +1

                playSelectedSong(contentTable.getSelectionModel().getSelectedIndex() + 1); // pobieramy index zaznaczonj komórki uprzednio zwiększona o jeden
            } else if (contentTable.getItems().size() > 1 && contentTable.getSelectionModel().getSelectedIndex() >= maxIndex) {
                messsageWindow.setText("aktulnie odtwarzana piosenka jest ostatnia na playLiście");
            } else if (contentTable.getItems().size() < 1) {
                messsageWindow.setText("nie można przejść do następnej piosenki, gdyż playLista jest pusta");
            }


        });

        previousButton.setOnAction(actionEvent -> {
            int minIndex = 1;


            if (contentTable.getItems().size() > 1 && contentTable.getSelectionModel().getSelectedIndex() > minIndex) {
                //selectionModel to obiekt klasy obrazującej selekcje elementów w tabeli a selectedIndex to jego pole
                contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() - 1); //ustawiamy index na +1

                playSelectedSong(contentTable.getSelectionModel().getSelectedIndex() - 1); // pobieramy index zaznaczonj komórki uprzednio zwiększona o jeden
            } else if (contentTable.getItems().size() > 1 && contentTable.getSelectionModel().getSelectedIndex() < minIndex) {
                messsageWindow.setText("aktulnie odtwarzana piosenka jest pierwszą na playLiście ");
            } else if (contentTable.getItems().size() < 1) {
                messsageWindow.setText("nie można przejść do poprzedniej piosenki, gdyż playLista jest pusta");
            }
        });


    }

    public void configurePlayAllPlaylistandRepeatPlaylistAndLoopSongAndRandomButtons(ToggleButton playButton, ToggleButton replayPlayList, ToggleButton playAllPlaylist, ToggleButton loopSong, ToggleButton randomButton, Slider progressSlider) {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();

        TextArea messageWindow = controlPaneController.getMessageTextArea();

        // dodanie nasłuchu zdarzeń i selected property jest konieczne żeby program obserwował zmiane warunków w trakcie jego działania
        //
        loopSong.selectedProperty().addListener((arg, oldValue, newValue) -> {
            if (newValue == true) {
                replayPlayList.setSelected(false);
                playAllPlaylist.setSelected(false);
                randomButton.setSelected(false);
            }

        });
        playAllPlaylist.selectedProperty().addListener((arg, oldValue, newValue) -> {
            if (newValue == true) {
                loopSong.setSelected(false);
                randomButton.setSelected(false);
                if (contentTable.getSelectionModel().isEmpty()) {
                    contentTable.getSelectionModel().select(0);
                    if (contentTable.getItems().size() > 0) {
                        playSelectedSong(0);
                    } else {
                        messageWindow.setText("aby uruchomić automatyczne odtwarzanie playlisty musisz najpierw załadować playlistę");
                    }
                }
            }
        });
        replayPlayList.selectedProperty().addListener((arg, oldValue, newValue) -> {
            if (newValue == true) {
                loopSong.setSelected(false);
                randomButton.setSelected(false);
            }
        });
        randomButton.selectedProperty().addListener((arg, oldValue, newValue) -> {
            if (newValue == true) {
                loopSong.setSelected(false);
                playAllPlaylist.setSelected(false);
                replayPlayList.setSelected(false);


                if (contentTable.getItems().size() > 0) {

                    if (contentTable.getSelectionModel().isEmpty()) {

                        int maxindex = contentTable.getItems().size() - 1;
                        Random random = new Random();
                        int index = random.nextInt(maxindex);
                        contentTable.getSelectionModel().select(index);
                        playSelectedSong(index);
                    }
                } else {
                    messageWindow.setText("aby uruchomić automatyczne losowe odtwarzanie playlisty musisz najpierw załadować playlistę");
                }
            }
        });

        progressSlider.valueProperty().addListener((arg, oldValue, newValue) -> {  // ustawia co się stanie po zakończeniu odtwarzania danej piosenki
            int index = contentTable.getSelectionModel().getSelectedIndex();
            if ((int) newValue.doubleValue() == (int) progressSlider.getMax()) {

                if (index < contentTable.getItems().size() - 1) {

                    if (playAllPlaylist.isSelected()) {
                        // Toogle button nie wymaga stosowania metody setOnaction, wystarczy stosowwać isSelected, co znacznie
                        //upraszcza sprawe, bo nie trzeba stosować dodatkowych wyrażeń lambda lub klas anonimowych,
                        // wniosek jest taki żeby dokładnie sprawdzać jakie możliwości daje każda klasa

                        playButton.setSelected(true);
                        contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1);
                        index = contentTable.getSelectionModel().getSelectedIndex();
                        System.out.println("indeks przed metodą play selected song z playAllplaylist=  " + index);
                        playSelectedSong(index);


                    } else {

                        player.stop();
                        playButton.setSelected(false);
                    }

                } else if (index == contentTable.getItems().size() - 1) {

                    if (replayPlayList.isSelected()) {
                        index = 0;
                        contentTable.getSelectionModel().select(index);
                        playSelectedSong(index);


                    } else {
                        player.stop();
                        playButton.setSelected(false);
                    }


                }
                if (loopSong.isSelected()) {

                    playSelectedSong(index);
                }
                if (randomButton.isSelected()) {
                    Random random = new Random();
                    if (contentTable.getItems().size() > 0) {
                        int maxIndex = contentTable.getItems().size() - 1;

                        int indexr = random.nextInt(maxIndex);
                        contentTable.getSelectionModel().select(indexr);
                        playSelectedSong(indexr);
                    } else {
                        messageWindow.setText("nie można wylosować piosenki, bo playLista jest Pusta");
                    }
                }

            }
        });

    }

    private Mp3Song createMp3SongFromPath() throws IOException, TagException {
        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
        File file = new File("target/Lady Gaga, Bradley Cooper - Shallow (A Star Is Born Soundtrack).mp3");
        MP3File mp3File = new MP3File(file);
        AbstractID3v2 id3v2 = mp3File.getID3v2Tag();
        String title = id3v2.getSongTitle();
        String author = id3v2.getLeadArtist();
        String album = id3v2.getAlbumTitle();
        String filePath = file.getAbsolutePath();
        Mp3Song testMp3 = new Mp3Song(title, author, album, filePath); // metoda zwraca za każdym razem nowy obiekt !!!!
        return testMp3;

    }


    private void addTestMp3Song() throws IOException, TagException {
        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
        // do listy zawierającej obiekty Mp3Song dodajemy obiekty Mp3Song
        items.add(createMp3SongFromPath());
        items.add(createMp3SongFromPath());
        items.add(createMp3SongFromPath());

    }


    private void configureMenu() {
parser=new Mp3Parser();

        addSongToplaylist(parser);
        removeSelectedSongsFromPlaylist();

        addDirectoryToPlaylist(parser);
        savePlaylist();
        openPlaylist();
        configureSearchTextField();
    }

    private void addSongToplaylist(Mp3Parser parser) {

        EventHandler<ActionEvent> addSongToplaylist = (actionEvent) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*.mp3"));
            File file = fileChooser.showOpenDialog(new Stage()); // obikt file jest wybierany przez użytkownika- tworzony przez obekt fileChooser
            try {
                contentPaneController.getContentTable().getItems().add(parser.createMp3Song(file));
            } catch (Exception e) {
                e.printStackTrace();

            }
        };

        MenuItem fileMenultem = menuPaneController.getFileMenuItem();
        fileMenultem.setOnAction(addSongToplaylist);
        Button addSongButton = menuPaneController.getOpenMp3File();
        addSongButton.setOnAction(addSongToplaylist);


    }

    private void addDirectoryToPlaylist(Mp3Parser parser) {
        EventHandler<ActionEvent> addDirectoryActionEvent = (actionEvent) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            TextArea messageTextArea = controlPaneController.getMessageTextArea();

            File file = directoryChooser.showDialog(new Stage());

            List<Mp3Song> songList=null;

    try {
        songList = parser.createMp3List(file);



    } catch (IOException e) {
        e.printStackTrace();
    } catch (TagException e) {
        e.printStackTrace();


    }


    try{
        for (Mp3Song mp3s : songList
        ) {

            contentPaneController.getContentTable().getItems().add(mp3s);

            messageTextArea.setText("dodano folder piosenek do playlisty");
        }
    }

    catch (Exception e) {
        e.printStackTrace();
        messageTextArea.setText("nie udało sie wczytać piosenek z folderu");

    }

        };
        MenuItem openDirectory = menuPaneController.getDirMenuItem();
        Button openDirectoryButton = menuPaneController.getAddDirectory();
        openDirectory.setOnAction(addDirectoryActionEvent);
        openDirectoryButton.setOnAction(addDirectoryActionEvent);


    }

    private void openPlaylist() {
        EventHandler<ActionEvent> openPlaylistActionEvent = (actionEvent) -> {
            player.stop();
            TableView<Mp3Song> contentTable = contentPaneController.getContentTable();

            contentTable.getItems().clear();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("obj", "*.obj"));
            File file = fileChooser.showOpenDialog(new Stage());
            String fileName2 = file.getAbsolutePath();  // jeśli pik jest zapisany nie w folderze projektu, to żeby go poprawnie odczytać zamiast nazwy podajemy ścieżkę absolutną

            TextArea messageTextArea = controlPaneController.getMessageTextArea();
            try (
                    var fis = new FileInputStream(fileName2);
                    var ois = new ObjectInputStream(fis);
            ) {

                Mp3Song[] loadedPlaylistTable = (Mp3Song[]) ois.readObject();
                for (int i = 0; i < loadedPlaylistTable.length; i++) {
                    System.out.println(loadedPlaylistTable[i]);
                    loadedPlaylistTable[i].setCheckBox(new CheckBox());
                    contentPaneController.getContentTable().getItems().add(loadedPlaylistTable[i]);
                }

                messageTextArea.setText("wczytano Playliste");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                messageTextArea.setText("nie udało sie wczytać playlisty");
            } catch (IOException e) {
                e.printStackTrace();
                messageTextArea.setText("nie udało sie wczytać playlisty");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                messageTextArea.setText("nie udało sie wczytać playlisty");
            }

        };

        MenuItem openPlaylist = menuPaneController.getOpenPlayListMenuItem();
        Button openPlaylistButton = menuPaneController.getOpenPlaylist();
        openPlaylist.setOnAction(openPlaylistActionEvent);
        openPlaylistButton.setOnAction(openPlaylistActionEvent);
    }

    private void savePlaylist() {
        EventHandler<ActionEvent> savePlaylistActionEvent = (actionEvent) -> {


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/playListName.fxml"));
            Parent parent = null;
            try {
                parent = loader.load();
                playListNameController = loader.getController(); // zwraca obiekt kontrolera

                System.out.println("playListNameController=  " + playListNameController);
                playListNameController.setMainController(this); //nie potrzebna ta linijka
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Wprowadż nazwę playlisty");

            Button zatwierdz = playListNameController.getZastosujButton();
            zatwierdz.setOnAction(actionEvent1 -> {

                String fileName = playListNameController.getPlayListNameTextField().getText();
                stage.close();
                DirectoryChooser directoryChooser = new DirectoryChooser();

                directoryChooser.setTitle("Wybierz folder w którym chcesz zapisać playliste");

                File savedPlalistDirectory = directoryChooser.showDialog(new Stage());
                String fileNameWithAbsoluthPath = savedPlalistDirectory.getAbsolutePath().toString() + "\\" + fileName + ".obj";
                System.out.println("fileNameWithAbsoluthPath=  " + fileNameWithAbsoluthPath);
                TextArea messageTextArea = controlPaneController.getMessageTextArea();
                try (
                        var fs = new FileOutputStream(fileNameWithAbsoluthPath);
                        var os = new ObjectOutputStream(fs);
                ) {


                    TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
                    List<Mp3Song> playList = contentTable.getItems();  // utworzenie listy kluczowe w tym miejscu, czyli po naciśnieciu przycisku, inaczej będzie pusta !!!, bo kod wywyła się za wcześnie, a potem nie powtórzy po naciśnieciu przycisku
                    Mp3Song[] playListTable = new Mp3Song[playList.size()];
                    //TableViev nie implementuje serializable,
                    // tak samo lista utworzona poprzez getItmes, dlatego tworze pomocniczą tabelę
                    // zeby zapidać obiekt do plik wszystkie obiekty, które są jego polami muszą implementować serializable
                    for (int i = 0; i < playListTable.length; i++) {
                        playListTable[i] = contentPaneController.getContentTable().getItems().get(i);
                        System.out.println(playListTable[i]);

                    }

                    os.writeObject(playListTable);
                    System.out.println("zapisano playliste");
                    messageTextArea.setText("zapisano playliste");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    messageTextArea.setText("nie udało sie zapisać playlisty");
                } catch (IOException e) {
                    e.printStackTrace();
                    messageTextArea.setText("nie udało się zapisać playlisty");
                }
            });
        };
        MenuItem savePlaylist = menuPaneController.getSavePlayListMenuItem();
        savePlaylist.setOnAction(savePlaylistActionEvent);
        Button savePlaylistButton = menuPaneController.getSavePlaylist();
        savePlaylistButton.setOnAction(savePlaylistActionEvent);


    }


    private void removeSelectedSongsFromPlaylist() {
        TableView<Mp3Song> contentable = contentPaneController.getContentTable();
        MenuItem removeSong = menuPaneController.getRemoveSongFromPlaylist();
        TextArea messageTextArea = controlPaneController.getMessageTextArea();
        playList = contentable.getItems();
        List<Mp3Song> songsLeftAfterRemoving = new ArrayList<>();
        EventHandler<ActionEvent> removeSongActionEvent = actionEvent -> {
            List<Mp3Song> playLists = new ArrayList<>();

            try {
                playLists = playList.stream()  // pomocnicza lista playLists jest po to bo nie da sie wykonać metody collect do ObservableList(jest to typ abstrakcyjny) a zwykła lista nie umożliwia obserwacji i automatycznej aktualizacji, i nie będzie działać
                        .filter(x -> (x.getCheckBox().isSelected() == false))
                        .peek(System.out::println)
                        .collect(Collectors.toList());
                playList.clear();
                playList.addAll(playLists);
                messageTextArea.setText("usunięto wybrane piosenki z playlisty");
            } catch (Exception e) {
                e.printStackTrace();
                messageTextArea.setText("nie udało sie usunąć piosenek z playlisty");
            }
/*     rozwiązanie tradycyjne z petlą for
       for (int i = 0; i <playList.size() ; i++) {

                  if(playList.get(i).getCheckBox().isSelected()==true){
                      playList.remove(i);
                      i--;// trzeba o tym pamiętać bo inaczej po usunięciu pierwszego elementu petla opuści ten element który wskoczy na jego miejsce itd, dlatego lepsze są strumienie bo nie trzeba o tym myśleć
                  }

            }
*/
        };
        removeSong.setOnAction(removeSongActionEvent);
        Button removeSongButton = menuPaneController.getRemoveSongs();
        removeSongButton.setOnAction(removeSongActionEvent);
    }

    private void configureSearchTextField() {
        TextField search = menuPaneController.getSearch();
        search.setText("Podaj tytuł piosenki, którą chcesz wyszukać");
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();

        contentTable.getItems().addListener(new ListChangeListener<Mp3Song>() {
            @Override
            public void onChanged(Change<? extends Mp3Song> change) {
                if (contentTable.getItems() != null) {
                    List<Mp3Song> songList = contentTable.getItems();
                    songNamesList = new ArrayList<>();

                    for (int i = 0; i < songList.size(); i++) {
                        songNamesList.add(songList.get(i).getTitle());
                    }
                }
            }
        });


        search.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            String songnameEnteredByUser = search.getText();
            TextArea mesageWindow = controlPaneController.getMessageTextArea();
            if (songNamesList != null) {

                for (int i = 0; i < songNamesList.size(); i++) {


                    if (songnameEnteredByUser.equalsIgnoreCase(songNamesList.get(i))) {
                        contentTable.getSelectionModel().select(i);
                        playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
                        mesageWindow.setText("Odtwarzanie piosenki :  " + songNamesList.get(i));
                        search.setText("Podaj tytuł piosenki, którą chcesz wyszukać");
                    }

                }
            } else {

                mesageWindow.setText("nie można wyszukać piosenki bo playlista jest pusta");
                search.setText("Podaj tytuł piosenki, którą chcesz wyszukać");
            }
        });

    }

    private void configureMouseEnteredOnControls() {
        TextArea messageWindow = controlPaneController.getMessageTextArea();
        ToggleButton playAllPlaylistButton = controlPaneController.getPlayAllSongsButton();
        ToggleButton repeatPlaylist = controlPaneController.getReplayPlaylistButton();
        ToggleButton playButton = controlPaneController.getPlayButton();
        ToggleButton loopSongButton = controlPaneController.getLoopSong();
        ToggleButton changeTimeDisplay = controlPaneController.getReverseTimeDisplay();
        ToggleButton randomButton = controlPaneController.getRandom();
        Button previousButton = controlPaneController.getPreviousButton();
        Button nextButton = controlPaneController.getNextButton();
        Slider progresSlider = controlPaneController.getProgressSlider();
        Slider volumeSlider = controlPaneController.getVolumeSlider();
        Button addSongButton = menuPaneController.getOpenMp3File();
        Button openPlaylist = menuPaneController.getOpenPlaylist();
        Button addDirectory = menuPaneController.getAddDirectory();
        Button savePlaylist = menuPaneController.getSavePlaylist();
        Button removeSongs = menuPaneController.getRemoveSongs();
        configureMouseEnteredOnButon(addSongButton, "naciśńij aby dodać piosenkę do playlisty");
        configureMouseEnteredOnButon(openPlaylist, "naciśnij aby otworzyć zapisaną playlistę");
        configureMouseEnteredOnButon(addDirectory, "naciśnij żeby wyszukać pliku z mp3 z folderu i dodać je do playlisty");
        configureMouseEnteredOnButon(savePlaylist, "naciśnij aby zapisać aktualną playlistę");
        configureMouseEnteredOnButon(removeSongs, "naciśnij aby usunąć zaznaczone piosenki z playlisty");

        configureMouseEnteredOnToogleButton(playAllPlaylistButton, "naciśnij aby zatrzymać automatyczne odtwarzanie playlisty", "naciśnij aby włączyć automatyczne odtwarzanie playlisty", "naciśnij aby odtworzyć pierwszy utwór na playliście", "aby włączyć automatyczne odtwarzanie playlisty najpierw ją załaduj");
        configureMouseEnteredOnToogleButton(repeatPlaylist, "naciśnij aby zatrzymać automatyczne powtarzanie playlisty", "naciśnij aby włączyć automatyczne powtarzanie plalisty (zapętl playlistę)");
        configureMouseEnteredOnToogleButton(playButton, "naciśnij aby zatrzymać odtwarzanie utworu", "naciśnij żeby odtworzyć utwór", "naciśnij aby odtworzyć pierwszy utwór na playliście", "aby właczyć odtwarzanie najpierw załadujPlalistę");
        configureMouseEnteredOnToogleButton(loopSongButton, "naciśnij żeby wyłączyć zapętlenie piosenki", "naciśnij żeby włączyć zapętlenie piosenki");
        configureMouseEnteredOnButon(previousButton, "naciśnij żeby odtworzyć poprzednią piosenkę");
        configureMouseEnteredOnButon(nextButton, "naciśnij żeby odtworzyć następną piosenkę");
        configureMouseEnteredOnToogleButton(changeTimeDisplay, " naciśnij aby wyświetlić aktualny postęp piosenki", "naciśnij aby wyświetlić czas pozostały do końca piosenki", "naciśnij aby wyświetlić czas pozostały do końca piosenki", "załaduj playlsitę aby zobaczyć możliwe opcje wyświetlania czasu trwania piosenki");
        configureMouseEnteredOnToogleButton(randomButton, "naciśnij żeby wyłączyć losowe odtwarzanie piosenek", "naciśnij żeby włączyć losowe odtwarzanie piosenek", "naciśnij żeby włączyć losowe odtwarzanie piosenek", "najpierw załaduj playliste, żeby umożliwić jej losowe odtwarzanie");
    }

    private void configureMouseEnteredOnToogleButton(ToggleButton toggleButton, String mouseEnteredMessageSelected, String mouseEnteredMessageNonSelected) {
        TextArea messageWindow = controlPaneController.getMessageTextArea();
        toggleButton.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {

            if (!toggleButton.isSelected()) {
                messageWindow.setText(mouseEnteredMessageNonSelected);
            } else if (toggleButton.isSelected()) {
                messageWindow.setText(mouseEnteredMessageSelected);
            }
        });
        toggleButton.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            messageWindow.clear();
        });
    }

    private void configureMouseEnteredOnToogleButton(ToggleButton toggleButton, String mouseEnteredMessageSelected, String mouseEnteredMessageNonSelected, String mouseEnteredMessageNonSelecteButtonAndNoSongSelected, String mouseEnteredMessageNonSelectedPlaylistEmpty) {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        TextArea messageWindow = controlPaneController.getMessageTextArea();
        toggleButton.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            if (!contentTable.getSelectionModel().isEmpty()) {
                if (!toggleButton.isSelected()) {
                    messageWindow.setText(mouseEnteredMessageNonSelected);
                } else if (toggleButton.isSelected()) {
                    messageWindow.setText(mouseEnteredMessageSelected);
                }
            } else if (contentTable.getSelectionModel().isEmpty()) {
                if (!toggleButton.isSelected()) {
                    if (contentTable.getItems().size() > 0) {
                        messageWindow.setText(mouseEnteredMessageNonSelecteButtonAndNoSongSelected);
                    } else {
                        messageWindow.setText(mouseEnteredMessageNonSelectedPlaylistEmpty);
                    }
                } else if (toggleButton.isSelected()) {
                    messageWindow.setText(mouseEnteredMessageSelected);
                }

            }
        });
        toggleButton.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            messageWindow.clear();
        });
    }

    private void configureMouseEnteredOnButon(Button button, String messageWhenMouseEntered) {
        TextArea messageWindow = controlPaneController.getMessageTextArea();
        button.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            messageWindow.setText(messageWhenMouseEntered);
        });

        button.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            messageWindow.clear();
        });
    }

    private void configureMouseActionOnProgressSlider(String messageMouseEnteredOnSlider, Mp3Player player) {
        TextArea messageWindow = controlPaneController.getMessageTextArea();
        Slider progressSlider = controlPaneController.getProgressSlider();

        ChangeListener<Duration> timePropertyListiner = (arg, oldValue, newValue) -> {  //parametryzujemy typem obiektu któremu potem chcemy dokleić słuchacza(podejrzeć przy wywołaniu addListener, jakie są dostępne)
            int houer = 0;
            int minut = 0;
            int second = 0;
            int currentSecond = (int) newValue.toSeconds();
            SimpleIntegerProperty newValueProperty = new SimpleIntegerProperty(currentSecond);
            LocalTime currentSongProgress = LocalTime.of(houer, minut, second).plusSeconds(currentSecond); // przy takiej deklaracji wartość czasu jest powiązana z nevValue i nie jest zerowana po każdym pobraniu nevValue,
            // w przeciwnym razie ciało wyrażenia lambda działą jak pętla i zeruje wartoścci zmiennej godziny minutu i sekundy przy każdym pobraniu nevValue, dzieje się tak dlatego że
            // zmienne używane wewnątrz lambda są efektwnie finalne i nie "pozwalają na przypisanie kolejnych wartości" !!!!
            // rozwiązaniem takiego problemu może być deklaracja zmiennej instancyjnej (deklaracja zmiennej globalnej, która jest polem klasy), w tym wypadku nie jest ona zerowana i
            // wyrażenie np int houer=0  if(new.Value> oldValue){houer=houer+1} powoduje poprawne zapamiętanie zmiennej i zwiększanie zmiennej houer zgodnie z oczekiwaniem.

                    /*alternatywne rozwiązanie bez metdy plusSeconds=własna metoda PlusSeconds
                    *
                    * int currentSongHouer=(int)newValue.toHours();
                    int currentSongMinute=(int)newValue.toMinutes();
                    int currentSongSecond=(int)newValue.toSeconds();
                    int minus60Parameter=60;
                    int minus24Parameter=24;
                    if(curentSongHouer< minus24Parameter){
                        currentSongHouer=(int)newValue.toHours();
                    }
                    else {
                        currentSongHouer=currentSongHouer- minus24Parameter;
                    }


                       currentSongMinute=currentSongMinute-currentSongHouer*minus60Parameter;




                        currentSongSecond=currentSongSecond-currentSongMinute*minus60Parameter;


                        LocalTime currentSongProgress=LocalTime.of(currentSongHouer,currentSongMinute,currentSongSecond);
            messageWindow.setText(messageMouseEnteredOnSlider + "  " + currentSongProgress);
*/


            messageWindow.setText("aktualny postęp piosenki  " + currentSongProgress);


        };


        progressSlider.addEventFilter(MouseEvent.ANY, mouseEvent -> {

            if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
                player.getMediaPlayer().currentTimeProperty().addListener(timePropertyListiner); //dodanie zmiennej w której jest zapisana funkcja automatycznie ją wywołuje

            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
                messageWindow.clear();

                player.getMediaPlayer().currentTimeProperty().removeListener(timePropertyListiner);


                System.out.println("window clear");
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED || mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                //trzeba usunąć słuchacza bo okno będzie niby czyszczone zdodnie z akcja MOUSE_EXITED, ale potem znowu zapełniane i tak w kółko

                player.getMediaPlayer().currentTimeProperty().removeListener(timePropertyListiner);

                player.stop();
                // nie potrzebujemy słuchacza wartośći property bo nie przypisujemy akcji do automatycznego zdarzenia tylko do naszej akcjo przesuniecia słuchacza,
                int settedSongSecond = (int) player.getMediaPlayer().currentTimeProperty().get().toSeconds();
                LocalTime settedSongTime = LocalTime.of(0, 0, 0).plusSeconds(settedSongSecond);


                messageWindow.setText("ustaw nową wartość postępu piosenki na  " + settedSongTime);


            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {

                player.getMediaPlayer().currentTimeProperty().addListener(timePropertyListiner);

                player.play();

            } else {
                messageWindow.clear();
            }


        });

    }
}

