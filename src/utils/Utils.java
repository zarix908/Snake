package utils;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.Getter;
import lombok.val;
import model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static java.lang.Math.abs;

public final class Utils {
    private Utils() {
    }

    @Getter
    private final static String playerName = "Daniyar";

    @Getter
    private final static UUID id = UUID.randomUUID();

    @Getter
    private final static Map<Direction, Direction> oppositeDirections = new HashMap<Direction, Direction>() {{
        put(Direction.UP, Direction.DOWN);
        put(Direction.DOWN, Direction.UP);
        put(Direction.LEFT, Direction.RIGHT);
        put(Direction.RIGHT, Direction.LEFT);
    }};


    @Getter
    private final static Map<Direction, Integer> xOffsets = new HashMap<Direction, Integer>() {{
        put(Direction.UP, 0);
        put(Direction.DOWN, 0);
        put(Direction.LEFT, -1);
        put(Direction.RIGHT, 1);
    }};


    @Getter
    private final static Map<Direction, Integer> yOffsets = new HashMap<Direction, Integer>() {{
        put(Direction.UP, -1);
        put(Direction.DOWN, 1);
        put(Direction.LEFT, 0);
        put(Direction.RIGHT, 0);
    }};

    @Getter
    private final static Map<Pair<Direction, Direction>, Integer> anglesForTurnBodyParts =
            new HashMap<Pair<Direction, Direction>, Integer>() {{
                put(new Pair<>(Direction.RIGHT, Direction.UP), -90);
                put(new Pair<>(Direction.RIGHT, Direction.DOWN), 270);
                put(new Pair<>(Direction.LEFT, Direction.UP), 90);
                put(new Pair<>(Direction.LEFT, Direction.DOWN), -270);
                put(new Pair<>(Direction.UP, Direction.RIGHT), 180);
                put(new Pair<>(Direction.UP, Direction.LEFT), -180);
                put(new Pair<>(Direction.DOWN, Direction.RIGHT), -360);
                put(new Pair<>(Direction.DOWN, Direction.LEFT), 360);
            }};

    @Getter
    private final static Map<Direction, Integer> anglesForStraightBodyParts =
            new HashMap<Direction, Integer>(){{
                put(Direction.UP, 270);
                put(Direction.DOWN, 90);
                put(Direction.LEFT, 180);
                put(Direction.RIGHT, 360);
            }};

    public enum SnakeColor {
        BLUE("blue"),
        GREEN("green"),
        ORANGE("orange"),
        PURPLE("purple"),
        RED("red"),
        YELLOW("yellow");

        private final String snakeColor;

        SnakeColor(String snakeColor) {
            this.snakeColor = snakeColor;
        }
    }

    public enum SnakeBodyPartsVarieties {
        HEAD("Head"),
        TORSO("Body"),
        TAIL("Tail");

        private final String snakeBodyPartsVarieties;

        SnakeBodyPartsVarieties(String snakeBodyPartsVarieties) {
            this.snakeBodyPartsVarieties = snakeBodyPartsVarieties;
        }
    }


    //TODO old code
   /* @Getter
    private final static  Map<Class, SnakeColor> unitsImages = new HashMap<Class, SnakeColor>(){{
        put(Apple.class, SnakeColor.RED);
        put(SnakeHead.class, SnakeColor.GRAY);
        put(SnakeBodyPart.class, SnakeColor.GREEN);
        put(Space.class, SnakeColor.WHITE);
        put(Wall.class, SnakeColor.BLACK);
        put(Mushroom.class, SnakeColor.BROWN);
    }};*/

    private final static  Map<Class, Color> unitsImages = new HashMap<Class, Color>(){{
        put(Apple.class, Color.RED);
        put(SnakeHead.class, Color.GRAY);
        put(SnakeBodyPart.class, Color.GREEN);
        put(Space.class, Color.WHITE);
        put(Wall.class, Color.BLACK);
        put(Mushroom.class, Color.BROWN);
        put(Portal.class, Color.BLUEVIOLET);
    }};

    public static boolean tryParseChar(Character value){
        try {
            Character.getNumericValue(value);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
