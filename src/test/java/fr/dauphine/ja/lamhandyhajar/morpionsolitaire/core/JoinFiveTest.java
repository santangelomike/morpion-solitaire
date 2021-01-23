package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core.JoinFive.Rule;

public class JoinFiveTest {
	@Test
	public void testPlay() {
		JoinFive game = new JoinFive(Rule.T);
		
		Point p1, p2, p3, p4, p5;
		p1 = new Point(new PointCoordinates(4, 6));
		p2 = new Point(new PointCoordinates(5, 6));
		p3 = new Point(new PointCoordinates(3, 10));
		p4 = new Point(new PointCoordinates(5, 8));
		p5 = new Point(new PointCoordinates(10, 6));

		assertTrue(game.getPossibleLines(p2).size() == 1);
		game.play(game.getPossibleLines(p1).get(0), p1);
		assertTrue(game.getPossibleLines(p2).size() == 2);
		assertTrue(game.undoPlay());
		assertTrue(!game.undoPlay());

		// example from wikipedia
		game.play(game.getPossibleLines(p3).get(0), p3);
		game.play(game.getPossibleLines(p4).get(0), p4);
		game.play(game.getPossibleLines(p5).get(0), p5);
		
		JoinFive game2 = new JoinFive(Rule.D);
		
		game2.play(game2.getPossibleLines(p1).get(0), p1);
		assertTrue(game2.getPossibleLines(p2).size() == 1);
	}
	
	@Test
	public void testGetMoves() {
		JoinFive game = new JoinFive(Rule.D);
		assertTrue(game.getMoves().size() == 28);
		
		Point p1 = new Point(new PointCoordinates(3, 10));
		game.play(game.getPossibleLines(p1).get(0), p1);
		assertTrue(game.getMoves().size() == 27);
		game.undoPlay();
		
		p1 = new Point(new PointCoordinates(4, 6));
		game.play(game.getPossibleLines(p1).get(0), p1);
		assertTrue(game.getMoves().size() == 26);
	}
}
