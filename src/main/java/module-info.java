module com.example.reversi {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jdk.jshell;
    requires jdk.xml.dom;


    opens com.example.reversi to javafx.fxml;
    exports com.example.reversi;
    exports com.example.reversi.game;
    opens com.example.reversi.game to javafx.fxml;
    exports com.example.reversi.controllers;
    opens com.example.reversi.controllers to javafx.fxml;
}