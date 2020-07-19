package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoAaccountWithThisUsernameException;
import Exceptions.NoOutPutHandlerException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class CellView extends HBox implements MyCellInt {
    Label name;
    Label username;
    Label numberOfLooses;
    Label numberOfWins;

    String usernameString;

    Button blockButton;

    HasCellView view;

    public CellView(String name, String username, String numberOfWins, String numberOfLooses, HasCellView view) {
        this.name = new Label("Name: " + name);
        this.username = new Label("Username: " + username);
        this.numberOfLooses = new Label("Number Of Looses: " + numberOfLooses);
        this.numberOfWins = new Label("Number of Wins: " + numberOfWins);

        this.usernameString = username;

        this.view = view;

        this.username.setFont(Font.font(14));
        this.name.setFont(Font.font(12));
        this.numberOfWins.setFont(Font.font(12));
        this.numberOfLooses.setFont(Font.font(12));

        Button block = new Button("Block");

        this.blockButton = block;

        blockButton.setOnAction((actionEvent) -> {
            new Thread(() ->{
                try {
                    OutputHandler.getOutputHandler().blockUser(usernameString);
                } catch (NoAaccountWithThisUsernameException e){
                    view.updateError("No Account With This User Name");
                } catch (NoOutPutHandlerException ignored) { }
            }).start();
        });

        this.setSpacing(15);

        this.getChildren().addAll(this.username, this.name,this.numberOfWins,this.numberOfLooses,this.blockButton);
    }
}