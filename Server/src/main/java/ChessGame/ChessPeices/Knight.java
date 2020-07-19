package ChessGame.ChessPeices;

import ChessGame.Game;
import ChessGame.Move;
import ChessGame.Position;

import java.util.ArrayList;

public class Knight extends ChessPiece{

    public Knight(Position position, boolean isWhite, Game game) {
        super(position, isWhite, game);
    }

    @Override
    public ArrayList<Move> validMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        addMoveToMoves(new Move(1,2),moves);
        addMoveToMoves(new Move(2,1),moves);
        addMoveToMoves(new Move(1,-2),moves);
        addMoveToMoves(new Move(2,-1),moves);
        addMoveToMoves(new Move(-1,2),moves);
        addMoveToMoves(new Move(-2,1),moves);
        addMoveToMoves(new Move(-1,-2),moves);
        addMoveToMoves(new Move(-2,-1),moves);
        return moves;
    }
}
