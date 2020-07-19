package ChessGame.ChessPeices;

import ChessGame.Game;
import ChessGame.Move;
import ChessGame.Position;

import java.util.ArrayList;

public class Bishop extends ChessPiece {


    public Bishop(Position position, boolean isWhite, Game game) {
        super(position, isWhite, game);
    }

    static ArrayList<Move> biShopMoves(ChessPiece chessPiece, Game game) {
        ArrayList<Move> moves = new ArrayList<>();
        int thisPosX = chessPiece.getPosition().getX();
        int thisPosY = chessPiece.getPosition().getY();
        int i;
        for (i = 1; thisPosX + i <= 7 && thisPosY + i <= 7 && game.getPieceAt(thisPosX + i, thisPosY + i) == null; i++)
            moves.add(new Move(i, i));
        if (game.getPieceAtIgnoringIOB(thisPosX + i, thisPosY + i) != null
                && game.getPieceAt(thisPosX + i, thisPosY + i).isOppose(chessPiece))
            moves.add(new Move(i, i));

        for (i = 1; thisPosX - i >= 0 && thisPosY - i >= 0 && game.getPieceAt(thisPosX - i, thisPosY - i) == null; i++)
            moves.add(new Move(-i, -i));
        if (game.getPieceAtIgnoringIOB(thisPosX - i, thisPosY - i) != null
                && game.getPieceAtIgnoringIOB(thisPosX - i, thisPosY - i).isOppose(chessPiece))
            moves.add(new Move(i, i));

        for (i = 1; thisPosX - i >= 0 && thisPosY + i <= 7 && game.getPieceAt(thisPosX - i, thisPosY + i) == null; i++)
            moves.add(new Move(-i, i));
        if (game.getPieceAtIgnoringIOB(thisPosX - i, thisPosY + i) != null
                && game.getPieceAtIgnoringIOB(thisPosX - i, thisPosY + i).isOppose(chessPiece))
            moves.add(new Move(-i, i));

        for (i = 1; thisPosX + i < 8 && thisPosY - i >= 0 && game.getPieceAt(thisPosX + i, thisPosY - i) == null; i++)
            moves.add(new Move(i, -i));
        if (game.getPieceAtIgnoringIOB(thisPosX + i, thisPosY - i) != null
                && game.getPieceAtIgnoringIOB(thisPosX + i, thisPosY - i).isOppose(chessPiece))
            moves.add(new Move(i, -i));
        return moves;
    }


    @Override
    public ArrayList<Move> validMoves(){
        return biShopMoves(this, game);
    }
}
