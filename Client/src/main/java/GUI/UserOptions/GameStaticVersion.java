package GUI.UserOptions;

import Communication.OutputHandler;
import Exceptions.NoOutPutHandlerException;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Objects;

public class GameStaticVersion {

    static ScrollPane chats;
    static VBox chatBox;
    static TextField chatInput;
    static GridPane boardPane;
    static boolean isPieceSelected = false;
    static int xSelected = -1;
    static int ySelected = -1;

    public static void setPiece(int row, int column, String pieceName, boolean isWhite) {
        for (Node node : boardPane.getChildren()) {
            if (GridPane.getRowIndex(node) == column && GridPane.getColumnIndex(node) == row)
                ((ImageView) node).setImage(new Image("Assets/Piece/" + pieceName + (isWhite ? "White" : "Black")));
        }
    }

    public static ImageView imageViewAt(int row, int column) {
        for (Node node : boardPane.getChildren()) {
            if (GridPane.getRowIndex(node) == column && GridPane.getColumnIndex(node) == row)
                return (ImageView) node;
        }
        return null;
    }

    public static void addMessageToChatBox(String message) {
        Platform.runLater(() ->
                chatBox.getChildren().add(new Label(message))
        );
    }

    public static void getMessage(String message) {
        addMessageToChatBox(message);
    }

    public static void highlightCellAt(int row, int column) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.RED);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);

        try {
            Platform.runLater(() ->
                    Objects.requireNonNull(imageViewAt(row, column)).setEffect(borderGlow)
            );
        } catch (NullPointerException ignored) {
        }

    }

    public static void highlightCells(String response) {
        String[] values = response.split(" ");
        for (int i = 0; i < values.length / 2; i++) {
            int x = Integer.parseInt(values[2 * i]);
            int y = Integer.parseInt(values[2 * i + 1]);
            GameStaticVersion.highlightCellAt(x, y);
        }
    }

    public static void visuallyMovePiece(int row1, int col1, int row2, int col2) {

        Objects.requireNonNull(imageViewAt(row2, col2)).setImage(
                Objects.requireNonNull(imageViewAt(row1, col1)).getImage()
        );
        Objects.requireNonNull(imageViewAt(row1, col1)).setImage(null);
    }

    public static void movePieceTo(int row1, int col1, int row2, int col2) {
        new Thread(() -> {
            try {
                OutputHandler.getOutputHandler().move(row1, col1, row2, col2);
            } catch (NoOutPutHandlerException ignored) {
            }
        }).start();
    }
}

