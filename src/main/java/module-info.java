module mp3player {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires jid3lib;
    requires javafx.media;
    exports pl.javastart.Mp3Player.Main to javafx.graphics;
    exports pl.javastart.Mp3Player.Controller to javafx.graphics;
    exports pl.javastart.Mp3Player.mp3 to javafx.graphics;
    opens pl.javastart.Mp3Player.Controller to javafx.fxml;  // kontroler tworzy powiązanie z kodem fxml, i ta komenda opens umożliwia to powiązanie
    opens pl.javastart.Mp3Player.mp3 to javafx.graphics,javafx.base ;


}