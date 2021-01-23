package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.LinkedList;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Move;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;

/**
 * Used for NMCS multithread implementation. This class represents a lower level search.
 * The idea is to look at the variables score and sequence which give the result of the run() method.
 */
public class NMCSThread implements Runnable {
	
	private JoinFive game;
	private int level;
	private Move move;
	
	private int score;
	private LinkedList<Move> sequence;
	
	public int getScore() {
		return score;
	}
	
	public LinkedList<Move> getSequence() {
		return sequence;
	}
	
	public void setMove(Move move) {
		this.move = move;
	}
	
	public Move getMove() {
		return move;
	}
	
	public void setGame(JoinFive game) {
		this.game = game;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public NMCSThread(JoinFive game, int level) {
		this.game = game;
		this.level = level;
		sequence = new LinkedList<Move>();
		score = -1;
	}
	
	/**
	 * Resets this instance to make it reusable instead of instantiating a new instance.
	 */
	public void reset() {
		score = -1;
		sequence = new LinkedList<Move>();
		move = null;
	}
	
	@Override
	public void run() {
		sequence.add(move);
		game.play(move.getP1(), move.getP2());
		if (level == 1) {
			Pair<Integer, LinkedList<Move>> result = RandomSolution.sample(game);
			score = result.getP1();
			sequence.addAll(result.getP2());
			
			game.undoPlay();
		}
		else {
			Pair<Integer, LinkedList<Move>> result = NMCS.getInstance().nested(game, level - 1);
			for (int i = 0; i < result.getP2().size(); i++) game.undoPlay();
			game.undoPlay();
			
			score = result.getP1();
			sequence.addAll(result.getP2());
		}
		game.updateBounds();
	}

}
