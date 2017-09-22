package model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class SnakeBody extends GameObject {

    @Getter @Setter private SnakeBody previous;

    public SnakeBody(GameObject[][] map, Image image, Point location, SnakeBody previous) {
        super(map, image, location);
        this.previous = previous;
    }

    public SnakeBody(GameObject[][] map, Image image, Point location){
        this(map, image, location, null);
    }
}
