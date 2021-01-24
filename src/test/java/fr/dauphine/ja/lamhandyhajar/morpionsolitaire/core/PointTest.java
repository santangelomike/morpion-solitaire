package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PointTest {
    @Test
    public void testGetCopy() {
        PointCoordinates c = new PointCoordinates(1, 1);
        Point p = new Point(c);
        Point p2 = p.getCopy();
        p.setIndex(1);
        p.addLine(new Line(c, Orientation.VERTICAL), 1);
        assertTrue(p.equals(p2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLine() {
    	Point p = new Point(new PointCoordinates(0, 0));
        p.addLine(new Line(new PointCoordinates(0, 1), Orientation.HORIZONTAL), 1);
    }
}
