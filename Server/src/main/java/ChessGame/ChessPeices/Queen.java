package ChessGame.ChessPeices;

import ChessGame.Game;
import ChessGame.Move;
import ChessGame.Position;

import java.util.ArrayList;

public class Queen extends ChessPiece {
    public Queen(Position position, boolean isWhite, Game game) {
        super(position, isWhite, game);
    }

    @Override
    public ArrayList<Move> validMoves() {
        ArrayList<Move> moves = Bishop.biShopMoves(this, game);
        moves.addAll(Rock.rockMoves(this, game));
        return moves;
    }
}
