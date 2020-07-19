package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import Exceptions.PasswordIncorrectException;
import GUI.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class ChangePassword {
    public PasswordField oldPassword;
    public PasswordField newPassword;
    public Label error;

    public void back(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/UserOptions");
        } catch (IOException ignored) { }
    }

    public void changePassword(ActionEvent actionEvent) {
        String oldPassword = this.oldPassword.getText();
        String newPassword = this.newPassword.getText();
        new Thread(() -> {
            try {
                OutputHandler.getOutputHandler().changePassword(oldPassword, newPassword);
            } catch (NoOutPutHandlerException ignored) {

            } catch (PasswordIncorrectException e) {
                Platform.runLater(() -> error.setText("Password is not correct"));
            }
        }).start();

    }
}
