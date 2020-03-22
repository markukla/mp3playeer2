package pl.javastart.Mp3Player.Main;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pl.javastart.Mp3Player.Controller.IconfiedController;
import pl.javastart.Mp3Player.Controller.MainController;
import pl.javastart.Mp3Player.mp3.Mp3Song;
import pl.javastart.Mp3Player.player.Mp3Player;


import java.io.IOException;
import java.time.LocalTime;

public class Main extends Application {
    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
   private MainController mainController;

    public IconfiedController getIconfiedController() {
        return iconfiedController;
    }

    public void setIconfiedController(IconfiedController iconfiedController) {
        this.iconfiedController = iconfiedController;
    }

    @FXML
    private IconfiedController iconfiedController;




    public static void main(String[] args) {
        launch(args);
    }


    /*Na koniec przerabiamy klasę startową aplikacji w taki sposób,
     aby korzystając z FXMLLoadera wczytać plik mainPane.fxml,
     dodać go do sceny, a tę na końcu osadzić w oknie aplikacji.
     Jako że mainPane jest powiązany z mainControler(który ma wstryknięte wszystkie inne kontrolery)to
     FXMLLoader automatycznie tworzy obiekty wszystkich kontrolerek i wszystkich zawartych w nich kontrolek(oznaczonych @FXML)
     charakterystyczną cechą klasy kontrolera jest to że powinien on posiadać metodę initialize() wywoływaną automatycznie po utworzeniu obiektu kontrolera.
     wewnątrz tej metody należy definiować instrukcję (albo odnośniki do metod), które zmieniają stan kontrolek, obsługa zdarzeń itp*/
    @Override
    public void start(Stage stage) throws Exception {
        Pane mainPane=null;
        FXMLLoader mainPaneLoader = new FXMLLoader();
        mainPaneLoader.setLocation(getClass().getResource("/fxml/mainPane.fxml")); //tylko ustawia lokację póżniej trzeba wywołać Loader.load, aby wywołać efekt
       mainPane= mainPaneLoader.load();
        System.out.println(mainPaneLoader);
        mainController=mainPaneLoader.getController(); //wcześniej należy wywołać loader load
        System.out.println(mainController);

        Scene scene = new Scene(mainPane);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.setTitle("Mp3Song Player");
        stage.show();
        Stage iconfiedstage=new Stage();  // okno minimzalizacji tworzone jest tutaj, ale dopiero póżniej ustawia się go jako widoczne, dzięki temu wyrażenie lambda w metodzie działa poprawnie
        showSimplifiedSmallWindowWhenMainWindowIsIconised (stage,iconfiedstage);



    }


