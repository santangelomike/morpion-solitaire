package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.PointCoordinates;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class GamePanel extends JPanel {

    private final JoinFive game;

    public GamePanel(JoinFive.Rule rule) {
        game = new JoinFive(rule);
        StartGame();
    }

    private void StartGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO main while loop
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid((Graphics2D) g);
    }

    public void drawGrid(Graphics2D g) {
        final int pointSize = 8;
        final int cellSize = 39;

        int halfCellSize = cellSize / 2;

        int xCenter = getWidth() / 2;
        int yCenter = getHeight() / 2;

        int xOrigin = xCenter - halfCellSize - 4 * cellSize;
        int yOrigin = yCenter - halfCellSize - 4 * cellSize;

        g.setColor(Color.WHITE);

        int x = (xCenter - halfCellSize) % cellSize;
        int y = (yCenter - halfCellSize) % cellSize;

        for (int i = 0; i <= getWidth() / cellSize; i++) {
            g.drawLine(x + i * cellSize, 0, x + i * cellSize, getHeight());
        }

        for (int i = 0; i <= getHeight() / cellSize; i++) {
            g.drawLine(0, y + i * cellSize, getWidth(), y + i * cellSize);
        }

        g.setColor(Color.DARK_GRAY);

        Iterator<Map.Entry<PointCoordinates, Point>> it = game.getGrid().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<PointCoordinates, Point> pair = it.next();

            int xPoint = xOrigin + pair.getKey().getP1() * cellSize - (pointSize / 2);
            int yPoint = yOrigin + pair.getKey().getP2() * cellSize - (pointSize / 2);

            g.fillOval(xPoint, yPoint, pointSize, pointSize);

//            Iterator<Map.Entry<Line, Integer>> linesIt = pair.getValue().getLines().entrySet().iterator();

//            g.setColor(Color.BLACK);

//            while (linesIt.hasNext()) {
//                Entry<Line, Integer> line = linesIt.next();
//
//                Iterator<Point> pointsIt = line.getKey().iterator();
//
//                Point oldPoint = null;
//
//                while (pointsIt.hasNext()) {
//
//                    Point point = pointsIt.next();
//
//                    if (oldPoint == null) {
//                        oldPoint = point;
//                        continue;
//                    }
//
//                    int x1 = xOrigin + oldPoint.getPosition().p1 * cellSize;
//                    int y1 = yOrigin + oldPoint.getPosition().p2 * cellSize;
//
//                    int x2 = xOrigin + point.getPosition().p1 * cellSize;
//                    int y2 = yOrigin + point.getPosition().p2 * cellSize;
//
//                    oldPoint = point;
//
//                    g.drawLine(x1, y1, x2, y2);
//
//                    pointsIt.remove();
//                }
//                linesIt.remove();
//            }
//            it.remove();
        }
    }
}
