package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

/**
 * This class represents a move for the JoinFive game.
 */
public class Move extends Pair<Line, Point> {
	public Move(Line l, Point p) {
		super(l, p);
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
	
	/**
	 * Used to avoid multithreading concurrent modifications
	 * @return a copy of this instance
	 */
	public Move getCopy() {
		return new Move(p1.getCopy(), p2.getCopy());
	}
}
