package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class MoveTest {
    @Test
    public void testGetCopy() {
        PointCoordinates pc = new PointCoordinates(0, 0);
        Line line = new Line(pc, Orientation.VERTICAL);
        Move move = new Move(line, new Point(pc));
        assertTrue(move.equals(move.getCopy()));
    }
}
