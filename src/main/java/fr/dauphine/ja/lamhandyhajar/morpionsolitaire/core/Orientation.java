package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.List;

enum Orientation {
    HORIZONTAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            ArrayList<PointCoordinates> points = new ArrayList<>();
            // TODO: check if there is a point out of bounds, if it's the case return empty
            // list
            for (int i = 0; i < length; i++) {
                points.add(firstPosition.goRight(i));
            }
            return points;
        }
    },
    VERTICAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            ArrayList<PointCoordinates> points = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                points.add(firstPosition.goUp(i));
            }
            return points;
        }
    },
    UPDIAGONAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            ArrayList<PointCoordinates> points = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                points.add(firstPosition.goRight(i).goUp(i));
            }
            return points;
        }
    },
    DOWNDIAGONAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            ArrayList<PointCoordinates> points = new ArrayList<>();
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
