package fr.dauphine.ja.lamhandyhajar.morpionsolitaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Point {
	HashMap<Line, Integer> lines;
	int index; // si c'est un point initial ou le premier coup, le deuxième coup etc.
	private PointCoordinates position;
	
	public Point(int index, PointCoordinates position) {
		lines = new HashMap<Line, Integer>();
		this.index = index;
		this.position = position;
	}
	
	public List<Line> getAllPossibleLines() {
		List<Line> possibleLines = new ArrayList<Line>();
		
		for (int i = 0; i < JoinFive.getInstance().getLineLength(); i++) {
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
	
	public HashMap<Line, Integer> getLines() {
		return lines;
	}
	
	public String toString() {
		return position.toString();
	}
}
