package model;

import lombok.Getter;
import utils.Point;

public class SnakeBodyPart extends SnakePart {
    @Getter private boolean isSafePart;
    public SnakeBodyPart(Map map, Point location, boolean isSafePart) {
        super(map, location);
        this.isSafePart = isSafePart;
    }
}
