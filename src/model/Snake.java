package model;

import lombok.val;
import utils.Action;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static java.util.Map.entry;

public class Snake extends GameObject {

    private Direction direction = Direction.UP;
    private ArrayList<snakeDeathHandler> handlers = new ArrayList<>();
    private SnakeBody previous;
    private int length;
    private final Map<Class, Action<GameObject>> movementHandlers = Map.ofEntries(
            entry(Wall.class, this::moveToWall),
            entry(Apple.class, this::moveToApple),
            entry(SnakeBody.class, this::moveToSnakeBody),
            entry(Space.class, this::moveToSpace)
    );

    public void addDeathHandler(snakeDeathHandler handler) {
        handlers.add(handler);
    }

    private void notyfyDeath() {
        handlers.forEach(e -> e.onDeath(length));
    }

    public Snake(GameObject[][] map, Image image, Point location) {
        super(map, image, location);
    }

    public void rotate(Direction direction) {
        if (direction != Utils.getOppositeDirections().get(this.direction))
            this.direction = direction;
    }

    public void Move() {
        val destination = getNeighboor(direction);
        if (destination == null) {
            notyfyDeath();
            return;
        }

        movementHandlers.get(destination.getClass()).Invoke(destination);
    }

    private void moveToWall(GameObject wall) {
        notyfyDeath();
    }

    private void moveToSnakeBody(GameObject body) {
        notyfyDeath();
    }

    private void moveToApple(GameObject apple){
    }

    private void moveToSpace(GameObject space){

    }
}
