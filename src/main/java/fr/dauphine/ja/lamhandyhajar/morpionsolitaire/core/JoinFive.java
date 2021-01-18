package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class JoinFive {
	
	Stack<Pair<Line, Point>> plays = new Stack<Pair<Line, Point>>();
	
	private HashMap<PointCoordinates, Point> grid;

	// to print the grid with the lines
	private List<Line> linesOnGrid;

	private static int lineLength = 5;
	
	// bounds of the current grid to print it properly.
	private Integer leftBound;
	private Integer rightBound;
	private Integer upBound;
	private Integer downBound;

	public int getNumberOfMoves() {
		return grid.size() - 36;
	}

	private Rule rule;
	
	public enum Rule {
		T, D;
	}

	public Rule getRule() {
		return rule;
	}

	/**
	 * @return the calling instance of the class. This method is used on getMoves()
	 *         method
	 */
	private JoinFive getInstance() {
		return this;
	}
	
	public ArrayList<Pair<Line, Point>> getMoves() {
		ArrayList<Pair<Line, Point>> result = new ArrayList<>();
		
		for (int x = leftBound - 1; x <= rightBound + 1; x++) {
			for (int y = upBound + 1; y >= downBound - 1; y--) {
				PointCoordinates coord = new PointCoordinates(x, y);
				Point p = getPoint(coord);
				
				if (p == null) {
					p = new Point(coord, getInstance());
					List<Line> lines = getPossibleLines(p);
					for (Line line : lines) {
						result.add(new Pair<Line, Point>(line, p));
					}
				}
			}
		}
		
		return result;
	}

	public int getLineLength() {
		return lineLength;
	}

	private JoinFive() {
		grid = new HashMap<>();
		linesOnGrid = new ArrayList<Line>();
		initGrid();
	}

	public JoinFive(Rule rule) {
		this();
		this.rule = rule;
	}
	
	public JoinFive(Rule rule, HashMap<PointCoordinates, Point> grid) {
		this(rule);
		this.grid = grid;
	}

	private void initGrid() {
		final int[] basePoints = { 120, 72, 72, 975, 513, 513, 975, 72, 72, 120 };
		for (int r = 0; r < 10; r++)
			for (int c = 0; c < 10; c++)
				if ((basePoints[r] & (1 << c)) != 0) {
					Point p = new Point(0, new PointCoordinates(c, r), this);
					setPoint(p, true);
				}
	}

	public Point getPoint(PointCoordinates position) {
		return grid.get(position);
	}

	private void setPoint(Point p, boolean persistent) {
		grid.put(p.getPosition(), p);
		
		if (persistent) {
			int p1 = p.getPosition().getP1();
			int p2 = p.getPosition().getP2();

			leftBound = (leftBound != null) ? Math.min(leftBound, p1) : p1;
			rightBound = (rightBound != null) ? Math.max(rightBound, p1) : p1;
			downBound = (downBound != null) ? Math.min(downBound, p2) : p2;
			upBound = (upBound != null) ? Math.max(upBound, p2) : p2;
			
			p.setIndex(getNumberOfMoves());
			
			//System.out.println("set " + p.getPosition());
		}
	}

	private void removePoint(Point p) {
		grid.remove(p.getPosition());
	}

	/*
	 * Given a position of a point, returns the available lines or an empty list if
	 * the point is not valid
	 */
	public List<Line> getPossibleLines(Point newPoint) {
		List<Line> possibleLines = null;
		List<Line> lines = new ArrayList<Line>();

		if (getPoint(newPoint.getPosition()) != null)
			return lines;

		possibleLines = newPoint.getAllPossibleLines();

		setPoint(newPoint, false);
		for (Line line : possibleLines) {
			if (line.isValid()) {
				lines.add(line);
			}
		}
		removePoint(newPoint);

		return lines;
	}
	
	public void updateBounds() {
		leftBound = null;
		rightBound = null;
		downBound = null;
		upBound = null;
		
		for (PointCoordinates pc : grid.keySet()) {
			int p1 = pc.getP1();
			int p2 = pc.getP2();

			leftBound = (leftBound != null) ? Math.min(leftBound, p1) : p1;
			rightBound = (rightBound != null) ? Math.max(rightBound, p1) : p1;
			downBound = (downBound != null) ? Math.min(downBound, p2) : p2;
			upBound = (upBound != null) ? Math.max(upBound, p2) : p2;
		}
	}

	/*
	 * Adds a line to the grid, with its new corresponding point
	 */
	public void play(Line line, Point p) {
		if (getPoint(p.getPosition()) != null) return;
		setPoint(p, true);
		linesOnGrid.add(line);
		int i = 0;
		for (Point p1 : line) {
			p1.addLine(line, i++);
		}
		plays.push(new Pair<Line, Point>(line, p));
	}
	
	/**
	 * @return true if there was a move to undo, false otherwise
	 */
	public boolean undoPlay() {
		if (plays.empty()) return false;
		
		Pair<Line, Point> play = plays.pop();
		Point p = play.getP2();
		Line line = play.getP1();
		
		linesOnGrid.remove(line);
		for (Point p1 : line) {
			p1.removeLine(line);
		}
		removePoint(p);
		return true;
	}

	public static void main(String[] args) {
		JoinFive game;
		if (args[0].equals("5T"))
			game = new JoinFive(Rule.T);
		else
			game = new JoinFive(Rule.D);
		Point p1, p2, p3, p4, p5;
		p1 = new Point(new PointCoordinates(4, 6), game);
		p2 = new Point(new PointCoordinates(5, 6), game);
		p3 = new Point(new PointCoordinates(3, 10), game);
		p4 = new Point(new PointCoordinates(5, 8), game);
		p5 = new Point(new PointCoordinates(10, 6), game);

		// exemple de chevauchement de deux lignes sur un point:
		// game.getPossibleLines(p2) renvoie deux lignes possibles
		game.play(game.getPossibleLines(p1).get(0), p1);
		game.play(game.getPossibleLines(p2).get(1), p2);

		// exemple de wikipedia:
		/*
		 * game.play(game.getPossibleLines(p3).get(0), p3);
		 * game.play(game.getPossibleLines(p4).get(0), p4);
		 * game.play(game.getPossibleLines(p5).get(0), p5);
		 */
	}
}
