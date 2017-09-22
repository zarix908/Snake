package main;

import lombok.val;
import model.*;

import java.awt.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        val map = new GameObject[5][5];
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Space(map, new Point(j, i));
            }

        val snake = new Snake(map, new Point(1, 4));
        map[4][1] = snake;
        map[2][1] = new Apple(map, new Point(1, 2));
        val level = new Level(map, snake, 1);
        val maps = new ArrayList<Level>();
        maps.add(level);
        val game = new Game(maps, 1);
        game.addEndGameHandler((l, sl, d) -> System.out.println(l + ", " + sl + ", " + d));
        game.startGame();

    }
}
