package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final JoinFive game;

    public GamePanel(JoinFive.Rule rule) {
        game = new JoinFive(5, rule);
        StartGame();
    }

    private void StartGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Random rand = new Random();
//                while (true) {
//                    try {
//                        if (gameState == MorpionSolitairePanel.State.BOT) {
//                            Thread.sleep(1500L);
//
//                            List<java.awt.Point> moves = grid.possibleMoves();
//                            java.awt.Point move = moves.get(rand.nextInt(moves.size()));
//                            grid.computerMove(move.y, move.x);
//                            botScore++;
//
//                            if (grid.possibleMoves().isEmpty()) {
//                                gameState = MorpionSolitairePanel.State.OVER;
//                            } else {
//                                gameState = MorpionSolitairePanel.State.HUMAN;
//                                message = "Your turn";
//                            }
//                            repaint();
//                        }
//                        Thread.sleep(100L);
//                    } catch (InterruptedException ignored) {
//                    }
//                }
//
//                Point p1, p2, p3, p4, p5;
//                p1 = new Point(1, new PointCoordinates(4, 6), game);
//                p2 = new Point(2, new PointCoordinates(5, 6), game);
//                p3 = new Point(3, new PointCoordinates(3, 10), game);
//                p4 = new Point(4, new PointCoordinates(5, 8), game);
//                p5 = new Point(5, new PointCoordinates(10, 6), game);
//
//                // exemple de chevauchement de deux lignes sur un point:
//                // game.getPossibleLines(p2) renvoie deux lignes possibles
//                game.addLine(game.getPossibleLines(p1).get(0), p1);
//                game.addLine(game.getPossibleLines(p2).get(1), p2);
//
//                // exemple de wikipedia:
//                /*
//                 * game.addLine(game.getPossibleLines(p3).get(0), p3);
//                 * game.addLine(game.getPossibleLines(p4).get(0), p4);
//                 * game.addLine(game.getPossibleLines(p5).get(0), p5);
//                 */
//                while (true) {
//                    repaint();
//                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.drawGrid((Graphics2D) g, getWidth(), getHeight());
    }
}
