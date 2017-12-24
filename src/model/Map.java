package model;

import lombok.Getter;
import lombok.val;
import utils.Point;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        if(location.getX() != x || location.getY() != y || gameObject.map != this)
            throw new IllegalArgumentException();
        map[y][x] = gameObject;
    }

    public void add(Point point, GameObject gameObject){
        add(point.getX(), point.getY(), gameObject);
    }

    public GameObject get(int x, int y){
        return map[y][x];
    }

    public GameObject get(Point point){
        return get(point.getX(), point.getY());
    }

    public GameObject findFirst(Class cls){
        return Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(e -> e.getClass() == cls)
                .findFirst()
                .orElse(null);
    }

    public Stream<GameObject> findAll(Class cls){
        return Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(e -> e.getClass() == cls);
//                .collect(Collectors.toList());
    }

    public Stream<GameObject> toStream(){
        return Arrays.stream(map).flatMap(Arrays::stream);
    }
}
