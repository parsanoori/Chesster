package ChessGame;

import AccountsManagar.Account;
import ChessGame.ChessPeices.*;
import Exceptions.ItsNotYourTurnException;
import Exceptions.NoPieceAtThisLocationException;
import Tools.StreamTools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Game {
    final ChessPiece[][] locations = new ChessPiece[8][8];
    final ArrayList<ChessPiece> whiteLostPieces = new ArrayList<>();
    final ArrayList<ChessPiece> blackLostPieces = new ArrayList<>();
    final ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    final ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    final King whiteKing;
    final King blackKing;

    public final Account whiteSide;
    public final Account blackSide;

    private boolean isWhiteChecked = false;
    private boolean isBlackChecked = false;

    int moves = 0;

    private boolean isFinished = false;
    private boolean isWhiteWinner;

    boolean isWhitesTurn = true;

    public final LocalDate date;

    void moveIgnoringCheck(Position from, Position to) throws NoPieceAtThisLocationException, Position.InvalidEndPositionException {
        ChessPiece piece1 = getPieceAt(from.getX(), from.getY());
        if (piece1 == null)
            throw new NoPieceAtThisLocationException();
        if (!piece1.isNextPositionValid(to))
            throw new Position.InvalidEndPositionException();
        ChessPiece piece2 = getPieceAt(to.getX(), to.getY());
        if (piece2 != null && piece2.isOppose(piece1) && piece1.isValidKillingMove(from.moveNeededToGoToPos(to))) {
            if (piece2.isWhite) {
                whiteLostPieces.add(piece2);
                whitePieces.remove(piece2);
            } else {
                blackLostPieces.add(piece1);
                blackPieces.remove(piece2);
            }
        }
        try {
            piece1.moveTo(to);
            locations[to.getX()][to.getY()] = piece1;
        } catch (ChessPiece.PositionNotValidException ignored) {
        }
    }

    void move(Position from,Position to) throws ItsNotYourTurnException, NoPieceAtThisLocationException, Position.InvalidEndPositionException {
        if (locations[from.getX()][from.getY()] == null)
            throw new NoPieceAtThisLocationException();
        if (locations[from.getX()][from.getY()].isWhite != isWhitesTurn)
            throw new ItsNotYourTurnException();
        if (!CheckAndCheckMate.willBeCheckAfterMove(from,to,this,false)){
            moveIgnoringCheck(from,to);
            moves++;
            if (CheckAndCheckMate.willBeCheckAfterMove(to,to,this,true)){
                if (getPieceAtIgnoringIOB(to).isWhite)
                    isBlackChecked = true;
                else
                    isWhiteChecked = true;
            } else {
                if (getPieceAtIgnoringIOB(to).isWhite)
                    isBlackChecked = false;
                else
                    isWhiteChecked = false;
            }
            if (CheckAndCheckMate.isCheckMate(this,getPieceAtIgnoringIOB(to).isWhite)){
                isFinished = true;
                isWhiteWinner = getPieceAt(to).isWhite;
            }
        }
    }

    public void move(int fromX,int fromY,int toX, int toY) throws Position.InvalidPositionException, ItsNotYourTurnException, NoPieceAtThisLocationException, Position.InvalidEndPositionException {
        this.move(new Position(fromX,fromY),new Position(toX,toY));
        whiteSide.getClientOutputHandler().sendMove(fromX, fromY, toX, toY);
        blackSide.getClientOutputHandler().sendMove(fromX, fromY, toX, toY);
    }

    public boolean isWhiteChecked() {
        return isWhiteChecked;
    }

    public boolean isBlackChecked() {
        return isBlackChecked;
    }



    private ArrayList<Position> validPositionsForPieceAt(Position position) throws NoPieceAtThisLocationException {
        if (locations[position.getX()][position.getY()] == null)
            throw new NoPieceAtThisLocationException();
        return StreamTools.getArrayListFromStream(
            this.validPositionsForPieceAtIgnoringCheck(position.getX(),position.getY()).stream()
                .filter(pos -> !CheckAndCheckMate.willBeCheckAfterMove(position,pos,this,false))
        );
    }

    public ArrayList<Position> validPositionsForPieceAt(int x,int y) throws Position.InvalidPositionException, NoPieceAtThisLocationException {
        return validPositionsForPieceAt(new Position(x,y));
    }

    private void addPieceToGame(ChessPiece chessPiece) {
        if (chessPiece.isWhite)
            whitePieces.add(chessPiece);
        else
            blackPieces.add(chessPiece);
        locations[chessPiece.getPosition().getX()][chessPiece.getPosition().getY()] = chessPiece;
    }

    {
        try {
            for (int i = 0; i < 8; i++) {
                addPieceToGame(new Soldier(new Position(i, 1), true, this));
                addPieceToGame(new Soldier(new Position(i, 6), false, this));
            }
            addPieceToGame(new Rock(new Position(0, 0), true, this));
            addPieceToGame(new Rock(new Position(7, 0), true, this));
            addPieceToGame(new Rock(new Position(0, 7), false, this));
            addPieceToGame(new Rock(new Position(7, 7), false, this));

            addPieceToGame(new Knight(new Position(1, 0), true, this));
            addPieceToGame(new Knight(new Position(6, 0), true, this));
            addPieceToGame(new Knight(new Position(1, 7), false, this));
            addPieceToGame(new Knight(new Position(6, 7), false, this));

            addPieceToGame(new Bishop(new Position(2, 0), true, this));
            addPieceToGame(new Bishop(new Position(5, 0), true, this));
            addPieceToGame(new Bishop(new Position(2, 7), false, this));
            addPieceToGame(new Bishop(new Position(5, 7), false, this));

            addPieceToGame(new Queen(new Position(3, 0), true, this));
            addPieceToGame(new Queen(new Position(3, 7), false, this));
            addPieceToGame(new King(new Position(4, 0), true, this));
            addPieceToGame(new King(new Position(4, 7), false, this));

        } catch (Position.InvalidPositionException ignored) {
        }

        whiteKing = (King) locations[4][0];
        blackKing = (King) locations[4][7];

        date = LocalDate.now();
    }

    public Game(Account whiteSide, Account blackSide) {
        this.whiteSide = whiteSide;
        this.blackSide = blackSide;
    }


    public ArrayList<Position> validPositionsForPieceAtIgnoringCheck(int x, int y) {
        return getPieceAtIgnoringIOB(x, y) == null ? null :
                locations[x][y].validPositions();
    }

    public boolean isOccupied(int x, int y) {
        return locations[x][y] != null;
    }

    public ChessPiece getPieceAt(int x, int y) {
        return locations[x][y];
    }

    public ChessPiece getPieceAt(Position position) {
        return getPieceAt(position.getX(), position.getY());
    }

    public ChessPiece getPieceAtIgnoringIOB(int x, int y) {
        try {
            return getPieceAt(x, y);
        } catch (IndexOutOfBoundsException ignored) {
        }
        return null;
    }

    public ChessPiece getPieceAtIgnoringIOB(Position position) {
        return getPieceAtIgnoringIOB(position.getX(), position.getY());
    }

    public boolean isOccupiedIgnoringIOB(int x, int y) {
        return getPieceAtIgnoringIOB(x, y) != null;
    }

    public boolean isOccupiedIgnoringIOB(Position position) {
        return isOccupiedIgnoringIOB(position.getX(), position.getY());
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isWhiteWinner() {
        return isWhiteWinner;
    }

    @Override
    public String toString() {
        return " " + whiteSide.getUsername() +
                " " + blackSide.getUsername() +
                " " + moves +
                " " + date.format(DateTimeFormatter.ofPattern("yyyy\\MM\\dd"));
    }
}

