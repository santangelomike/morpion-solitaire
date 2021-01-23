package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void testAddLine() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Point p = new Point(new PointCoordinates(0, 0));
                p.addLine(new Line(new PointCoordinates(0, 1), Orientation.HORIZONTAL), 1);
            }
        });
    }
}
