package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

public class NMCS {
	
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
				for (Pair<Line, Point> move : moves) {
					game.play(move.getP1(), move.getP2());
					Pair<Integer, LinkedList<Pair<Line, Point>>> result = nested(game, level - 1);
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
			if (scoreOfMove > bestScore) {
				bestScore = scoreOfMove;
				bestSequence = moveSequence;
			}
			
			Pair<Line, Point> move = bestSequence.removeFirst(); // NullPointerException when level = 3
			effectivePlays.add(move);
			System.out.println(move.getP2());
			game.play(move.getP1(), move.getP2());
			moves = game.getMoves();
		}
		return new Pair<Integer, LinkedList<Pair<Line, Point>>>(bestScore, effectivePlays);
	}
	
	public static void main(String[] args) {
		JoinFive game = new JoinFive(Rule.D);
		NMCS.nested(game, 3);
		System.out.println(game.getNumberOfMoves());
	}
}
