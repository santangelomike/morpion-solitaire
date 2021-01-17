package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers;

import java.util.ArrayList;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Line;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Pair;
import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.Point;

public class RandomSolution {
	public static JoinFive solution() {
		JoinFive game = new JoinFive(5, Rule.T);
		ArrayList<Pair<Line, Point>> l;
		do {
			l = game.getMoves();
			if (l.size() == 0) break;
			int choiceIndex = (int)(Math.random() * l.size());
			Pair<Line, Point> choice = l.get(choiceIndex);
			game.addLine(choice.getP1(), choice.getP2());
		} while (l.size() != 0);
		return game;
	}
	
	public static void main(String[] args) {
		System.out.println(solution().getNumberOfMoves());
	}
}
