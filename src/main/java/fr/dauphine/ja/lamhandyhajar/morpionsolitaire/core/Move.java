package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.Objects;

/**
 * This class represents a move for the JoinFive game.
 */
public class Move extends Pair<Line, Point> {
    public Move(Line l, Point p) {
        super(l, p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1.hashCode(), p2.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Move))
            return false;
        Move move = (Move) o;

        return move.p1.equals(p1) && move.p2.equals(p2);
    }

    @Override
    public String toString() {
        return "Point: " + p2.toString() + ", Line: " + p1.toString();
    }

    /**
     * Used to avoid multithreading concurrent modifications
     *
     * @return a copy of this instance
     */
    public Move getCopy() {
        return new Move(p1.getCopy(), p2.getCopy());
    }
}
