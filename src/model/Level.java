package model;

import lombok.Getter;

public class Level {
    @Getter private Map map;
    @Getter private Snake snake;
    @Getter private int appleCount;

    public Level(Map map, Snake snake, int appleCount){
        this.map = map;
        this.snake = snake;
        this.appleCount = appleCount;
    }
}
