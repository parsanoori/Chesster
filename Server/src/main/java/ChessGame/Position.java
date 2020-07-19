package ChessGame;

import java.util.Objects;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) throws InvalidPositionException {
        setPos(x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) throws InvalidPositionException {
        if (x > 7 || x < 0)
            throw new InvalidPositionException();
        this.x = x;
    }

    public void setY(int y) throws InvalidPositionException {
        if (y > 7 || y < 0)
            throw new InvalidPositionException();
        this.y = y;
    }

    public void setPos(int x,int y) throws InvalidPositionException {
        this.setX(x);
        this.setY(y);
    }

    public void changeX(int amount) throws InvalidPositionException {
        setX(this.x + amount);
    }

    public void changeY(int amount) throws InvalidPositionException{
        setY(this.y + amount);
    }

    public void changePos(int xAmount,int yAmount) throws InvalidPositionException {
        changeX(xAmount);
        changeY(yAmount);
    }

    public void changePos(Move move) throws InvalidPositionException {
        changePos(move.x,move.y);
    }

    public Position movedBy(Move move) throws  InvalidPositionException{
        return new Position(this.getX()+move.x,this.getY()+move.y);
    }

    public Position movedByIgnoringInvalidPositionException(Move move){
        try {
            return movedBy(move);
        } catch (InvalidPositionException e){
            return  null;
        }
    }

    public Move moveNeededToGoToPos(Position position){
        return new Move(position.getX()-this.getX(), position.getY()-this.getY());
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static class InvalidPositionException extends Exception {
    }

    public static class InvalidEndPositionException extends Throwable {
    }
}