    public void showSimplifiedSmallWindowWhenMainWindowIsIconised (Stage stage,Stage iconfiedStage){


        stage.iconifiedProperty().addListener((arg, oldValue,newValue)-> {

            /*nie może być w tym miejscu deklaracji typy Stage iconfiedStage=null;
            dlatego że słuchacz działą jak pętla i będzie ustawiał za każdym sprawdzeniem newValue  wartość iconfiedStage jako null*/

            if (newValue == true) {
                System.out.println("minimalizacja głównego okna");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/iconfiedPane.fxml"));

                Parent parent = null;
                try {
                    parent = loader.load();
                    iconfiedController = loader.getController();
                    System.out.println(iconfiedController);


                    System.out.println(mainController.getControlPaneController().getPreviousButton());


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene iconfiedScene = new Scene(parent);
                iconfiedStage.setScene(iconfiedScene);
                iconfiedStage.setTitle("Mp3Song Player");

                iconfiedStage.show();
                iconfiedStage.setX(800);
                iconfiedStage.setY(600);
                stage.hide();
                ToggleButton playButton = iconfiedController.getPlayButton();
                Button nextButton = iconfiedController.getNextButton();
                Button previousButton = iconfiedController.getPreviousButton();
                // ustawienie akcji przycisków typu button z małego okna(izonizedPane) poprzez wywołanie metody configureButtons i podanie ich jako parametry
                mainController.configureButtons(playButton, nextButton, previousButton);

                ToggleButton replayPlayList = iconfiedController.getReplayPlaylistButton();
                ToggleButton playAllPlaylist = iconfiedController.getPlayAllSongsButton();
                ToggleButton loopSong = iconfiedController.getLoopSong();
                ToggleButton randomButton = iconfiedController.getRandom();
                Slider progressSlider = iconfiedController.getProgressSlider();
                Slider mainWindowSlider = mainController.getControlPaneController().getProgressSlider();

                // najprostrzy sposób na ustawienie działania przycisków typy ToggleButton z małego okna to zbindowanie ich selectedProperty z przyciskami z głownego okna, wtedy ich załączenie załącza te z głownego okna
                replayPlayList.selectedProperty().bindBidirectional(mainController.getControlPaneController().getReplayPlaylistButton().selectedProperty());
                playAllPlaylist.selectedProperty().bindBidirectional(mainController.getControlPaneController().getPlayAllSongsButton().selectedProperty());
                loopSong.selectedProperty().bindBidirectional(mainController.getControlPaneController().getLoopSong().selectedProperty());
                randomButton.selectedProperty().bindBidirectional(mainController.getControlPaneController().getRandom().selectedProperty());

                Mp3Player player = mainController.getPlayer();

                progressSlider.valueProperty().bindBidirectional(mainWindowSlider.valueProperty());
                progressSlider.maxProperty().bindBidirectional(mainWindowSlider.maxProperty());
                Label songLenhthTime = iconfiedController.getSongLengthLabel();
                songLenhthTime.textProperty().bindBidirectional(mainController.getControlPaneController().getSongLengthLabel().textProperty());




                    ToggleButton timeDisplayRevers = iconfiedController.getReverseTimeDisplay();

                    Label timeLabelleft = iconfiedController.getCurrentTimeLabel();

                    timeLabelleft.textProperty().bindBidirectional(mainController.getControlPaneController().getCurrentTimeLabel().textProperty());
                    timeDisplayRevers.selectedProperty().bindBidirectional(mainController.getControlPaneController().getReverseTimeDisplay().selectedProperty());
                    progressSlider.valueProperty().addListener((arg1, oldValue1, newValue1) -> {
                        if (progressSlider.isValueChanging()) {

                            timeLabelleft.setText(mainController.getControlPaneController().getCurrentTimeLabel().textProperty().get());

                            player.getMediaPlayer().seek(Duration.seconds(newValue1.doubleValue())); //seek ustawia postep piosenki zgodnie z postepem suwaka, przelicza sobie procenty czy jakoś tak
                        }
                    });
TextArea songTitleDisplay=iconfiedController.getMessageTextArea();

ReadOnlyIntegerProperty indexP=mainController.getContentPaneController().getContentTable().getSelectionModel().selectedIndexProperty();


                String songTitleP=mainController.getContentPaneController().getContentTable().getItems().get(indexP.get()).getTitle();

songTitleDisplay.textProperty().set(songTitleP); // inicjacja wartością pola zaznaczonego w momencie minimalizacji okna
                songTitleDisplay.setText("odtwarzanie piosenki o tytule:  "+songTitleDisplay.textProperty().get());
                indexP.addListener((a,oldV,newV)->{  // nasłuchiwanie zmian wartości zaznaczonego indeksu przy zmianie piosenke

                    String songTitle=mainController.getContentPaneController().getContentTable().getItems().get(newV.intValue()).getTitle();

                    StringProperty titleProperty=new SimpleStringProperty(songTitle);

                    songTitleDisplay.textProperty().bindBidirectional(titleProperty); // jak chcemy wyświetlać tą wartość to nie trzeba dodawać metody set text, zostanie wowołana automatycznie dla text property
songTitleDisplay.setText("odtwarzanie piosenki o tytule:  "+songTitleDisplay.textProperty().get());


                });

iconfiedStage.maximizedProperty().addListener((observableValue, s, t1) -> {
    if(t1==true){
        stage.show();
        stage.setIconified(false);
        iconfiedStage.close();

    }

});

            }
            else if(newValue==false){
                iconfiedStage.close();
                System.out.println("zamykanie małego okna");
            }



        });

    }
}













