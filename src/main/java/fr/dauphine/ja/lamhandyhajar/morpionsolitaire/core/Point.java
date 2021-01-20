package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Point {
	private HashMap<Line, Integer> lines;
	private int index; // si c'est un point initial ou le premier coup, le deuxième coup etc.
	private PointCoordinates position;
	
	public Point(PointCoordinates position) {
		lines = new HashMap<Line, Integer>();
		this.position = position;
	}

	public Point(int index, PointCoordinates position) {
		this(position);
		this.index = index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

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
	
	public Point getCopy() {
		return new Point(index, position);
	}
	
}
