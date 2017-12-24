package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Level {
    @Getter private Map map;
    @Getter private SnakeHead snakeHead;
    @Getter private ArrayList<SnakeHead> snakeHeads;
    @Getter private int appleCount;

    public Level(Map map, SnakeHead snakeHead, int appleCount){
        this.map = map;
        this.snakeHead = snakeHead;
        this.appleCount = appleCount;
    }

    public Level(Map map, List<SnakeHead> snakeHeads, int appleCount){
        this.map = map;
        this.snakeHead = snakeHeads.get(0);
        this.snakeHeads = (ArrayList<SnakeHead>) snakeHeads;
        this.appleCount = appleCount;
    }
}
