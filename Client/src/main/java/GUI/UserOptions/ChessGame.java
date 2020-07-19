package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.InvalidPositionException;
import Exceptions.NoOutPutHandlerException;
import Exceptions.NoPieceAtLocationException;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static GUI.UserOptions.GameStaticVersion.*;

public class ChessGame implements Initializable {

    public ScrollPane chats;
    public VBox chatBox;
    public TextField chatInput;
    public GridPane boardPane;

    public void sendMsg(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                OutputHandler.getOutputHandler().message(chatInput.getText());
            } catch (NoOutPutHandlerException ignored) { }
        }).start();

        addMessageToChatBox(chatInput.getText());
        chatInput.clear();
    }

    public void exitGame(ActionEvent actionEvent) {


    }


    public void chessBoardMouseClicked(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        int indX = colIndex;
        int indY = rowIndex;

        new Thread(() -> {
            try {
                if (!isPieceSelected) {
                    OutputHandler.getOutputHandler().getValidMoves(indX,indY);
                    isPieceSelected = true;
                    xSelected = indX;
                    ySelected = indY;
                }
                if (isPieceSelected) {
                    OutputHandler.getOutputHandler().move(xSelected, ySelected,indX,indY);
                    xSelected = -1;
                    ySelected = -1;
                    isPieceSelected = false;
                }
            } catch (NoOutPutHandlerException | InvalidPositionException | NoPieceAtLocationException ignored) { }
        }).start();

    }


    private void initializePieces() {
        for (int i = 0; i < 8; i++) {
            setPiece(i, 1, "soldier", true);
            setPiece(i, 6, "soldier", false);
        }
        setPiece(0, 0, "rock", true);
        setPiece(0, 7, "rock", true);
        setPiece(7, 7, "rock", false);
        setPiece(7, 0, "rock", false);

        setPiece(1, 0, "knight", true);
        setPiece(6, 0, "knight", true);
        setPiece(1, 7, "knight", false);
        setPiece(6, 7, "knight", false);

        setPiece(2, 0, "bishop", true);
        setPiece(5, 0, "bishop", true);
        setPiece(2, 7, "bishop", false);
        setPiece(5, 7, "bishop", false);

        setPiece(3, 0, "queen", true);
        setPiece(3, 7, "queen", true);
        setPiece(4, 0, "queen", true);
        setPiece(4, 7, "queen", true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameStaticVersion.boardPane = this.boardPane;
        GameStaticVersion.chatBox = this.chatBox;
        GameStaticVersion.chatInput = this.chatInput;
        GameStaticVersion.chats = this.chats;
        initializePieces();
    }
}
