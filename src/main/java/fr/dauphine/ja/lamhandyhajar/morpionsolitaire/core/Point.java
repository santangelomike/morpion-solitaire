package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * used to represent a point in a JoinFive game
 */
public class Point {
	private HashMap<Line, Integer> lines;
	private int index;
	private PointCoordinates position;
	
	public Point(PointCoordinates position) {
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
	 */
	public void addLine(Line line, int positionInLine) {
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
	
}
