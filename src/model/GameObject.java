package model;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;

import java.awt.*;

public class GameObject {
    private GameObject[][] map;
    private Point location;

    @Getter
    private Image image;
    
    public GameObject(GameObject[][] map, Image image, Point location) {
        this.map = map;
        this.image = image;
        if (map[location.y][location.x] != this)
            throw new ExceptionInInitializerError();
        this.location = location;
    }

    @SneakyThrows
    public void Swap(GameObject other) {
        if (map != other.map)
            throw new Exception();

        map[location.y][location.x] = other;
        map[other.location.y][other.location.x] = this;
        val otherLocation = other.location;
        other.location = location;
        location = otherLocation;
    }
}
