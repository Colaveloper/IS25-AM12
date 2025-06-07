package it.polimi.ingsw.galaxytruckers.view;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    private static final Direction[] VALUES = values();

    public Direction getRight() {
        return VALUES[(this.ordinal() + 1) % Direction.values().length];
    }

    public Direction getLeft() {
        return VALUES[(this.ordinal() + (values().length-1)) % Direction.values().length];
    }

    public Direction getOpposite() {
        return VALUES[(this.ordinal() + 2) % Direction.values().length];
    }

    public static <V> Map<Direction, V> rotateDirectionMap(
            Map<Direction, V> original,
            Direction from,
            Direction to
    ) {
        int steps = computeSteps(from, to);
        Map<Direction, V> rotated = new EnumMap<>(Direction.class);

        for (Direction key : Direction.values()) {
            Direction newKey = key;
            for (int i = 0; i < steps; i++) {
                newKey = newKey.getRight();
            }
            rotated.put(newKey, original.get(key));
        }

        return rotated;
    }

    private static int computeSteps(Direction from, Direction to) {
        int dist = to.ordinal() - from.ordinal();
        if (dist < 0) dist += Direction.values().length;
        return dist;
    }

    public static Point getNeighbour(Point p, Direction direction) {
        return switch (direction) {
            case UP -> new Point(p.x, p.y - 1);
            case DOWN -> new Point(p.x, p.y + 1);
            case LEFT -> new Point(p.x - 1, p.y);
            case RIGHT -> new Point(p.x + 1, p.y);
        };
    }
}