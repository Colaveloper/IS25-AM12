package it.polimi.ingsw.galaxytruckers.view;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Represents the four cardinal directions in the Galaxy Truckers game.
 * This enum provides utility methods for direction manipulation, rotation,
 * and calculating neighboring positions on a grid.
 */
public enum Direction {
    /** Represents upward direction (north) */
    UP,
    /** Represents rightward direction (east) */
    RIGHT,
    /** Represents downward direction (south) */
    DOWN,
    /** Represents leftward direction (west) */
    LEFT;

    /** Cached array of all direction values for efficient access */
    private static final Direction[] VALUES = values();

    /**
     * Gets the direction that is 90 degrees clockwise from this direction.
     *
     * @return The direction to the right of this direction
     */
    public Direction getRight() {
        return VALUES[(this.ordinal() + 1) % Direction.values().length];
    }

    /**
     * Gets the direction that is 90 degrees counterclockwise from this direction.
     *
     * @return The direction to the left of this direction
     */
    public Direction getLeft() {
        return VALUES[(this.ordinal() + (values().length - 1)) % Direction.values().length];
    }

    /**
     * Gets the direction that is 180 degrees from this direction (opposite).
     *
     * @return The opposite direction
     */
    public Direction getOpposite() {
        return VALUES[(this.ordinal() + 2) % Direction.values().length];
    }

    /**
     * Converts this direction to an angle in degrees.
     * UP = 0°, RIGHT = 90°, DOWN = 180°, LEFT = 270°
     *
     * @return The angle in degrees corresponding to this direction
     */
    public int getAngle() {
        return switch (this) {
            case UP -> 0;
            case RIGHT -> 90;
            case DOWN -> 180;
            case LEFT -> 270;
        };
    }

    /**
     * Rotates a map of direction-to-value pairs from one direction to another.
     * This is useful for rotating component orientations in the game.
     *
     * @param <V>      The type of values in the map
     * @param original The original direction-to-value map
     * @param from     The source direction
     * @param to       The target direction
     * @return A new map with rotated direction keys
     */
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

    /**
     * Computes the number of 90-degree clockwise rotations needed to go from one direction to another.
     *
     * @param from The starting direction
     * @param to   The target direction
     * @return The number of clockwise steps needed
     */
    private static int computeSteps(Direction from, Direction to) {
        int dist = to.ordinal() - from.ordinal();
        if (dist < 0) dist += Direction.values().length;
        return dist;
    }

    /**
     * Gets the neighboring point in a specified direction from a given point.
     * This is useful for grid-based navigation in the game.
     *
     * @param p         The starting point
     * @param direction The direction to move
     * @return A new point that is one unit away in the specified direction
     */
    public static Point getNeighbour(Point p, Direction direction) {
        return switch (direction) {
            case UP -> new Point(p.x, p.y - 1);
            case DOWN -> new Point(p.x, p.y + 1);
            case LEFT -> new Point(p.x - 1, p.y);
            case RIGHT -> new Point(p.x + 1, p.y);
        };
    }
}
