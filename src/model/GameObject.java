package model;

import lombok.val;
import utils.Utils;

import java.awt.*;

public class GameObject {
    protected Map map;
    protected Point location;

    public GameObject(Map map, Point location) {
        this.map = map;
        this.location = location;
    }

    protected void swap(GameObject other) {
        if (map != other.map)
            throw new IllegalArgumentException("objects are on different maps");

        val otherLocation = other.location;
        other.location = location;
        location = otherLocation;
        map.add(other.location, other);
        map.add(location, this);

    }

    protected GameObject getNeighbor(Direction direction) {
        val destinationX = Utils.getXOffsets().get(direction) + location.x;
        val destinationY = Utils.getYOffsets().get(direction) + location.y;

        if (destinationX < 0 || destinationY < 0 || destinationY >= map.getHeight() || destinationX >= map.getWidth())
            return null;
        return map.get(destinationX, destinationY);
    }

    protected boolean isNeighboor(GameObject other) {
        val dx = location.x - other.location.x;
        val dy = location.y - other.location.y;
        return dx * dx + dy * dy == 1;
    }

    public Direction getDirectionTo(GameObject other){
        if(!isNeighboor(other))
            throw new IllegalArgumentException();
        val dx = location.x - other.location.x;
        val dy = location.y - other.location.y;
        return dx > 0 ? Direction.LEFT
                : dx < 0 ? Direction.RIGHT
                : dy > 0 ? Direction.UP
                : Direction.DOWN;
    }

    @Override
    public String toString(){
        return getClass().toString() + "(" + location.x + "," + location.y + ")";
    }
}
