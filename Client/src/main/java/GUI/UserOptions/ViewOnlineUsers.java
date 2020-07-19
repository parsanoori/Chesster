package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.EmptyListException;
import Exceptions.NoOutPutHandlerException;
import GUI.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewOnlineUsers extends HasCellView implements Initializable {

    public ListView<CellView> onlineUsersView;
    public Label error;
    public TextField query;

    public void search(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                ArrayList<String> accounts = OutputHandler.getOutputHandler().searchOnlineUsers(query.getText());
                fillList(accounts,onlineUsersView);
            } catch (NoOutPutHandlerException ignored) {
            } catch (EmptyListException e) {
                fillList(new ArrayList<>(),onlineUsersView);
            }
        }).start();

    }


    public void back(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/UserOptions");
        } catch (IOException ignored) { }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                ArrayList<String> accounts = OutputHandler.getOutputHandler().getOnlineUsers();
                fillList(accounts,onlineUsersView);
            } catch (NoOutPutHandlerException ignored) {
            }
        }).start();
    }

    public void updateError(String string) {
        Platform.runLater(() -> error.setText(string));
    }
}
