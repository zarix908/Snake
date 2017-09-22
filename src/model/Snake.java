package model;

import lombok.val;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

public class Snake extends GameObject {

    private Direction direction = Direction.UP;
    private ArrayList<SnakeDeathHandler> handlers = new ArrayList<>();
    private LinkedList<SnakeBody> body = new LinkedList<>();
    private final Map<Class, Consumer<GameObject>> movementHandlers = new HashMap<>();

    public void addDeathHandler(SnakeDeathHandler handler) {
        handlers.add(handler);
    }

    private void notyfyDeath() {
        handlers.forEach(e -> e.onDeath(getLength()));
    }

    public Snake(GameObject[][] map, Point location) {
        super(map, location);

        movementHandlers.put(Wall.class, this::moveToWall);
        movementHandlers.put(Apple.class, this::moveToApple);
        movementHandlers.put(SnakeBody.class, this::moveToSnakeBody);
        movementHandlers.put(Space.class, this::moveToSpace);
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

        movementHandlers.get(destination.getClass()).accept(destination);
    }

    public int getLength(){
        return body.size() + 1;
    }

    private void moveToWall(GameObject wall) {
        notyfyDeath();
    }

    private void moveToSnakeBody(GameObject body) {
        notyfyDeath();
    }

    private void moveToApple(GameObject apple){
        map[apple.location.y][apple.location.x] = this;
        val newBodyBlock = new SnakeBody(map, location);
        map[location.y][location.x] = newBodyBlock;
        body.add(newBodyBlock);
    }

    private void moveToSpace(GameObject space){
        swap(space);
        if(body.size() == 0)
            return;
        body.getFirst().swap(space);
        body.add(body.removeFirst());
    }
}
