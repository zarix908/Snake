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
    private ArrayList<SnakeDeathHandler> deathHandlers = new ArrayList<>();
    private ArrayList<EatAppleEventHandler> eatAppleHandlers = new ArrayList<>();
    private LinkedList<SnakeBody> body = new LinkedList<>();
    private final Map<Class, Consumer<GameObject>> movementHandlers = new HashMap<>();

    public void addDeathHandler(SnakeDeathHandler handler) {
        deathHandlers.add(handler);
    }
    public void addEatAppleHandler(EatAppleEventHandler handler) {
        eatAppleHandlers.add(handler);
    }

    private void notifyDeath() {
        for(val handler : deathHandlers)
            handler.onDeath(getLength());
    }
    private void notifyEatApple() {
        for(val handler : eatAppleHandlers)
            handler.onEatApple();
    }

    public Snake(model.Map map, Point location) {
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
            notifyDeath();
            return;
        }

        movementHandlers.get(destination.getClass()).accept(destination);
    }

    public int getLength(){
        return body.size() + 1;
    }

    private void moveToWall(GameObject wall) {
        notifyDeath();
    }

    private void moveToSnakeBody(GameObject body) {
        notifyDeath();
    }

    private void moveToApple(GameObject apple){
        val oldLocation = location;
        swap(apple);
        val newBodyBlock = new SnakeBody(map, oldLocation);
        map.add(oldLocation.x, oldLocation.y, newBodyBlock);
        body.add(newBodyBlock);
        notifyEatApple();
    }

    private void moveToSpace(GameObject space){
        swap(space);
        if(body.size() == 0)
            return;
        body.getFirst().swap(space);
        body.add(body.removeFirst());
    }
}
