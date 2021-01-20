package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;

public class NMCS {
	
	private static NMCS instance = null;
	
	private int nbThreads;
	
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
		if (instance == null) throw new IllegalStateException();
		return instance;
	}
	
	public Pair<Integer, LinkedList<Move>> nested(JoinFive game, int level) {
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
					game.updateBounds();
				}
			}
			else {
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
					game.updateBounds();
				}
			}
			if (scoreOfMove > bestScore) {
				bestScore = scoreOfMove;
				bestSequence = moveSequence;
			}
			
			Move move = bestSequence.removeFirst();
			effectivePlays.add(move);
			game.play(move.getP1(), move.getP2());
			moves = game.getMoves();
		}
		return new Pair<Integer, LinkedList<Move>>(game.getNumberOfMoves(), effectivePlays);
	}
	
	// pas d'appel récursif à multithreadNested car sinon ça fait trop de threads différents et vu qu'il y a que 8 coeurs dans le CPU, on a fait l'hypothèse que ça n'allait pas grandement améliorer les perfs
	public Pair<Integer, LinkedList<Move>> multithreadNested(int level, Rule rule) throws InterruptedException {
		List<JoinFive> games = new ArrayList<JoinFive>();
		List<NMCSThread> nmcsThreads = new ArrayList<NMCSThread>();
		List<Thread> threads = new ArrayList<Thread>();
		
		for (int i = 0; i < nbThreads; i++) {
			games.add(new JoinFive(rule));
			nmcsThreads.add(new NMCSThread(games.get(i), level));
			threads.add(new Thread(nmcsThreads.get(i)));
		}
		
		ArrayList<Move> moves = games.get(0).getMoves();
		int bestScore = -1;
		LinkedList<Move> bestSequence = null;
		LinkedList<Move> effectivePlays = new LinkedList<>();
		
		while (moves.size() != 0) {
			int scoreOfMove = -1;
			LinkedList<Move> moveSequence = null;
			
			for (Move move : moves) {
				int i = 0;
				while(true) {
					if (!threads.get(i).isAlive()) {
						NMCSThread t = nmcsThreads.get(i);
						if (scoreOfMove < t.getScore()) {
							scoreOfMove = t.getScore();
							
							moveSequence = new LinkedList<Move>();
							moveSequence.addAll(t.getSequence());
						}
						t.reset();
						threads.remove(i);
						Move copiedMove = new Move(move.getP1().getCopy(), move.getP2().getCopy());
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
			
			Move move = bestSequence.removeFirst();
			effectivePlays.add(move);
			for (JoinFive game : games) {
				game.play(move.getP1().getCopy(), move.getP2().getCopy());
			}
			moves = games.get(0).getMoves();
		}
		return new Pair<Integer, LinkedList<Move>>(games.get(0).getNumberOfMoves(), effectivePlays);
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
}
