package model;

import lombok.val;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public final class LevelGenerator {
    private static ArrayList<String> levels = new ArrayList<>();

    static {
        levels.add(
                "xxxxxxxxxxx\n" +
                "x.........x\n" +
                "x.........x\n" +
                "x.........x\n" +
                "x.........x\n" +
                "x.s.......x\n" +
                "x.........x\n" +
                "xxxxxxxxxxx\n"
        );
    }

    private GameObject[][] parseLevel(String level){
        throw new NotImplementedException();
    }

    public LinkedList<Level> getLevels(){
        throw new NotImplementedException();
    }

    private LevelGenerator() {
    }


}
