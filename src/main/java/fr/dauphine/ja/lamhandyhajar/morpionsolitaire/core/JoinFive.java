package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers.Move;

import static com.google.common.base.Preconditions.checkNotNull;

public class JoinFive {
	
	Stack<Move> plays = new Stack<Move>();
	
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
	 * @return the available moves at the current state of the game
	 */
	public ArrayList<Move> getMoves() {
		checkNotNull(leftBound);
		checkNotNull(rightBound);
		checkNotNull(downBound);
		checkNotNull(upBound);
		
		ArrayList<Move> result = new ArrayList<>();
		
		for (int x = leftBound - 1; x <= rightBound + 1; x++) {
			for (int y = upBound + 1; y >= downBound - 1; y--) {
				PointCoordinates coord = new PointCoordinates(x, y);
				Point p = getPoint(coord);
				
				if (p == null) {
					p = new Point(coord);
					List<Line> lines = getPossibleLines(p);
					for (Line line : lines) {
						result.add(new Move(line, p));
					}
				}
			}
		}
		
		return result;
	}

	/**
	 * @return the number of points a line has to contain (fixed to 5)
	 */
	public static int getLineLength() {
		return lineLength;
	}

	private JoinFive() {
		grid = new HashMap<>();
		linesOnGrid = new ArrayList<Line>();
		initGrid();
	}
	
	/**
	 * @param rule T for 5T, D for 5D
	 */
	public JoinFive(Rule rule) {
		this();
		this.rule = rule;
	}

	/**
	 * Initializes the grid of this instance with the usual grid from the JoinFive game.
	 * Code inspired from: https://rosettacode.org/wiki/Morpion_solitaire/Java
	 */
	private void initGrid() {
		final int[] basePoints = { 120, 72, 72, 975, 513, 513, 975, 72, 72, 120 };
		for (int r = 0; r < 10; r++)
			for (int c = 0; c < 10; c++)
				if ((basePoints[r] & (1 << c)) != 0) {
					Point p = new Point(0, new PointCoordinates(c, r));
					setPoint(p, true);
				}
	}

	/**
	 * @param position of the point
	 * @return the point in the grid, or null if not found
	 */
	public Point getPoint(PointCoordinates position) {
		checkNotNull(position);
		
		return grid.get(position);
	}

	/**
	 * Sets a point in the grid.
	 * @param p
	 * @param persistent is true iff we consider that this point is permanently added to the grid
	 */
	private void setPoint(Point p, boolean persistent) {
		checkNotNull(p);
		
		grid.put(p.getPosition(), p);
		
		if (persistent) {
			int p1 = p.getPosition().getP1();
			int p2 = p.getPosition().getP2();

			leftBound = (leftBound != null) ? Math.min(leftBound, p1) : p1;
			rightBound = (rightBound != null) ? Math.max(rightBound, p1) : p1;
			downBound = (downBound != null) ? Math.min(downBound, p2) : p2;
			upBound = (upBound != null) ? Math.max(upBound, p2) : p2;
			
			p.setIndex(getNumberOfMoves());
		}
	}

	private void removePoint(Point p) {
		checkNotNull(p);
		
		grid.remove(p.getPosition());
	}

	/**
	 * Given a point, returns the playable lines according to the actual state of the game, or an empty list if there isn't any move available for this point
	 */
	public List<Line> getPossibleLines(Point newPoint) {
		checkNotNull(newPoint);
		
		List<Line> possibleLines = null;
		List<Line> lines = new ArrayList<Line>();

		if (getPoint(newPoint.getPosition()) != null)
			return lines;

		possibleLines = newPoint.getAllPossibleLines();

		setPoint(newPoint, false);
		for (Line line : possibleLines) {
			if (line.isValid(this)) {
				lines.add(line);
			}
		}
		removePoint(newPoint);

		return lines;
	}
	
	/**
	 * updates variables leftBound, rightBound, downBound and upBound according to the actual grid
	 */
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

	/**
	 * Adds a line to the grid, with its new corresponding point
	 * @param line
	 * @param p
	 */
	public void play(Line line, Point p) {
		checkNotNull(line);
		checkNotNull(p);
		
		if (getPoint(p.getPosition()) != null) throw new IllegalArgumentException("There is already a point where you want to play.");
		
		setPoint(p, true);
		linesOnGrid.add(line);
		int i = 0;
		for (PointCoordinates c1 : line) {
			Point p1 = getPoint(c1);
			if (p1 == null) throw new NullPointerException("This line is not valid.");
			p1.addLine(line, i++);
		}
		plays.push(new Move(line, p));
	}
	
	public void play(Move move) {
		checkNotNull(move);
		
		play(move.getP1(), move.getP2());
	}
	
	/**
	 * @return true if there was a move in the grid to undo, false otherwise
	 */
	public boolean undoPlay() {
		if (plays.empty()) return false;
		
		Move play = plays.pop();
		Point p = play.getP2();
		Line line = play.getP1();
		
		linesOnGrid.remove(line);
		for (PointCoordinates c1 : line) {
			Point p1 = getPoint(c1);
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
		p1 = new Point(new PointCoordinates(4, 6));
		p2 = new Point(new PointCoordinates(5, 6));
		p3 = new Point(new PointCoordinates(3, 10));
		p4 = new Point(new PointCoordinates(5, 8));
		p5 = new Point(new PointCoordinates(10, 6));

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
