package model;

import utils.Point;

import java.util.Arrays;

class SnakePart extends GameObject {
    SnakePart(Map map, Point location) {
        super(map, location);
    }

    @Override
    public Direction getDirectionTo(GameObject other) {
        if (!(other instanceof SnakePart))
            return super.getDirectionTo(other);
        if (isNeighbor(other))
            return super.getDirectionTo(other);
        else
            return Arrays.stream(Direction.values())
                    .map(this::getNeighbor)
                    .filter(e -> e instanceof Portal)
                    .map(e -> ((Portal) e).getOut())
                    .filter(e -> e.isNeighbor(other))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)
                    .getDirectionTo(other);
    }
}
