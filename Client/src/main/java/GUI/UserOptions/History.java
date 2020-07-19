package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.EmptyListException;
import Exceptions.NoOutPutHandlerException;
import GUI.App;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class History implements Initializable {
    public Label error;
    public ListView<Label> games;

    public void back(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/UserOptions");
        } catch (IOException ignored) { }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ArrayList<String> gamesHis = OutputHandler.getOutputHandler().getHistory();
            ArrayList<Label> cellViews = new ArrayList<>();
            for (int i = 0; i < gamesHis.size(); i++) {
                String account = gamesHis.get(i);
                cellViews.add(new Label( i + " " + account));
            }
            games.setItems(FXCollections.observableList(cellViews));
        } catch (NoOutPutHandlerException ignored) {
        } catch (EmptyListException e) {
            games.setItems(FXCollections.observableList(new ArrayList<>()));
        }
    }

}
