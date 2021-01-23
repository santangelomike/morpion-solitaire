package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Used to represent the orientation of a line in a JoinFive game.
 * It can either be horizontal (from left to right), vertical (bottom to top), updiagonal (going right and up) or downdiagonal (going right and down)
 */
enum Orientation {
    HORIZONTAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            checkNotNull(firstPosition);
            checkArgument(length > 2);

            return getPositions(firstPosition, length, new PointDisplacer() {
                @Override
                public PointCoordinates displace(PointCoordinates position, int numberDisplaces) {
                    return position.goRight(numberDisplaces);
                }
            });
        }
    },
    VERTICAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            checkNotNull(firstPosition);
            checkArgument(length > 2);

            return getPositions(firstPosition, length, new PointDisplacer() {
                @Override
                public PointCoordinates displace(PointCoordinates position, int numberDisplaces) {
                    return position.goUp(numberDisplaces);
                }
            });
        }
    },
    UPDIAGONAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            checkNotNull(firstPosition);
            checkArgument(length > 2);

            return getPositions(firstPosition, length, new PointDisplacer() {
                @Override
                public PointCoordinates displace(PointCoordinates position, int numberDisplaces) {
                    return position.goRight(numberDisplaces).goUp(numberDisplaces);
                }
            });
        }
    },
    DOWNDIAGONAL {
        public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
            checkNotNull(firstPosition);
            checkArgument(length > 2);

            return getPositions(firstPosition, length, new PointDisplacer() {
                @Override
                public PointCoordinates displace(PointCoordinates position, int numberDisplaces) {
                    return position.goRight(numberDisplaces).goDown(numberDisplaces);
                }
            });
        }
    };

    /**
     * d
     *
     * @param position the first position
     * @param length   the length of the line
     * @param pd       is the way we want to get the other points from the first position
     * @return a list of the coordinates of the line
     */
    private static List<PointCoordinates> getPositions(PointCoordinates firstPosition, int length, PointDisplacer pd) {
        ArrayList<PointCoordinates> points = new ArrayList<PointCoordinates>();
        for (int i = 0; i < length; i++) {
            points.add(pd.displace(firstPosition, i));
        }
        return points;
    }

    public List<PointCoordinates> getPointsPosition(PointCoordinates firstPosition, int length) {
        return null;
    }

    private interface PointDisplacer {
        /**
         * @param position        the position of a point
         * @param numberDisplaces the number of displaces: how much do we want to move the point x times to the right, left, down or up
         * @return the resulting coordinate after it has been displaced
         */
        public PointCoordinates displace(PointCoordinates position, int numberDisplaces);
    }
}
