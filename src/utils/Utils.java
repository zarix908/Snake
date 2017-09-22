package utils;

import lombok.Getter;
import model.Direction;

import java.util.HashMap;
import java.util.Map;

public final class Utils {
    private Utils() {}
    @Getter
    private final static Map<Direction, Direction> oppositeDirections = new HashMap<Direction, Direction>(){{
        put(Direction.UP, Direction.DOWN);
        put(Direction.DOWN, Direction.UP);
        put(Direction.LEFT, Direction.RIGHT);
        put(Direction.RIGHT, Direction.LEFT);
    }};


    @Getter
    private final static Map<Direction, Integer> xOffsets = new HashMap<Direction, Integer>(){{
        put(Direction.UP, 0);
        put(Direction.DOWN, 0);
        put(Direction.LEFT, -1);
        put(Direction.RIGHT, 1);
    }};


    @Getter
    private final static Map<Direction, Integer> yOffsets = new HashMap<Direction, Integer>(){{
        put(Direction.UP, -1);
        put(Direction.DOWN, 1);
        put(Direction.LEFT, 0);
        put(Direction.RIGHT, 0);
    }};
}
