package model;

import lombok.Getter;

public class Level {
    @Getter private Map map;
    @Getter private SnakeHead snakeHead;
    @Getter private int appleCount;

    public Level(Map map, SnakeHead snakeHead, int appleCount){
        this.map = map;
        this.snakeHead = snakeHead;
        this.appleCount = appleCount;
    }
}
