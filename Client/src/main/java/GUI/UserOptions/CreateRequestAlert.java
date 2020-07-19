package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import GUI.App;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class CreateRequestAlert {
    public static void createRequestAlert(String accountName,String numberOFWins,String numberOfLooses){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm Game ?");
        alert.setContentText("Game With " + accountName + " " + numberOfLooses + " " + numberOFWins);

        Optional<ButtonType> result = alert.showAndWait();
        try {
            if (result.get() == ButtonType.OK) {
                OutputHandler.getOutputHandler().accept();
                App.setRoot("UserOptions/ChessGame");
            } else {
                OutputHandler.getOutputHandler().reject();
            }
        } catch (IOException | NoOutPutHandlerException ignored) { }
    }
}
