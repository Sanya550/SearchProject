module com.example.searchproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires snowball.stemmer;
    requires stanford.corenlp;
    requires lombok;
    requires org.jsoup;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.json;


    opens com.example.searchproject to javafx.fxml;
    exports com.example.searchproject;
    exports com.example.searchproject.helpers;
    opens com.example.searchproject.helpers to javafx.fxml;
    exports com.example.searchproject.enums;
    opens com.example.searchproject.enums to javafx.fxml;
    exports com.example.searchproject.controller;
    opens com.example.searchproject.controller to javafx.fxml;
}