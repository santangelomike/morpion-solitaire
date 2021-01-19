package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.LinkedList;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

public class NMCSThread implements Runnable {
	
	private JoinFive game;
	private int level;
	private Pair<Line, Point> move;
	
	private int score;
	private LinkedList<Pair<Line, Point>> sequence;
	
	public int getScore() {
		return score;
	}
	
	public LinkedList<Pair<Line, Point>> getSequence() {
		return sequence;
	}
	
	public void setMove(Pair<Line, Point> move) {
		this.move = move;
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
	}
	
	@Override
	public void run() {
		if (level == 1) {
			game.play(move.getP1(), move.getP2());
			
			Pair<Integer, LinkedList<Pair<Line, Point>>> result = RandomSolution.sample(game);
			score = result.getP1();
			sequence = result.getP2();
			
			game.undoPlay();
		}
		else {
			game.play(move.getP1(), move.getP2());
			Pair<Integer, LinkedList<Pair<Line, Point>>> result = NMCS.nested(game, level - 1);
			game.undoPlay();
			
			score = result.getP1();
			sequence = new LinkedList<Pair<Line, Point>>();
			sequence.add(move);
			sequence.addAll(result.getP2());
		}
		game.updateBounds();
	}

}
