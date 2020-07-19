package ChessGame.ChessPeices;

import ChessGame.Game;
import ChessGame.Move;
import ChessGame.Position;

import java.util.ArrayList;
import java.util.Objects;

import static Tools.StreamTools.getArrayListFromStream;

public abstract class ChessPiece {
    Position position;
    public final boolean isWhite;
    public final Game game;

    public ChessPiece(Position position, boolean isWhite, Game game) {
        this.position = position;
        this.isWhite = isWhite;
        this.game = game;
    }

    public Position getPosition() {
        return position;
    }

    public void moveTo(Position position) throws PositionNotValidException {
        if (!isNextPositionValid(position))
            throw new PositionNotValidException();
        this.position = position;
    }

    public abstract ArrayList<Move> validMoves();

    public ArrayList<Move> validKillingMoves(){
        return validMoves();
    }

    public boolean isValidKillingMove(Move move){
        return validKillingMoves().contains(move);
    }

    public ArrayList<Position> validKillingPositions() {
        return getArrayListFromStream(validKillingMoves().stream().map(this.position::movedByIgnoringInvalidPositionException));
    }

    protected void addMoveToMoves(Move move, ArrayList<Move> moves){
        if (isMoveBasicallyPossible(move))
            moves.add(move);
    }

    private boolean isMoveBasicallyPossible(Move move){
        try {
            return game.getPieceAtIgnoringIOB(position.movedBy(move)).isOppose(this);
        } catch (NullPointerException exception) {
            return true;
        } catch (Position.InvalidPositionException e){
            return false;
        }
    }


    public boolean isMoveValid(Move move) {
        return validMoves().contains(move);
    }

    public boolean isNextPositionValid(Position position) {
        ArrayList<Move> validMoves = validMoves();
        return !validMoves.isEmpty() && validMoves.contains(this.position.moveNeededToGoToPos(position));
    }

    public ArrayList<Position> validPositions() {
        return getArrayListFromStream(validMoves().stream().map(this.position::movedByIgnoringInvalidPositionException)
                .filter(Objects::nonNull));
    }


    protected int upward(int amount){
        return amount*(isWhite ? 1 : -1);
    }

    public boolean isOppose(ChessPiece chessPiece){
        return chessPiece.isWhite != this.isWhite;
    }

    public boolean isPieceAtPositionOppose(int x,int y){
        return game.getPieceAtIgnoringIOB(x,y) != null &&
                isOppose(game.getPieceAtIgnoringIOB(x, y));
    }



    public boolean isPieceAtPositionOppose(Position position){
        return isPieceAtPositionOppose(position.getX(),position.getY());
    }

    public class PositionNotValidException extends Exception {
    }
}
