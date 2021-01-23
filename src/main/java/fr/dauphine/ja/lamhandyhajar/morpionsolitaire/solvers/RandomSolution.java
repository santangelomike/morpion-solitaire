package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Move;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;

public class RandomSolution {

    /**
     * plays random moves (uniform distribution) to find a solution for the JoinFive game
     *
     * @param game
     * @return the score, and the moves made to get this score, in order.
     */
    public static Pair<Integer, LinkedList<Move>> sample(JoinFive game) {
        checkNotNull(game);

        ArrayList<Move> l;
        LinkedList<Move> plays = new LinkedList<>();
        int nbPlays = 0;

        do {
            l = game.getMoves();
            if (l.size() == 0) break;
            int choiceIndex = (int) (Math.random() * l.size());
            Move choice = l.get(choiceIndex);
            plays.add(choice);
            game.play(choice);
            nbPlays++;
        } while (l.size() != 0);

        int score = game.getNumberOfMoves();
        for (int i = 0; i < nbPlays; i++) game.undoPlay();

        return new Pair<Integer, LinkedList<Move>>(score, plays);
    }

    public static void main(String[] args) {
        JoinFive game = new JoinFive(Rule.D);
        System.out.println(sample(game).getP1());
    }
}
