package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.dauphine.ja.lamhandyhajar.morpionsolitaire.solvers.NMCS;

public class NMCSTest {
	@Test
	public void testNMCS1() throws InterruptedException {
		int score = NMCS.getInstance(8).multithreadNested(1, JoinFive.Rule.D).getP1();
		assertTrue(score > 57 && score < 67) ;
	}
}
