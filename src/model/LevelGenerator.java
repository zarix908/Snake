package model;

import lombok.SneakyThrows;
import lombok.val;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
                "x.....x.....x...x\n" +
                "xxxxxxxxxxxxxxxxx"
        );
        levels.add(
                "xxxxxxxxxxxxxxxxx\n" +
                "x...............x\n" +
                "x..xxxx...xxxx..x\n" +
                "x...............x\n" +
                "x..xxxxxxxxxxx..x\n" +
                "x.s...x...x.....x\n" +
                "x...............x\n" +
                "xxxxxxxxxxxxxxxxx"
        );
        levels.add(
                "xxxxxxxxxxxxxxxxx\n" +
                "x...............x\n" +
                "x.xxxxxxxx......x\n" +
                "x...............x\n" +
                "x....xxxxxxxxxxxx\n" +
                "x.s.............x\n" +
                "xx.....x........x\n" +
                ".xxxxxxxxxxxxxxxx"
        );
    }

    @SneakyThrows
    private static Map parseLevel(String level) {
        val lines = level.split("\n");
        val width = lines[0].length();
        val result = new Map(lines.length, width);

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() != width)
                throw new InstantiationException();

            for (int j = 0; j < lines[i].length(); j++) {
                val currentChar = lines[i].charAt(j);
                if (currentChar == '.')
                    result.add(j, i, new Space(result, new Point(j, i)));
                else if (currentChar == 'x')
                    result.add(j, i, new Wall(result, new Point(j, i)));
                else if (currentChar == 's')
                    result.add(j, i, new Snake(result, new Point(j, i)));
                else if (currentChar == '@')
                    result.add(j, i, new Apple(result, new Point(j, i)));
                else
                    throw new IllegalArgumentException();
            }
        }
        return result;
    }

    public static ArrayList<Level> getLevels() {
        val result = new ArrayList<Level>();
        for (int i = 0; i < levels.size(); i++) {
            val map = parseLevel(levels.get(i));
            val snake = (Snake) map.findFirst(Snake.class);

            result.add(new Level(map, snake, 3 + 2 * i));
        }
        return result;
    }

    private LevelGenerator() {
    }
}
