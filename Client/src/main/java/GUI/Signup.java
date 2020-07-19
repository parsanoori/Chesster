package GUI;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import Exceptions.PasswordInvalidCharacterException;
import Exceptions.PasswordNotLongEnoughException;
import Exceptions.UserExistsException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Signup {

    public TextField name;
    public TextField username;
    public PasswordField password;
    public PasswordField confirmPassword;
    public Label error;


    public void signup(ActionEvent actionEvent) {
        String name = this.name.getText();
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.confirmPassword.getText();

        if (!password.equals(confirmPassword)){
            error.setText("Passwords Do not Match");
            return;
        }

        new Thread(() -> {
            try {
                OutputHandler.getOutputHandler().signup(name, username, password);
                App.setRoot("Login");
            } catch (PasswordNotLongEnoughException e) {
                updateLabel("Password Must Be At Least 8 Characters");
            } catch (PasswordInvalidCharacterException e) {
                updateLabel("Invalid Characters Used In Password");
            } catch (UserExistsException e) {
                updateLabel("User Already Exists");
            } catch (IOException | NoOutPutHandlerException ignored) {
            }
        }).start();

    }

    private void updateLabel(String message){
        Platform.runLater(() -> {
            error.setText(message);
        });
    }

    public void login(ActionEvent actionEvent) {
        try {
            App.setRoot("Login");
        } catch (IOException ignored) {
        }
    }
}
