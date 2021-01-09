package fr.dauphine.ja.lamhandyhajar.morpionsolitaire;

import java.util.ArrayList;
import java.util.List;

public class JoinFive {
	private static JoinFive instance = null;
	
	public static JoinFive getInstance() {
		if (instance == null) {
			instance = new JoinFive(5);
        }
        return instance;
	}
	
	private int maximumGridLength;
	private Point[][] grid;
	
	// to print the grid with the lines
	private List<Line> lines;
	
	private int lineLength;
	
	// bounds of the current grid to print it properly. Each of these values is between 0 and maximumGridLength
	private Integer leftBound;
	private Integer rightBound;
	private Integer upBound;
	private Integer downBound;
	
	public int getMaximumGridLength() {
		return maximumGridLength;
	}
	
	public int getLineLength() {
		return lineLength;
	}
	
	/*
	 * The upper bound for maximum score is 705 for 5T and 121 for 5D, so it's sufficient to initialize a grid of size 2000x2000
	 */
	private JoinFive(int lineLength) {
		maximumGridLength = 100;
		grid = new Point[maximumGridLength][maximumGridLength];
		this.lineLength = lineLength;
		lines = new ArrayList<Line>();
		initGrid();
	}
	
	private void initGrid() {
		int offset = (maximumGridLength/2) - 5;
		final int[] basePoints = {120, 72, 72, 975, 513, 513, 975, 72, 72, 120};
		for (int r = 0; r < 10; r++)
            for (int c = 0; c < 10; c++)
                if ((basePoints[r] & (1 << c)) != 0) {
                	Point p = new Point(0, new PointCoordinates(offset + c, offset + r));
                    setPoint(p);
                }
	}
	
	public Point getPoint(PointCoordinates position) {
		return grid[position.getP1()][position.getP2()];
	}
	
	private void setPoint(Point p) {
		int p1 = p.getPosition().getP1();
		int p2 = p.getPosition().getP2();
		System.out.println("set: " + p1 + ", " + p2);
		leftBound = (leftBound != null) ? Math.min(leftBound, p1) : p1;
		rightBound = (rightBound != null) ? Math.max(rightBound, p1) : p1;
		downBound = (downBound != null) ? Math.min(downBound, p2) : p2;
		upBound = (upBound != null) ? Math.max(upBound, p2) : p2;
		
		grid[p.getPosition().getP1()][p.getPosition().getP2()] = p;
	}
	
	private void removePoint(Point p) {
		int p1 = p.getPosition().getP1();
		int p2 = p.getPosition().getP2();
		System.out.println("remove: " + p1 + ", " + p2);
		grid[p.getPosition().getP1()][p.getPosition().getP2()] = null;
	}
	
	/*
	 * Given a position of a point, returns the available lines or an empty list if the point is not valid
	 */
	private List<Line> getPossibleLines(Point newPoint) {
		List<Line> possibleLines = null;
		List<Line> lines = new ArrayList<Line>();
		
		if (getPoint(newPoint.getPosition()) != null) return lines;
		
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
	private void addLine(Line line, Point p) {
		setPoint(p);
		lines.add(line);
		int i = 0;
		for (Point p1 : line) {
			p1.addLine(line, i++);
		}
	}
	
	public static void main(String[] args) {
		JoinFive game = getInstance();
		Point p1, p2, p3, p4, p5;
		p1 = new Point(1, new PointCoordinates(49, 51));
		p2 = new Point(2, new PointCoordinates(50, 51));
		p3 = new Point(3, new PointCoordinates(48, 55));
		p4 = new Point(4, new PointCoordinates(50, 53));
		p5 = new Point(5, new PointCoordinates(55, 51));
		
		// exemple de chevauchement de deux lignes sur un point: game.getPossibleLines(p2) renvoie deux lignes possibles
		game.addLine(game.getPossibleLines(p1).get(0), p1);
		game.addLine(game.getPossibleLines(p2).get(1), p2);
		
		// exemple de wikipedia:
		/*game.addLine(game.getPossibleLines(p3).get(0), p3);
		game.addLine(game.getPossibleLines(p4).get(0), p4);
		game.addLine(game.getPossibleLines(p5).get(0), p5);*/
	}
}
