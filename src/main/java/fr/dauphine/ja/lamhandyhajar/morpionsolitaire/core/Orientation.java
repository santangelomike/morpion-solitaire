package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to represent the orientation of a line in a JoinFive game.
 * It can either be horizontal (from left to right), vertical (bottom to top), updiagonal (going right and up) or downdiagonal (going right and down)
 */
enum Orientation {
	HORIZONTAL {
		public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
			ArrayList<PointCoordinates> points = new ArrayList<PointCoordinates>();
			for (int i = 0; i < length; i++) {
				points.add(firstPosition.goRight(i));
			}
			return points;
		}
	},
	VERTICAL {
		public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
			ArrayList<PointCoordinates> points = new ArrayList<PointCoordinates>();
			for (int i = 0; i < length; i++) {
				points.add(firstPosition.goUp(i));
			}
			return points;
		}
	},
	UPDIAGONAL {
		public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
			ArrayList<PointCoordinates> points = new ArrayList<PointCoordinates>();
			for (int i = 0; i < length; i++) {
				points.add(firstPosition.goRight(i).goUp(i));
			}
			return points;
		}
	},
	DOWNDIAGONAL {
		public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
			ArrayList<PointCoordinates> points = new ArrayList<PointCoordinates>();
			for (int i = 0; i < length; i++) {
				points.add(firstPosition.goRight(i).goDown(i));
			}
			return points;
		}
	};

	public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
		return null;
	}
}
