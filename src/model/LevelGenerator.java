package model;

import lombok.SneakyThrows;
import lombok.val;


import java.awt.*;
import java.util.ArrayList;

public final class LevelGenerator {
    private static ArrayList<String> levels = new ArrayList<>();

    static {
        levels.add(
                "xxxxxxxxxxxxxxxxx\n" +
                        "x.....x.........x\n" +
                        "x.....x.........x\n" +
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
                        ".xxxxxxxxxxxxxxxx"
        );
    }

    @SneakyThrows
    private static Map parseLevel(String level) {
        val lines = level.split("\n");
        val width = lines[0].length();
        val result = new Map(lines.length, width);
        int snakeHeadCount = 0;
        int snakeBodyCount = 0;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() != width)
                throw new InstantiationException();

            for (int j = 0; j < lines[i].length(); j++) {
                val currentChar = lines[i].charAt(j);
                if (currentChar == '.')
                    result.add(j, i, new Space(result, new Point(j, i)));
                else if (currentChar == 'x')
                    result.add(j, i, new Wall(result, new Point(j, i)));
                else if (currentChar == 's') {
                    result.add(j, i, new Snake(result, new Point(j, i)));
                    snakeHeadCount++;
                } else if (currentChar == '@')
                    result.add(j, i, new Apple(result, new Point(j, i)));
                else if (currentChar == 'o') {
                    result.add(j, i, new SnakeBody(result, new Point(j, i)));
                    snakeBodyCount++;
                } else
                    throw new IllegalArgumentException();
            }
        }
        if (snakeBodyCount != 1 || snakeHeadCount != 1)
            throw new IllegalArgumentException();
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
        val snake = (Snake) map.findFirst(Snake.class);
        val snakeBody = (SnakeBody) map.findFirst(SnakeBody.class);

        if (!snake.isNeighboor(snakeBody))
            throw new IllegalArgumentException();

        snake.getBody().add(snakeBody);
        return new Level(map, snake, 3 + 2 * number);
    }

    private LevelGenerator() {
    }
}
