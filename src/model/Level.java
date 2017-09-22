package model;

import lombok.Getter;

public class Level {
    @Getter private GameObject[][] map;
    @Getter private Snake snake;
    @Getter private int appleCount;

    public Level(GameObject[][] map, Snake snake, int appleCount){
        this.map = map;
        this.snake = snake;
        this.appleCount = appleCount;
    }
}
