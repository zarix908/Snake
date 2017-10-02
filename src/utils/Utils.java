package utils;

import javafx.scene.paint.Color;
import lombok.Getter;
import model.*;

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

    @Getter
    private final static  Map<Class, Color> unitsImages = new HashMap<Class, Color>(){{
        put(Apple.class, Color.RED);
        put(Snake.class, Color.GRAY);
        put(SnakeBody.class, Color.GREEN);
        put(Space.class, Color.WHITE);
        put(Wall.class, Color.BLACK);
    }};
}
