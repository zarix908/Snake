package main;

import view.*;

import lombok.val;
import model.*;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        View app = new View(new Game(3));
        app.setVisible(true);
        app.addKeyListener(app);
    }
}

