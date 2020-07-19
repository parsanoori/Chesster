package ChessGame.ChessPeices;
import ChessGame.Game;
import ChessGame.Move;
import ChessGame.Position;
import java.util.ArrayList;


public class Soldier extends ChessPiece{
    public Soldier(Position position, boolean isWhite, Game game) {
        super(position, isWhite, game);
    }

    private int upward(){
        return super.upward(1);
    }

    @Override
    public ArrayList<Move> validKillingMoves(){
        ArrayList<Move> result = new ArrayList<Move>();
        if (isPieceAtPositionOppose(position.getX()+1,position.getY()+upward()))
            result.add(new Move(1, upward()));
        if (isPieceAtPositionOppose(position.getX()-1,position.getY()+upward()))
            result.add(new Move(-1, upward()));
        return result;
    }

    @Override
    public ArrayList<Move> validMoves() {
        ArrayList<Move> moves = this.validKillingMoves();
        if (!game.isOccupiedIgnoringIOB(position.getX(),position.getY()+upward()))
            moves.add(new Move(0,upward()));
        if (!game.isOccupiedIgnoringIOB(position.getX(),position.getY()+upward(2)) &&
                ((this.isWhite && this.position.getY() == 1) || (!this.isWhite && this.position.getY() == 6)))
            moves.add(new Move(0,upward(2)));
        return moves;
    }
}
