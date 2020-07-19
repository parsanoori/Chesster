package GUI;

import Communication.OutputHandler;
import Exceptions.*;
import Main.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Login {

    public Label errorLabel;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;

    public void login(ActionEvent actionEvent) {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();

        new Thread(() -> {
            try {
                OutputHandler.getOutputHandler().login(username,password);
                App.setRoot("UserOptions/UserOptions");
            } catch (NoUserFoundException e) {
                updateLabel("No User Found");
            } catch (PasswordIncorrectException e) {
                updateLabel("Password Is incorrect");
            } catch (NoOutPutHandlerException e) {
                e.printStackTrace();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }


        }).start();

    }

    private void updateLabel(String message){
        Platform.runLater(() -> errorLabel.setText(message));
    }

    public void signup(ActionEvent actionEvent) {
        try {
            App.setRoot("Signup");
        } catch (IOException ignored) {
        }
    }
}
