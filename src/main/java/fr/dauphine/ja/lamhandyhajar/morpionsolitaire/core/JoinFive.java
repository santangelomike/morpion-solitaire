package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class JoinFive {

    private final HashMap<PointCoordinates, Point> grid;
    // to print the grid with the lines
    private final List<Line> lines;
    private final int lineLength;
    private Rule rule;
    // to print the points
    private final int pointSize = 8;
    private final int cellSize = 39;
    // bounds of the current grid to print it properly.
    private Integer leftBound;
    private Integer rightBound;
    private Integer upBound;
    private Integer downBound;

    public JoinFive(int lineLength) {
        grid = new HashMap<>();
        this.lineLength = lineLength;
        lines = new ArrayList<>();
        initGrid();
    }

    public JoinFive(int lineLength, Rule rule) {
        this(lineLength);
        setRule(rule);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getLineLength() {
        return lineLength;
    }

    private void initGrid() {
        final int[] basePoints = {120, 72, 72, 975, 513, 513, 975, 72, 72, 120};
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
        List<Line> possibleLines;
        List<Line> lines = new ArrayList<>();

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

    public enum Rule {
        T,
        D
    }

    public void drawGrid(Graphics2D g, int w, int h) {

        int halfCellSize = cellSize / 2;

        int xCenter = w / 2;
        int yCenter = h / 2;

        int xOrigin = xCenter - halfCellSize - 4 * cellSize;
        int yOrigin = yCenter - halfCellSize - 4 * cellSize;

        g.setColor(Color.WHITE);

        int x = (xCenter - halfCellSize) % cellSize;
        int y = (yCenter - halfCellSize) % cellSize;

        for (int i = 0; i <= w / cellSize; i++) {
            g.drawLine(x + i * cellSize, 0, x + i * cellSize, h);
        }

        for (int i = 0; i <= h / cellSize; i++) {
            g.drawLine(0, y + i * cellSize, w, y + i * cellSize);
        }

        g.setColor(Color.DARK_GRAY);

        Iterator<Entry<PointCoordinates, Point>> it = grid.entrySet().iterator();

        while (it.hasNext()) {
            Entry<PointCoordinates, Point> pair = it.next();

            int xPoint = xOrigin + pair.getKey().p1 * cellSize - (pointSize / 2);
            int yPoint = yOrigin + pair.getKey().p2 * cellSize - (pointSize / 2);

            g.fillOval(xPoint, yPoint, pointSize, pointSize);

            it.remove();
        }
    }
}
