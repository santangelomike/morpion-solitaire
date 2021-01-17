package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Point {
    private final PointCoordinates position;
    private final JoinFive game;
    HashMap<Line, Integer> lines;
    int index; // si c'est un point initial ou le premier coup, le deuxième coup etc.

    public Point(int index, PointCoordinates position, JoinFive game) {
        lines = new HashMap<>();
        this.index = index;
        this.position = position;
        this.game = game;
    }

    public List<Line> getAllPossibleLines() {
        List<Line> possibleLines = new ArrayList<>();

        for (int i = 0; i < game.getLineLength(); i++) {
            possibleLines.add(new Line(position.goDown(i), Orientation.VERTICAL, game));
            possibleLines.add(new Line(position.goLeft(i), Orientation.HORIZONTAL, game));
            possibleLines.add(new Line(position.goLeft(i).goDown(i), Orientation.UPDIAGONAL, game));
            possibleLines.add(new Line(position.goLeft(i).goUp(i), Orientation.DOWNDIAGONAL, game));
        }

        return possibleLines;
    }

    public PointCoordinates getPosition() {
        return position;
    }

    public void addLine(Line line, int positionInLine) {
        lines.put(line, positionInLine);
    }

    public HashMap<Line, Integer> getLines() {
        return lines;
    }

    public String toString() {
        return position.toString();
    }
}
