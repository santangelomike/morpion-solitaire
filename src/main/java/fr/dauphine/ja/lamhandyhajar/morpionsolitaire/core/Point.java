package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * used to represent a point in a JoinFive game
 */
public class Point {
	private HashMap<Line, Integer> lines;
	private int index;
	private PointCoordinates position;
	
	/**
	 * @param position
	 * @throws NullPointerException
	 */
	public Point(PointCoordinates position) {
		checkNotNull(position);
		
		lines = new HashMap<Line, Integer>();
		this.position = position;
	}

	/**
	 * @param index corresponds to the order of the move associated to this point: is it the first move, the second, etc.
	 * @param position the position of the point
	 */
	public Point(int index, PointCoordinates position) {
		this(position);
		this.index = index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return a list of all lines that could contain this point. Independent from any grid.
	 */
	public List<Line> getAllPossibleLines() {
		List<Line> possibleLines = new ArrayList<Line>();

		for (int i = 0; i < JoinFive.getLineLength(); i++) {
			possibleLines.add(new Line(position.goDown(i), Orientation.VERTICAL));
			possibleLines.add(new Line(position.goLeft(i), Orientation.HORIZONTAL));
			possibleLines.add(new Line(position.goLeft(i).goDown(i), Orientation.UPDIAGONAL));
			possibleLines.add(new Line(position.goLeft(i).goUp(i), Orientation.DOWNDIAGONAL));
		}
		
		return possibleLines;
	}

	public PointCoordinates getPosition() {
		return position;
	}

	/**
	 * Adds a line that contains this point
	 * @param line the line that
	 * @param positionInLine the position of this point in line, according to iterator order from line.
	 * @throws IllegalArgumentException if line doesn't contain this point
	 */
	public void addLine(Line line, int positionInLine) {
		boolean pointFoundInLine = false;
		for (PointCoordinates coord : line) {
			if (coord.equals(position)) pointFoundInLine = true;
		}
		if (!pointFoundInLine) throw new IllegalArgumentException("Point is not found in the line it is supposed to be in.");
		
		lines.put(line, positionInLine);
	}
	
	public void removeLine(Line line) {
		lines.remove(line);
	}

	public HashMap<Line, Integer> getLines() {
		return lines;
	}

	public String toString() {
		return position.toString();
	}
	
	/**
	 * This method is useful to avoid multithreading concurrent modifications
	 * @return a copy of this point, without the lines associated with it.
	 */
	public Point getCopy() {
		return new Point(index, position);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(position.getP1(), position.getP2());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Point))
			return false;
		Point p = (Point) o;

		return position.equals(p.position);
	}
}
