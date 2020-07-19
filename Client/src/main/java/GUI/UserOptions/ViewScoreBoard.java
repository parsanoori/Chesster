package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import GUI.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewScoreBoard extends HasCellView implements Initializable {

    public ListView<CellView> onlineUsersView;
    public Label error;


    public void back(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/UserOptions");
        } catch (IOException ignored) { }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                ArrayList<String> accounts = OutputHandler.getOutputHandler().getScoreBoard();
                Platform.runLater(() -> fillList(accounts,onlineUsersView));
            } catch (NoOutPutHandlerException ignored) {
            }
        }).start();
    }




    public void updateError(String string) {
        Platform.runLater(() -> error.setText(string));
    }
}
