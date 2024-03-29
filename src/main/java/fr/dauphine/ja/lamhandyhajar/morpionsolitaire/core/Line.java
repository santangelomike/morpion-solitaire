package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Used to represent a line in a JoinFive game
 */
public class Line implements Iterable<PointCoordinates> {
    private PointCoordinates firstPosition;
    private Orientation orientation;

    /**
     * @param firstPosition the starting point of this line
     * @param orientation   is the orientation, is it horizontal (going from left to right), vertical (from bottom to top), updiagonal (going up and right), downdiagonal (going bottom and right)
     * @throws NullPointerException
     */
    public Line(PointCoordinates firstPosition, Orientation orientation) {
        checkNotNull(firstPosition);
        checkNotNull(orientation);

        this.firstPosition = firstPosition;
        this.orientation = orientation;
    }

    /**
     * @param game the game we want to consider
     * @return true if this line can be played according to the actual grid in game, false otherwise
     * @throws NullPointerException
     */
    public boolean isValid(JoinFive game) {
        checkNotNull(game);

        int i = 0;
        int numberOverlaps = 0;

        for (PointCoordinates coord : this) {
            Point p = game.getPoint(coord);
            if (p == null)
                return false;
            else {
                // si c'est un point qui n'est pas aux extrémités de la ligne
                if (i != 0 && i != JoinFive.getLineLength() - 1) {
                    for (Line line : p.getLines().keySet()) {
                        if (line.orientation == orientation)
                            return false;
                    }
                }
                // si c'est un point aux extrémités
                else if (i == 0) {
                    for (Line line : p.getLines().keySet()) {
                        if (line.orientation == orientation) {
                            // p needs to be the last point of line, otherwise it doesn't work
                            Point tmp = null;
                            for (PointCoordinates c1 : line) {
                                Point p1 = game.getPoint(c1);
                                tmp = p1;
                            }
                            if (tmp != p)
                                return false;
                            numberOverlaps += 1;
                        }
                    }
                } else if (i == JoinFive.getLineLength() - 1) {
                    for (Line line : p.getLines().keySet()) {
                        if (line.orientation == orientation) {
                            if (line.firstPosition != p.getPosition())
                                return false;
                            numberOverlaps += 1;
                        }
                    }
                }
            }
            i += 1;
        }
        return numberOverlaps < (game.getRule() == Rule.T ? 2 : 1);
    }

    /**
     * @return a list of the coordinates that this line contains
     */
    private List<PointCoordinates> getPointsPosition() {
        return orientation.getPointsPosition(firstPosition, JoinFive.getLineLength());
    }

    public Iterator<PointCoordinates> iterator() {
        return new Iterator<PointCoordinates>() {
            List<PointCoordinates> pointsPosition = getPointsPosition();
            int i = 0;

            public boolean hasNext() {
                return i < pointsPosition.size();
            }

            public PointCoordinates next() {
                return pointsPosition.get(i++);
            }

            @Override
            public void remove() {

            }
        };
    }

    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        String result = "";
        for (PointCoordinates p : getPointsPosition()) {
            result += p.toString() + ", ";
        }
        return result.substring(0, result.length() - 2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPosition.getP1(), firstPosition.getP2(), orientation);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Line))
            return false;
        Line l = (Line) o;

        return firstPosition.equals(l.firstPosition) && orientation == l.orientation;
    }

    /**
     * This method is useful to avoid multithreading concurrent modifications
     *
     * @return a copy of the object
     */
    public Line getCopy() {
        return new Line(firstPosition.getCopy(), orientation);
    }
}
