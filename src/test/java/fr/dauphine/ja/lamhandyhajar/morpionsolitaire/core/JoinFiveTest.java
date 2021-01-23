package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JoinFiveTest {
	@Test
	public void testGetMoves() {
		PointCoordinates pc = new PointCoordinates(0, 0);
		Line l = new Line(pc, Orientation.HORIZONTAL);
		assertTrue(l.equals(l.getCopy()));
	}
}
