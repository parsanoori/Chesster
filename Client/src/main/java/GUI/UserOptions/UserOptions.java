package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import Exceptions.notLoggedInException;
import GUI.App;
import javafx.event.ActionEvent;

import java.io.IOException;

public class UserOptions {


    public void getMyData(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/GetMyData");
        } catch (IOException ignored) {
        }
    }

    public void viewOnlineUsers(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/ViewOnlineUsers");
        } catch (IOException ignored) { }

    }

    public void viewScoreBoard(ActionEvent actionEvent) {
        try {
            App.setRoot("UserOptions/ViewScoreBoard");
        } catch (IOException ignored) { }
    }

    public void history(ActionEvent actionEvent) {
        try {
            App.setRoot("ViewHistory");
        } catch (IOException ignored) { }
    }

    public void changePassword(ActionEvent actionEvent) {
        try {
            App.setRoot("ChangePassword");
        } catch (IOException ignored) { }

    }

    public void logout(ActionEvent actionEvent) {
        try {
            OutputHandler.getOutputHandler().logout();
            App.setRoot("Login");
        } catch (IOException | NoOutPutHandlerException | notLoggedInException ignored) { }
    }
}
