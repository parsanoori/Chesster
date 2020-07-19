package ChessGame.ChessPeices;

import ChessGame.Game;
import ChessGame.Move;
import ChessGame.Position;

import java.util.ArrayList;

public class Rock extends ChessPiece {


    public Rock(Position position, boolean isWhite, Game game) {
        super(position, isWhite, game);
    }

    static ArrayList<Move> rockMoves(ChessPiece chessPiece, Game game){
        int thisPosX = chessPiece.getPosition().getX();
        int thisPosY = chessPiece.getPosition().getY();
        int i;

        ArrayList<Move> moves = new ArrayList<>();

        for (i = 1; i + thisPosX < 8 && game.getPieceAt(thisPosX + i,thisPosY) == null; i++)
            moves.add(new Move(i,0));
        if (game.getPieceAtIgnoringIOB(thisPosX+i,thisPosY) != null &&
                game.getPieceAtIgnoringIOB(thisPosX + i,thisPosY).isOppose(chessPiece))
            moves.add(new Move(i,0));

        for (i = 1; i + thisPosY < 8 && game.getPieceAt(thisPosX,thisPosY+i) == null; i++)
            moves.add(new Move(0,i));
        if (game.getPieceAtIgnoringIOB(thisPosX,thisPosY+i) != null &&
                game.getPieceAtIgnoringIOB(thisPosX ,thisPosY + i).isOppose(chessPiece))
            moves.add(new Move(0,i));

        for (i = 1; thisPosX - i > -1 && game.getPieceAt(thisPosX-i,thisPosY) == null; i++)
            moves.add(new Move(-i,0));
        if (game.getPieceAtIgnoringIOB(thisPosX-i,thisPosY) != null &&
                game.getPieceAtIgnoringIOB(thisPosX-i ,thisPosY).isOppose(chessPiece))
            moves.add(new Move(-i,0));

        for (i = 1; thisPosY - i > -1 && game.getPieceAt(thisPosX,thisPosY-i) == null; i++)
            moves.add(new Move(0,-i));
        if (game.getPieceAtIgnoringIOB(thisPosX,thisPosY-i) != null &&
                game.getPieceAtIgnoringIOB(thisPosX ,thisPosY - i).isOppose(chessPiece))
            moves.add(new Move(0,-i));
        return  moves;
    }


    @Override
    public ArrayList<Move> validMoves() {
        return rockMoves(this, game);
    }
}
