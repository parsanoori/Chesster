module Client {
    requires javafx.controls;
    requires javafx.fxml;

    opens GUI to javafx.fxml;
    exports GUI to javafx.graphics;
    exports GUI.UserOptions to javafx.graphics;
    opens GUI.UserOptions to javafx.fxml;
}