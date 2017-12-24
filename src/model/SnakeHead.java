package model;

import lombok.Getter;
import lombok.val;
import utils.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

public class SnakeHead extends SnakePart {

    @Getter
    private Direction direction = Direction.UP;
    private ArrayList<SnakeDeathHandler> deathHandlers = new ArrayList<>();
    private ArrayList<EatAppleEventHandler> eatAppleHandlers = new ArrayList<>();
    private ArrayList<ThrewBackTailHandler> threwBackTailHAndlers = new ArrayList<>();
    @Getter
    private LinkedList<SnakeBodyPart> body = new LinkedList<>();
    private final Map<Class, Consumer<GameObject>> movementHandlers = new HashMap<>();

    public void addDeathHandler(SnakeDeathHandler handler) {
        deathHandlers.add(handler);
    }

    public void addEatAppleHandler(EatAppleEventHandler handler) {
        eatAppleHandlers.add(handler);
    }

    public void addThrewBackTailHandler(ThrewBackTailHandler handler) {
        threwBackTailHAndlers.add(handler);
    }

    private void notifyThrewbackTail() {
        for (val handler : threwBackTailHAndlers)
            handler.onThrewBackTail();
    }

    private void notifyDeath(int length) {
        for (val handler : deathHandlers)
            handler.onDeath(length);
    }

    private void notifyEatApple() {
        for (val handler : eatAppleHandlers)
            handler.onEatApple();
    }

    public SnakeHead(model.Map map, Point location) {
        super(map, location);
        movementHandlers.put(Wall.class, this::moveToWall);
        movementHandlers.put(Apple.class, this::moveToApple);
        movementHandlers.put(SnakeBodyPart.class, this::moveToSnakeBodyPart);
        movementHandlers.put(SnakeHead.class, this::moveToSnakeBodyPart);
        movementHandlers.put(Space.class, this::moveToSpace);
        movementHandlers.put(Mushroom.class, this::moveToMushroom);
    }

    public void rotate(Direction direction) {
        if (getDirectionTo(body.getLast()) == direction)
            return;
        this.direction = direction;
    }

    public void move() {
        val destination = getDestinationBy(direction);
        if (destination == null) {
            notifyDeath(getLength());
            return;
        }
        if (movementHandlers.containsKey(destination.getClass()))
            movementHandlers.get(destination.getClass()).accept(destination);
        else
            notifyDeath(getLength());
    }

    private void tryDie() {
        val length = getLength();
        while (body.size() > 1) {
            val bodyPart = body.pollFirst();
            map.add(bodyPart.location, new Space(map, bodyPart.location));
            if (bodyPart.isSafePart()) {
                notifyThrewbackTail();
                return;
            }
        }
        notifyDeath(length);
    }

    public int getLength() {
        return body.size() + 1;
    }

    private void moveToWall(GameObject wall) {
        tryDie();
    }

    private void moveToSnakeBodyPart(GameObject bodyPart) {
        tryDie();
    }

    private void moveToApple(GameObject apple) {
        moveAndAddBodyPart(apple, false);
        notifyEatApple();
    }

    private void moveToMushroom(GameObject mushroom) {
        moveAndAddBodyPart(mushroom, true);
    }

    private void moveAndAddBodyPart(GameObject object, boolean isSafeBodyPart) {
        moveToSpace(object);
        val newBodyBlock = new SnakeBodyPart(map, object.location, isSafeBodyPart);
        map.add(object.location, newBodyBlock);
        body.addFirst(newBodyBlock);
    }

    private void moveToSpace(GameObject space) {
        swap(space);
        val iter = body.descendingIterator();
        while (iter.hasNext()) {
            iter.next().swap(space);
        }
    }
}
