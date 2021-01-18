package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

public class RandomSolution {
	public static Pair<Integer, LinkedList<Pair<Line, Point>>> sample(JoinFive game) {
		ArrayList<Pair<Line, Point>> l;
		LinkedList<Pair<Line, Point>> plays = new LinkedList<>();
		int nbPlays = 0;
		
		do {
			l = game.getMoves();
			if (l.size() == 0) break;
			int choiceIndex = (int)(Math.random() * l.size());
			Pair<Line, Point> choice = l.get(choiceIndex);
			plays.add(choice);
			game.play(choice.getP1(), choice.getP2());
			nbPlays++;
		} while (l.size() != 0);
		
		int score = game.getNumberOfMoves();
		for (int i = 0; i < nbPlays; i++) game.undoPlay();
		
		return new Pair<Integer, LinkedList<Pair<Line, Point>>>(score, plays);
	}
	
	public static void main(String[] args) {
		JoinFive game = new JoinFive(Rule.D);
		System.out.println(sample(game).getP1());
	}
}
