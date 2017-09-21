package model;

import lombok.Getter;

public class Level {
    @Getter private GameObject[][] map;

    public Level(int height, int width){
        map = new GameObject[height][width];
    }
}
