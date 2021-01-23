package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Move;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of the NMCS algorithm, with a multithreading option.
 * Singleton pattern.
 */
public class NMCS {

    private static NMCS instance = null;

    private int nbThreads;

    /**
     * @param nbThreads the maximum number of threads we want to instantiate for the multithread implementation.
     */
    private NMCS(int nbThreads) {
        this.nbThreads = nbThreads;
    }

    public static NMCS getInstance(int nbThreads) {
        if (instance == null) {
            instance = new NMCS(nbThreads);
        }
        return instance;
    }

    public static NMCS getInstance() {
        if (instance == null)
            throw new IllegalStateException("An attempt has been made to get NMCS instance but it hasn't been instantiated.");
        return instance;
    }

    public static void main(String[] args) {
        try {
            NMCS nmcs = NMCS.getInstance(8);
            Pair<Integer, LinkedList<Move>> result = nmcs.multithreadNested(2, Rule.D);
            System.out.println(result.getP1());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * NMCS algorithm: https://www.lamsade.dauphine.fr/~cazenave/papers/nested.pdf
     *
     * @param game
     * @param level
     * @return the score and the moves made to get to this score, in order
     */
    public Pair<Integer, LinkedList<Move>> nested(JoinFive game, int level) {
        checkNotNull(game);

        ArrayList<Move> moves = game.getMoves();
        int bestScore = -1;
        LinkedList<Move> bestSequence = null;
        LinkedList<Move> effectivePlays = new LinkedList<>();

        while (moves.size() != 0) {
            int scoreOfMove = -1;
            LinkedList<Move> moveSequence = null;

            if (level == 1) {
                for (Move move : moves) {
                    game.play(move.getP1(), move.getP2());

                    Pair<Integer, LinkedList<Move>> result = RandomSolution.sample(game);
                    int score = result.getP1();

                    game.undoPlay();

                    if (scoreOfMove < score) {
                        scoreOfMove = score;

                        moveSequence = new LinkedList<Move>();
                        moveSequence.add(move);
                        moveSequence.addAll(result.getP2());
                    }
                }
            } else {
                for (Move move : moves) {
                    game.play(move.getP1(), move.getP2());
                    Pair<Integer, LinkedList<Move>> result = nested(game, level - 1);
                    for (int i = 0; i < result.getP2().size(); i++) game.undoPlay();
                    game.undoPlay();

                    int score = result.getP1();
                    if (scoreOfMove < score) {
                        scoreOfMove = score;

                        moveSequence = new LinkedList<Move>();
                        moveSequence.add(move);
                        moveSequence.addAll(result.getP2());
                    }
                }
            }
            if (scoreOfMove > bestScore) {
                bestScore = scoreOfMove;
                bestSequence = moveSequence;
            }

            Move move = bestSequence.removeFirst();
            effectivePlays.add(move);
            game.play(move.getP1(), move.getP2());
            moves = new ArrayList<>(game.getMoves());
        }
        return new Pair<Integer, LinkedList<Move>>(game.getNumberOfMoves(), effectivePlays);
    }

    /**
     * Multithread implementation of the NMCS algorithm.
     *
     * @param level
     * @param rule
     * @return the score and the moves made to get to this score, in order
     * @throws InterruptedException
     */
    public Pair<Integer, LinkedList<Move>> multithreadNested(int level, Rule rule) throws InterruptedException {
        checkNotNull(rule);

        List<JoinFive> games = new ArrayList<JoinFive>();
        List<NMCSThread> nmcsThreads = new ArrayList<NMCSThread>();
        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < nbThreads; i++) {
            games.add(new JoinFive(rule));
            nmcsThreads.add(new NMCSThread(games.get(i), level));
            threads.add(new Thread(nmcsThreads.get(i)));
        }

        ArrayList<Move> moves = new ArrayList<>(games.get(0).getMoves());
        int bestScore = -1;
        LinkedList<Move> bestSequence = null;
        LinkedList<Move> effectivePlays = new LinkedList<>();

        while (moves.size() != 0) {
            int scoreOfMove = -1;
            LinkedList<Move> moveSequence = null;

            for (Move move : moves) {
                int i = 0;
                while (true) {
                    if (!threads.get(i).isAlive()) {
                        NMCSThread t = nmcsThreads.get(i);
                        if (scoreOfMove < t.getScore()) {
                            scoreOfMove = t.getScore();

                            moveSequence = new LinkedList<Move>();
                            moveSequence.addAll(t.getSequence());
                        }
                        t.reset();
                        threads.remove(i);
                        Move copiedMove = move.getCopy();
                        t.setMove(copiedMove);
                        threads.add(i, new Thread(t));
                        threads.get(i).start();
                        break;
                    }
                    i = (i + 1) % nbThreads;
                }
            }
            for (Thread t : threads) t.join();
            for (NMCSThread t : nmcsThreads) {
                if (scoreOfMove < t.getScore()) {
                    scoreOfMove = t.getScore();

                    moveSequence = new LinkedList<Move>();
                    moveSequence.addAll(t.getSequence());
                }
            }

            if (scoreOfMove > bestScore) {
                bestScore = scoreOfMove;
                bestSequence = moveSequence;
            }

            System.out.println("play move");
            Move move = bestSequence.removeFirst();
            effectivePlays.add(move);
            for (JoinFive game : games) {
                game.play(move.getP1().getCopy(), move.getP2().getCopy());
            }
            moves = new ArrayList<>(games.get(0).getMoves());
        }
        return new Pair<Integer, LinkedList<Move>>(games.get(0).getNumberOfMoves(), effectivePlays);
    }
}
