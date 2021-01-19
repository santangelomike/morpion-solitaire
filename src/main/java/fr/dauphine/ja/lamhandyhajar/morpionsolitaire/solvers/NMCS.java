package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

public class NMCS {
	
	//TODO: multithread
	public static Pair<Integer, LinkedList<Pair<Line, Point>>> nested(JoinFive game, int level) {
		ArrayList<Pair<Line, Point>> moves = game.getMoves();
		int bestScore = -1;
		LinkedList<Pair<Line, Point>> bestSequence = null;
		LinkedList<Pair<Line, Point>> effectivePlays = new LinkedList<>();
		
		while (moves.size() != 0) {
			int scoreOfMove = -1;
			LinkedList<Pair<Line, Point>> moveSequence = null;
			
			if (level == 1) {
				for (Pair<Line, Point> move : moves) {
					game.play(move.getP1(), move.getP2());
					
					Pair<Integer, LinkedList<Pair<Line, Point>>> result = RandomSolution.sample(game);
					int score = result.getP1();
					
					game.undoPlay();
					
					if (scoreOfMove < score) {
						scoreOfMove = score;
						
						moveSequence = new LinkedList<Pair<Line, Point>>();
						moveSequence.add(move);
						moveSequence.addAll(result.getP2());
					}
					game.updateBounds();
				}
			}
			else {
				if (level == 2) System.out.println(moves.size());
				for (Pair<Line, Point> move : moves) {
					int nbMoves = game.getNumberOfMoves();
					game.play(move.getP1(), move.getP2());
					Pair<Integer, LinkedList<Pair<Line, Point>>> result = nested(game, level - 1);
					LinkedList<Pair<Line, Point>> movesToCancel = result.getP2();
					for (int i = 0; i < movesToCancel.size(); i++) game.undoPlay();
					game.undoPlay();
					if (level == 2) System.out.println(game.getNumberOfMoves() - nbMoves);
					
					int score = result.getP1();
					if (scoreOfMove < score) {
						scoreOfMove = score;
						
						moveSequence = new LinkedList<Pair<Line, Point>>();
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
			
			if (level == 2) System.out.println("play move");
			Pair<Line, Point> move = bestSequence.removeFirst();
			effectivePlays.add(move);
			game.play(move.getP1(), move.getP2());
			moves = game.getMoves();
		}
		return new Pair<Integer, LinkedList<Pair<Line, Point>>>(game.getNumberOfMoves(), effectivePlays);
	}
	
	private static int nbThreads = 4;
	
	public static Pair<Integer, LinkedList<Pair<Line, Point>>> multithreadNested(JoinFive game, int level) {
		List<NMCSThread> nmcsThreads = new ArrayList<NMCSThread>();
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < nbThreads; i++) {
			JoinFive newGame = new JoinFive(Rule.D);
			nmcsThreads.add(new NMCSThread(newGame, level));
			threads.add(new Thread(nmcsThreads.get(i)));
		}
		
		ArrayList<Pair<Line, Point>> moves = game.getMoves();
		int bestScore = -1;
		LinkedList<Pair<Line, Point>> bestSequence = null;
		LinkedList<Pair<Line, Point>> effectivePlays = new LinkedList<>();
		
		while (moves.size() != 0) {
			int scoreOfMove = -1;
			LinkedList<Pair<Line, Point>> moveSequence = null;
			
			for (Pair<Line, Point> move : moves) {
				// iterate all the threads to look if there is a thread available
				// if there is, update scoreOfMove and moveSequence + restart thread and break
				for (int i = 0; i < nbThreads; i++) {
					if (!threads.get(i).isAlive()) {
						NMCSThread t = nmcsThreads.get(i);
						if (scoreOfMove < t.getScore()) {
							scoreOfMove = t.getScore();
							
							moveSequence = new LinkedList<Pair<Line, Point>>();
							moveSequence.add(move);
							moveSequence.addAll(t.getSequence());
						}
						t.setMove(move);
						threads.get(i).start();
						break;
					}
				}
				
			}
			if (scoreOfMove > bestScore) {
				bestScore = scoreOfMove;
				bestSequence = moveSequence;
			}
			
			Pair<Line, Point> move = bestSequence.removeFirst();
			effectivePlays.add(move);
			game.play(move.getP1(), move.getP2());
			moves = game.getMoves();
		}
		return new Pair<Integer, LinkedList<Pair<Line, Point>>>(game.getNumberOfMoves(), effectivePlays);
	}
	
	public static void main(String[] args) {
		JoinFive game = new JoinFive(Rule.D);
		NMCS.nested(game, 2);
		System.out.println(game.getNumberOfMoves());
	}
}
