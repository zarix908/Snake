package utils;

import lombok.Getter;
import model.Direction;

import java.util.Map;
import static java.util.Map.entry;

public final class Utils {
    private Utils() {}
    @Getter
    private final static Map<Direction, Direction> oppositeDirections = Map.ofEntries(
            entry(Direction.UP, Direction.DOWN),
            entry(Direction.DOWN, Direction.UP),
            entry(Direction.LEFT, Direction.RIGHT),
            entry(Direction.RIGHT, Direction.LEFT)
    );

    @Getter
    private final static Map<Direction, Integer> xOffsets = Map.ofEntries(
            entry(Direction.UP, 0),
            entry(Direction.DOWN, 0),
            entry(Direction.LEFT, -1),
            entry(Direction.RIGHT, 1)
    );

    @Getter
    private final static Map<Direction, Integer> yOffsets = Map.ofEntries(
            entry(Direction.UP, -1),
            entry(Direction.DOWN, 1),
            entry(Direction.LEFT, 0),
            entry(Direction.RIGHT, 0)
    );

}
