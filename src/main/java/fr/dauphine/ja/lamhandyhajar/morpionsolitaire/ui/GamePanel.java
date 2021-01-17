package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.ui;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.PointCoordinates;

import javax.swing.*;

public class GamePanel extends JPanel {
    public JButton button;

    public GamePanel(JoinFive.Rule rule) {
        button = new JButton();
        button.setText("Quitter");
        this.add(button);

        StartGame(rule);
    }

    private void StartGame(JoinFive.Rule rule) {

        JoinFive game = new JoinFive(5, rule);

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
