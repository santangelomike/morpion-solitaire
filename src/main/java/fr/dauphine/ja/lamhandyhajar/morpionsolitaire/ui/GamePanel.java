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
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.drawGrid((Graphics2D) g, getWidth(), getHeight());
    }
}
