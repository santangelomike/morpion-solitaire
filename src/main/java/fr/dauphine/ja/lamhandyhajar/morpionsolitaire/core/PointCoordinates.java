package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.Objects;

public class PointCoordinates extends Pair<Integer, Integer> {
	PointCoordinates(Integer p1, Integer p2) {
		super(p1, p2);
	}

	public PointCoordinates goDown(int i) {
		return new PointCoordinates(p1, p2 - i);
	}

	public PointCoordinates goUp(int i) {
		return new PointCoordinates(p1, p2 + i);
	}

	public PointCoordinates goRight(int i) {
		return new PointCoordinates(p1 + i, p2);
	}

	public PointCoordinates goLeft(int i) {
		return new PointCoordinates(p1 - i, p2);
	}

	public String toString() {
		return "(" + p1 + ", " + p2 + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(p1, p2);
	}
	
	/**
	 * this method is useful to avoid multithreading concurrent modifications
	 * @return a copy of this object
	 */
	@Override
	public PointCoordinates getCopy() {
		return new PointCoordinates(p1, p2);
	}
}
