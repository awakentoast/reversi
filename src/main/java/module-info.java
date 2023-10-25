module com.example.reversi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.reversi to javafx.fxml;
    exports com.example.reversi;
}