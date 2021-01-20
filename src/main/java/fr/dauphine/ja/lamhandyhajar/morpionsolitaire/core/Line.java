package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.Iterator;
import java.util.List;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;

public class Line implements Iterable<PointCoordinates> {
	private PointCoordinates firstPosition;
	private Orientation orientation;

	public Line(PointCoordinates firstPosition, Orientation orientation) {
		this.firstPosition = firstPosition;
		this.orientation = orientation;
	}

	/*
	 * checks if there is any overlap
	 */
	public boolean isValid(JoinFive game) {
		// on sait à partir d'ici que tous les points de la ligne ne sont pas out of
		// bounds par rapport au tableau grid (vérifié dans Orientation)
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
							// il faut que p soit le dernier point de line, auquel cas ça ne fonctionne pas
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
		};
	}

	public String toString(final JoinFive game) {
		String result = "";
		for (PointCoordinates p : getPointsPosition()) {
			result += p.toString() + ", ";
		}
		return result.substring(0, result.length() - 2);
	}
	
	public Line getCopy() {
		return new Line(firstPosition.getCopy(), orientation);
	}
}
