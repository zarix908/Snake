package model;

import lombok.Getter;

public class Level {
    @Getter private GameObject[][] map;
    @Getter private Snake snake;

    public Level(GameObject[][] map, Snake snake){
        this.map = map;
        this.snake = snake;
    }
}
