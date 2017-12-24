package model;

import lombok.SneakyThrows;
import lombok.val;
import utils.Config;
import utils.Point;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class LevelGenerator {
    private static ArrayList<String> levels = new ArrayList<>();

    static {
        levels.add(
                "xxxxxxxxxxxxxxxxx\n" +
                        "x.o...x.........x\n" +
                        "x.s...x.........x\n" +
                        "x.....xxxx..x...x\n" +
                        "x...........x...x\n" +
                        "x.s...x.....x...x\n" +
                        "x.o...x.....x...x\n" +
                        "xxxxxxxxxxxxxxxxx"
        );
        levels.add(
                "xxxxxxxxxxxxxxxxx\n" +
                        "x...............x\n" +
                        "x..xxxx...xxxx..x\n" +
                        "x...............x\n" +
                        "x..xxxxxxxxxxx..x\n" +
                        "x.s...x...x.....x\n" +
                        "x.o.............x\n" +
                        "xxxxxxxxxxxxxxxxx"
        );
        levels.add(
                "xxxxxxxxxxxxxxxxx\n" +
                        "x...............x\n" +
                        "x.xxxxxxxx......x\n" +
                        "x...............x\n" +
                        "x....xxxxxxxxxxxx\n" +
                        "x.s.............x\n" +
                        "xxo....x........x\n" +
                        "xxxxxxxxxxxxxxxxx"
        );
        levels.add(
                "xxxxxxxxxxxxxxxxxxxxxxxxxx\n" +
                        "x........................x\n" +
                        "x........................x\n" +
                        "x....1...................x\n" +
                        "x........................x\n" +
                        "x........................x\n" +
                        "x........................x\n" +
                        "x........................x\n" +
                        "x...............1........x\n" +
                        "x....s...................x\n" +
                        "x....o...................x\n" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxx"
        );
    }

    @SneakyThrows
    private static Map parseLevel(String level) {
        val lines = level.split("\n");
        val width = lines[0].length();
        val result = new Map(lines.length, width);
        int snakeHeadCount = 0;
        int snakeBodyCount = 0;
        val portalManager = new PortalManager();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() != width)
                throw new InstantiationException();

            for (int j = 0; j < lines[i].length(); j++) {
                val currentChar = lines[i].toLowerCase().charAt(j);
                if (currentChar == '.')
                    result.add(j, i, new Space(result, new Point(j, i)));
                else if (currentChar == 'x')
                    result.add(j, i, new Wall(result, new Point(j, i)));
                else if (currentChar == 's') {
                    result.add(j, i, new SnakeHead(result, new Point(j, i)));
                    snakeHeadCount++;
                } else if (currentChar == '@')
                    result.add(j, i, new Apple(result, new Point(j, i)));
                else if (currentChar == 'o') {
                    result.add(j, i, new SnakeBodyPart(result, new Point(j, i), false));
                    snakeBodyCount++;
                } else if (Utils.tryParseChar(currentChar)) {
                    val portalId = Character.getNumericValue(currentChar);
                    val portal = new Portal(result, new Point(j, i), portalId);
                    portalManager.addPortal(portal);
                    result.add(j, i, portal);
                } else
                    throw new IllegalArgumentException();
            }
        }
//        if (snakeBodyCount != 1 || snakeHeadCount != 1)
//            throw new IllegalArgumentException();
        portalManager.connectPortals();
        return result;
    }

    public static ArrayList<Level> getLevels() {
        val result = new ArrayList<Level>();
        for (int i = 0; i < levels.size(); i++)
            result.add(getLevel(i));
        return result;
    }

    public static Level getLevel(int number) {
        val map = parseLevel(levels.get(number));
//        val snake = (SnakeHead) map.findFirst(SnakeHead.class);
//        val snakeBody = (SnakeBodyPart) map.findFirst(SnakeBodyPart.class);

//        if (!snake.isNeighbor(snakeBody))
//            throw new IllegalArgumentException();
//
//        snake.getBody().add(snakeBody);
//        return new Level(map, snake, Config.getApplesCount(number));

        val snakes = map.findAll(SnakeHead.class).map(e -> (SnakeHead) e).collect(Collectors.toList());
        val snakeBodies = map.findAll(SnakeBodyPart.class).map(e -> (SnakeBodyPart) e).collect(Collectors.toList());
        val i = 0;
        snakes.forEach(snakeHead -> snakeBodies.forEach(
                snakeBodyPart -> {
                    if (snakeHead.isNeighbor(snakeBodyPart))
                        snakeHead.getBody().add(snakeBodyPart);
                })
        );
        return new Level(map, snakes, Config.getApplesCount(number));
    }

    private LevelGenerator() {
    }
}
