package model;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import utils.Utils;

import java.awt.*;

public class GameObject {
    protected GameObject[][] map;
    protected Point location;

    @Getter
    private Image image;

    public GameObject(GameObject[][] map, Image image, Point location) {
        this.map = map;
        this.image = image;
        if (map[location.y][location.x] != this)
            throw new ExceptionInInitializerError("invalid location");
        val height = map.length;
        for(val row : map)
            if(row.length != height)
                throw new ExceptionInInitializerError("map must be square");

        this.location = location;
    }

    @SneakyThrows
    protected void Swap(GameObject other) {
        if (map != other.map)
            throw new IllegalArgumentException("objects are on different maps");

        map[location.y][location.x] = other;
        map[other.location.y][other.location.x] = this;
        val otherLocation = other.location;
        other.location = location;
        location = otherLocation;
    }

    protected GameObject getNeighboor(Direction direction){
        val destinationX = Utils.getXOffsets().get(direction) + location.x;
        val destinationY = Utils.getYOffsets().get(direction) + location.y;

        if(destinationX < 0 || destinationY < 0 || destinationY >= map.length || destinationX >= map[0].length)
            return null;
        return map[destinationY][destinationX];
    }
}
