package model;

import lombok.Getter;
import lombok.val;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class Map {
    private GameObject[][] map;
    @Getter private int height;
    @Getter private int width;

    public Map(int height, int width){
        map = new GameObject[height][width];
        this.height = height;
        this.width = width;
    }

    public void add(int x, int y, GameObject gameObject){
        val location = gameObject.location;
        if(location.x != x || location.y != y || gameObject.map != this)
            throw new IllegalArgumentException();
        map[y][x] = gameObject;
    }

    public void add(Point point, GameObject gameObject){
        add(point.x, point.y, gameObject);
    }

    public GameObject get(int x, int y){
        return map[y][x];
    }

    public GameObject get(Point point){
        return get(point.x, point.y);
    }

    public GameObject findFirst(Class cls){
        return Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(e -> e.getClass() == cls)
                .findFirst()
                .orElse(null);
    }

    public Stream<GameObject> toStream(){
        return Arrays.stream(map).flatMap(Arrays::stream);
    }
}
