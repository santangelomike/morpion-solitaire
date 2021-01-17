package fr.dauphine.ja.lamhandyhajar.morpionsolitaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinFive {
	
	enum Rule {
		T,
		D;
	}

	private HashMap<PointCoordinates, Point> grid;

	// to print the grid with the lines
	private List<Line> lines;

	private int lineLength;
	
	private Rule rule;
	
	private void setRule(Rule rule) {
		this.rule = rule;
	}
	
	public Rule getRule() {
		return rule;
	}

	// bounds of the current grid to print it properly.
	private Integer leftBound;
	private Integer rightBound;
	private Integer upBound;
	private Integer downBound;

	public int getLineLength() {
		return lineLength;
	}

	public JoinFive(int lineLength) {
		grid = new HashMap<>();
		this.lineLength = lineLength;
		lines = new ArrayList<Line>();
		initGrid();
	}
	
	public JoinFive(int lineLength, Rule rule) {
		this(lineLength);
		setRule(rule);
	}

	private void initGrid() {
		final int[] basePoints = { 120, 72, 72, 975, 513, 513, 975, 72, 72, 120 };
		for (int r = 0; r < 10; r++)
			for (int c = 0; c < 10; c++)
				if ((basePoints[r] & (1 << c)) != 0) {
					Point p = new Point(0, new PointCoordinates(c, r), this);
					setPoint(p);
				}
	}

	public Point getPoint(PointCoordinates position) {
		return grid.get(position);
	}

	private void setPoint(Point p) {
		int p1 = p.getPosition().getP1();
		int p2 = p.getPosition().getP2();
		leftBound = (leftBound != null) ? Math.min(leftBound, p1) : p1;
		rightBound = (rightBound != null) ? Math.max(rightBound, p1) : p1;
		downBound = (downBound != null) ? Math.min(downBound, p2) : p2;
		upBound = (upBound != null) ? Math.max(upBound, p2) : p2;

		System.out.println("set " + p.getPosition());
		grid.put(p.getPosition(), p);
	}

	private void removePoint(Point p) {
		int p1 = p.getPosition().getP1();
		int p2 = p.getPosition().getP2();
		System.out.println("remove: " + p1 + ", " + p2);
		grid.remove(p.getPosition());
	}

	/*
	 * Given a position of a point, returns the available lines or an empty list if
	 * the point is not valid
	 */
	public List<Line> getPossibleLines(Point newPoint) {
		List<Line> possibleLines = null;
		List<Line> lines = new ArrayList<Line>();

		if (getPoint(newPoint.getPosition()) != null) {
			System.out.println("bruh");
			return lines;
		}

		possibleLines = newPoint.getAllPossibleLines();

		setPoint(newPoint);
		for (Line line : possibleLines) {
			if (line.isValid()) {
				lines.add(line);
			}
		}
		removePoint(newPoint);

		return lines;
	}

	/*
	 * Adds a line to the grid, with its new corresponding point
	 */
	public void addLine(Line line, Point p) {
		setPoint(p);
		lines.add(line);
		int i = 0;
		for (Point p1 : line) {
			p1.addLine(line, i++);
		}
	}

	public static void main(String[] args) {
		JoinFive game = new JoinFive(5);
		if (args[0].equals("5T")) game.setRule(Rule.T);
		else game.setRule(Rule.D);
		Point p1, p2, p3, p4, p5;
		p1 = new Point(1, new PointCoordinates(4, 6), game);
		p2 = new Point(2, new PointCoordinates(5, 6), game);
		p3 = new Point(3, new PointCoordinates(3, 10), game);
		p4 = new Point(4, new PointCoordinates(5, 8), game);
		p5 = new Point(5, new PointCoordinates(10, 6), game);

		// exemple de chevauchement de deux lignes sur un point:
		// game.getPossibleLines(p2) renvoie deux lignes possibles
		game.addLine(game.getPossibleLines(p1).get(0), p1);
		game.addLine(game.getPossibleLines(p2).get(1), p2);

		// exemple de wikipedia:
		/*
		 * game.addLine(game.getPossibleLines(p3).get(0), p3);
		 * game.addLine(game.getPossibleLines(p4).get(0), p4);
		 * game.addLine(game.getPossibleLines(p5).get(0), p5);
		 */
	}
}
