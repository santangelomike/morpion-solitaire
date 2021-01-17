package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;

import java.util.Iterator;
import java.util.List;

public class Line implements Iterable<Point> {
    private final PointCoordinates firstPosition;
    private final Orientation orientation;
    private final JoinFive game;

    public Line(PointCoordinates firstPosition, Orientation orientation, JoinFive game) {
        this.firstPosition = firstPosition;
        this.orientation = orientation;
        this.game = game;
    }

    /*
     * checks if there is any overlap
     */
    public boolean isValid() {
        // on sait à partir d'ici que tous les points de la ligne ne sont pas out of
        // bounds par rapport au tableau grid (vérifié dans Orientation)
        int i = 0;
        int numberOverlaps = 0;

        for (Point p : this) {
            if (p == null)
                return false;
            else {
                // si c'est un point qui n'est pas aux extrémités de la ligne
                if (i != 0 && i != game.getLineLength() - 1) {
                    for (Line line : p.getLines().keySet()) {
                        if (line.orientation == orientation)
                            return false;
                    }
                }
                // si c'est un point aux extrémités
                else if (i == 0) {
                    for (Line line : p.getLines().keySet()) {
                        if (line.orientation == orientation) {
                            // il faut que p soit le dernier point de line, auquel cas ça ne fonctionne pas
                            Point tmp = null;
                            for (Point p1 : line) {
                                tmp = p1;
                            }
                            if (tmp != p)
                                return false;
                            numberOverlaps += 1;
                        }
                    }
                } else if (i == game.getLineLength() - 1) {
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

    private List<PointCoordinates> getPointsPosition() {
        return orientation.getPointsPosition(firstPosition, game.getLineLength());
    }

    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            final List<PointCoordinates> pointsPosition = getPointsPosition();
            int i = 0;

            public boolean hasNext() {
                return i < pointsPosition.size();
            }

            public Point next() {
                return game.getPoint(pointsPosition.get(i++));
            }

            @Override
            public void remove() {

            }
        };
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Point p : this) {
            result.append(p.toString()).append(", ");
        }
        return result.toString();
    }
}
