package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class OrientationTest {
    @Test(expected = IllegalArgumentException.class)
    public void testGetPointsPosition() {
    	Orientation.VERTICAL.getPointsPosition(new PointCoordinates(0, 0), 2);
    }
    
    @Test
    public void testGetPointsPosition2() {
    	PointCoordinates p = new PointCoordinates(0, 0);
        List<PointCoordinates> points = Orientation.VERTICAL.getPointsPosition(new PointCoordinates(0, 0), JoinFive.getLineLength());
        for (int i = 0; i < JoinFive.getLineLength(); i++) {
            assertTrue(p.goUp(i).equals(points.get(i)));
        }
    }
}
