package view;


import lombok.val;
import model.Direction;
import model.Game;
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
        super.paintComponent(g);     // paint parent's background
        setBackground(Color.BLACK);  // set background color for this JPanel

        for(int x=0; x < map.getWidth(); x++)
            for (int y=0; y < map.getHeight(); y++) {
                val unit = map.get(x, y);
             //   g.drawImage(
                g.setColor(Utils.getUnitsImages().get(unit.getClass()));
                g.fillRect(x * 50, y * 50, 50, 50);
            }
    }
}