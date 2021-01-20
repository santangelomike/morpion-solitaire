package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

public class Move extends Pair<Line, Point> {
	public Move(Line l, Point p) {
		super(l, p);
	}
}
