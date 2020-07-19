package ChessGame;

import java.util.Objects;

public class Move {
    public final int x;
    public final int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return x == move.x &&
                y == move.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
