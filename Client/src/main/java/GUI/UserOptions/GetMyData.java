package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import GUI.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GetMyData implements Initializable {
    public Label data;

    public void back(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/UserOptions");
        } catch (IOException ignored) { }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                String dataString = OutputHandler.getOutputHandler().getMyData();
                fillData(dataString);
            } catch (NoOutPutHandlerException ignored) { }
        }).start();
    }

    private void fillData(String dataString){
        Platform.runLater(() -> data.setText(dataString));
    }
}
