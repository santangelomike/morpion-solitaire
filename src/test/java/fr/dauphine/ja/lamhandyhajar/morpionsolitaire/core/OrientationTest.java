package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.function.Executable;

public class OrientationTest {
	@Test
	public void testGetPointsPosition() {
		assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				Orientation.VERTICAL.getPointsPosition(new PointCoordinates(0, 0), 2);
			}
		});
		
		PointCoordinates p = new PointCoordinates(0, 0);
		List<PointCoordinates> points = Orientation.VERTICAL.getPointsPosition(new PointCoordinates(0, 0), JoinFive.getLineLength());
		for (int i = 0; i < JoinFive.getLineLength(); i++) {
			assertTrue(p.goUp(i).equals(points.get(i)));
		}
	}
}
