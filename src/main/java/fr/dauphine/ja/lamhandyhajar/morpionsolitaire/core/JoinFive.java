package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;

public class JoinFive {

	private Stack<Move> plays;

	private HashSet<Move> availableMoves;

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
	 * @throws NullPointerException
	 */
	public ArrayList<Move> getMoves() {
		updateBounds();

		int left = leftBound - 1;
		int right = rightBound + 1;
		int down = downBound - 1;
		int up = upBound + 1;

		if (!availableMoves.isEmpty() && !plays.empty()) {
			Move lastMove = plays.peek();
			PointCoordinates lastPointPosition = lastMove.getP2().getPosition();

			Iterator<Move> it = availableMoves.iterator();
			while (it.hasNext()) {
				Move move = it.next();
				if (move.getP2().getPosition().equals(lastMove.getP2().getPosition()))
					it.remove();
				else if (move.getP1().getOrientation() == lastMove.getP1().getOrientation()) {
					Point p = move.getP2();
					Line l = move.getP1();
					setPoint(p);
					if (!l.isValid(this))
						it.remove();
					removePoint(p);
				}
			}

			left = Math.max(left, lastPointPosition.getP1() - 4);
			right = Math.min(right, lastPointPosition.getP1() + 4);
			up = Math.min(up, lastPointPosition.getP2() + 4);
			down = Math.max(down, lastPointPosition.getP2() - 4);
		}

		for (int x = left; x <= right; x++) {
			for (int y = up; y >= down; y--) {
				PointCoordinates coord = new PointCoordinates(x, y);
				Point p = getPoint(coord);

				if (p == null) {
					p = new Point(coord);
					List<Line> lines = getPossibleLines(p);
					for (Line line : lines) {
						availableMoves.add(new Move(line, p));
					}
				}
			}
		}

		ArrayList<Move> result = new ArrayList<>();
		for (Move move : availableMoves) {
			result.add(move.getCopy());
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
		plays = new Stack<Move>();
		availableMoves = new HashSet<Move>();
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
	 * Initializes the grid of this instance with the usual grid from the JoinFive
	 * game. Code inspired from: https://rosettacode.org/wiki/Morpion_solitaire/Java
	 */
	private void initGrid() {
		final int[] basePoints = { 120, 72, 72, 975, 513, 513, 975, 72, 72, 120 };
		for (int r = 0; r < 10; r++)
			for (int c = 0; c < 10; c++)
				if ((basePoints[r] & (1 << c)) != 0) {
					Point p = new Point(0, new PointCoordinates(c, r));
					setPoint(p);
				}
	}

	/**
	 * @param position of the point
	 * @return the point in the grid, or null if not found
	 * @throws NullPointerException
	 */
	public Point getPoint(PointCoordinates position) {
		checkNotNull(position);

		return grid.get(position);
	}

	/**
	 * Sets a point in the grid.
	 * 
	 * @param p
	 * @throws NullPointerException
	 */
	private void setPoint(Point p) {
		checkNotNull(p);

		grid.put(p.getPosition(), p);
		p.setIndex(getNumberOfMoves());
	}

	/**
	 * updates variables leftBound, rightBound, downBound and upBound according to
	 * the actual grid
	 */
	private void updateBounds() {
		leftBound = null;
		rightBound = null;
		downBound = null;
		upBound = null;

		for (PointCoordinates pc : grid.keySet()) {
			updateBounds(pc);
		}
	}
	
	private void updateBounds(PointCoordinates pc) {
		int p1 = pc.getP1();
		int p2 = pc.getP2();

		leftBound = (leftBound != null) ? Math.min(leftBound, p1) : p1;
		rightBound = (rightBound != null) ? Math.max(rightBound, p1) : p1;
		downBound = (downBound != null) ? Math.min(downBound, p2) : p2;
		upBound = (upBound != null) ? Math.max(upBound, p2) : p2;
	}

	/**
	 * @param p
	 * @throws NullPointerException
	 */
	private void removePoint(Point p) {
		checkNotNull(p);

		grid.remove(p.getPosition());
	}

	/**
	 * Given a point, returns the playable lines according to the actual state of
	 * the game, or an empty list if there isn't any move available for this point
	 * 
	 * @throws NullPointerException
	 */
	public List<Line> getPossibleLines(Point newPoint) {
		checkNotNull(newPoint);

		List<Line> possibleLines = null;
		List<Line> lines = new ArrayList<Line>();

		if (getPoint(newPoint.getPosition()) != null)
			return lines;

		possibleLines = newPoint.getAllPossibleLines();

		setPoint(newPoint);
		for (Line line : possibleLines) {
			if (line.isValid(this)) {
				lines.add(line);
			}
		}
		removePoint(newPoint);

		return lines;
	}

	/**
	 * Adds a line to the grid, with its new corresponding point
	 * 
	 * @param line
	 * @param p
	 * @throws IllegalArgumentException if the move can't be done
	 * @throws NullPointerException
	 */
	public void play(Line line, Point p) {
		checkNotNull(line);
		checkNotNull(p);

		if (getPoint(p.getPosition()) != null)
			throw new IllegalArgumentException("There is already a point where you want to play.");

		setPoint(p);
		linesOnGrid.add(line);
		int i = 0;
		for (PointCoordinates c1 : line) {
			Point p1 = getPoint(c1);
			if (p1 == null)
				throw new IllegalArgumentException("This line is not valid.");
			p1.addLine(line, i++);
		}
		plays.push(new Move(line, p));
	}

	/**
	 * @param move
	 * @throws NullPointerException
	 */
	public void play(Move move) {
		checkNotNull(move);

		play(move.getP1(), move.getP2());
	}

	/**
	 * @return true if there was a move in the grid to undo, false otherwise
	 */
	public boolean undoPlay() {
		availableMoves.clear();
		
		if (plays.empty())
			return false;

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
}
