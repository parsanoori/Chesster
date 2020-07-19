package ChessGame;

import ChessGame.ChessPeices.ChessPiece;
import ChessGame.ChessPeices.King;

import java.io.PipedOutputStream;
import java.util.ArrayList;

public class CheckAndCheckMate {

    static boolean willBeCheckAfterMove(Position from, Position to, Game game, boolean enemyWillBeChecked) {
        try {
            ChessPiece[][] chessPieces = game.locations;
            ChessPiece temp = chessPieces[to.getX()][to.getY()];
            ChessPiece chessPiece = game.getPieceAt(from);
            Position firstPos = new Position(from.getX(), from.getY());

            if (temp != null) {
                if (temp.isWhite)
                    game.whitePieces.remove(temp);
                else game.blackPieces.remove(temp);
            }

            chessPieces[from.getX()][from.getY()] = null;
            chessPieces[to.getX()][to.getY()] = chessPiece;
            chessPiece.getPosition().setPos(to.getX(), to.getY());


            boolean result = false;
            ArrayList<ChessPiece> chessPiecesToIterateOn = chessPiece.isWhite == enemyWillBeChecked ? game.whitePieces : game.blackPieces;
            King kingToCheck = chessPiece.isWhite != enemyWillBeChecked ? game.whiteKing : game.blackKing;

            for (ChessPiece cp : chessPiecesToIterateOn) {

                System.out.println(cp);
                if (cp.validKillingPositions().contains(kingToCheck.getPosition())) {

                    result = true;
                }
            }


            chessPieces[firstPos.getX()][firstPos.getY()] = chessPieces[to.getX()][to.getY()];
            chessPieces[firstPos.getX()][firstPos.getY()].getPosition().setPos(firstPos.getX(), firstPos.getY());
            chessPieces[to.getX()][to.getY()] = temp;

            if (temp != null) {
                if (temp.isWhite)
                    game.whitePieces.add(temp);
                else game.blackPieces.add(temp);
            }


            return result;
        } catch (Exception ignored) {
        }
        return false;
    }




    static boolean isCheckMate(Game game, boolean isWhiteAttacking) {
        ArrayList<ChessPiece> underAttackPieces = isWhiteAttacking ? game.blackPieces : game.whitePieces;

        for (ChessPiece chessPiece : underAttackPieces)
            for (Position position : chessPiece.validPositions())
                if (!willBeCheckAfterMove(chessPiece.getPosition(), position, game, false)) {
                    System.out.println(position);
                    return false;
                }
        return true;
    }


}
