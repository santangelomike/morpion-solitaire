package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointCoordinatesTest {
	@Test
	public void testGetCopy() {
		PointCoordinates c = new PointCoordinates(1, 1);
		PointCoordinates c2 = c.getCopy();
		assertTrue(c != c2 && c.equals(c2));
	}
}
