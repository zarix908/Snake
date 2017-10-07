package model;

import lombok.Getter;
import utils.Point;

public class Portal extends GameObject {
    @Getter private Portal out;
    @Getter private int id;

    public Portal(Map map, Point location, int id) {
        super(map, location);
        this.id = id;
    }

    public static void connectPortals(Portal first, Portal second){
        if(first.id != second.id)
            throw new IllegalStateException();
        first.out = second;
        second.out = first;
    }
}
