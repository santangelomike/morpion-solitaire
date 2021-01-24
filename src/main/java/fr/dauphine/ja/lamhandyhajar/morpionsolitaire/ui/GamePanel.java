package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final JoinFive game;
    private final int pointSize = 8;
    private final int cellSize = 39;
    private final int halfCellSize = cellSize / 2;
    private int xCenter, yCenter, xOrigin, yOrigin;
    private String message = "Debut de la partie";

    public GamePanel(JoinFive.Rule rule) {
        game = new JoinFive(rule);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                int x = Math.round((e.getX() - xOrigin) / cellSize);
                int y = Math.round((e.getY() - yOrigin) / cellSize);

                PointCoordinates coordinates = new PointCoordinates(x, y);

                Point point = new Point(coordinates);

                if (game.getPossibleLines(point).isEmpty()) {
                    message = "Pas de possibilite";
                } else {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        message = "Possibilites : ";

                        for (Line l : game.getPossibleLines(point)) {
                            message += l.toString();
                        }
                    } else {
                        for (Line l : game.getPossibleLines(point)) {

                            Iterator<PointCoordinates> it = l.iterator();

                            while (it.hasNext()) {
                                PointCoordinates pc = it.next();

                                if (pc.getP1() == coordinates.getP1() && pc.getP2() == coordinates.getP2()) {
                                    message = "Succes";

                                    Move move = new Move(l, point);

                                    game.play(move);
                                }
                            }
                        }
                    }
                }

                repaint();
            }
        });

        StartGame();
    }

    private void StartGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGrid((Graphics2D) g);

        g.setColor(Color.white);
        g.fillRect(0, getHeight() - 50, getWidth(), getHeight() - 50);

        g.setColor(Color.lightGray);
        g.drawLine(0, getHeight() - 50, getWidth(), getHeight() - 50);

        g.setColor(Color.darkGray);
        g.drawString(message, 20, getHeight() - 18);
    }

    public void drawGrid(Graphics2D g) {
        g.setColor(Color.WHITE);

        xCenter = getWidth() / 2;
        yCenter = getHeight() / 2;

        xOrigin = xCenter - halfCellSize - 4 * cellSize;
        yOrigin = yCenter - halfCellSize - 4 * cellSize;

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

            Iterator<Map.Entry<Line, Integer>> linesIt = pair.getValue().getLines().entrySet().iterator();

            g.setColor(Color.BLACK);

            while (linesIt.hasNext()) {
                Map.Entry<Line, Integer> line = linesIt.next();

                Iterator<PointCoordinates> pointsIt = line.getKey().iterator();

                PointCoordinates oldPoint = null;

                while (pointsIt.hasNext()) {

                    PointCoordinates point = pointsIt.next();

                    if (oldPoint == null) {
                        oldPoint = point;
                        continue;
                    }

                    int x1 = xOrigin + oldPoint.getP1() * cellSize;
                    int y1 = yOrigin + oldPoint.getP2() * cellSize;

                    int x2 = xOrigin + point.getP1() * cellSize;
                    int y2 = yOrigin + point.getP2() * cellSize;

                    oldPoint = point;

                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }
}
