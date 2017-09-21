package model;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends GameObject {

    private Direction direction = Direction.UP;
    private ArrayList<snakeDeathHandler> handlers = new ArrayList<>();
    private GameObject previous;
    private int length;

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

    }
}
