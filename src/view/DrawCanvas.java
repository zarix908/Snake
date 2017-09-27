package view;


import lombok.val;
import model.Direction;
import model.Game;
import utils.Config;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DrawCanvas extends JPanel {
    private Game game;

    public DrawCanvas(Game game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics g) {

        val map = game.getCurrentLevel().getMap();
        val size = Config.GAME_OBJECT_SIZE;

        super.paintComponent(g);
        setBackground(Color.BLACK);

        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                val gameObject = map.get(x, y);
                g.setColor(Utils.getUnitsImages().get(gameObject.getClass()));
                g.fillRect(x * size, y * size, size, size);
            }
    }
}