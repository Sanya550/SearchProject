module com.example.searchproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires snowball.stemmer;
    requires stanford.corenlp;


    opens com.example.searchproject to javafx.fxml;
    exports com.example.searchproject;
}