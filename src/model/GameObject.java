package model;

import lombok.Getter;
import lombok.val;
import utils.Point;
import utils.Utils;

public abstract class GameObject {
    protected Map map;
    @Getter protected Point location;

    public GameObject(Map map, Point location) {
        this.map = map;
        this.location = location;
    }

    void swap(GameObject other) {
        if (map != other.map)
            throw new IllegalArgumentException("objects are on different maps");

        val otherLocation = other.location;
        other.location = location;
        location = otherLocation;
        map.add(other.location, other);
        map.add(location, this);
    }

    GameObject getNeighbor(Direction direction) {
        val destinationX = Utils.getXOffsets().get(direction) + location.getX();
        val destinationY = Utils.getYOffsets().get(direction) + location.getY();

        if (destinationX < 0 || destinationY < 0 || destinationY >= map.getHeight() || destinationX >= map.getWidth())
            return null;
        return map.get(destinationX, destinationY);
    }

    GameObject gedDestinationBy(Direction direction){
        val result = getNeighbor(direction);
        if(!(result instanceof Portal))
            return result;
        return ((Portal) result).getOut().gedDestinationBy(direction);
    }

    boolean isNeighbor(GameObject other) {
        val dx = location.getX() - other.location.getX();
        val dy = location.getY() - other.location.getY();
        return dx * dx + dy * dy == 1;
    }

    public Direction getDirectionTo(GameObject other){
        if(!isNeighbor(other))
            throw new IllegalArgumentException();
        val dx = location.getX() - other.location.getX();
        val dy = location.getY() - other.location.getY();
        return dx > 0 ? Direction.LEFT
                : dx < 0 ? Direction.RIGHT
                : dy > 0 ? Direction.UP
                : Direction.DOWN;
    }

    @Override
    public String toString(){
        return getClass().toString() + "(" + location.getX() + "," + location.getY() + ")";
    }
}
