package main;
import view.*;

import lombok.val;
import model.*;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) { //эта функция может быть и в другом классе
        View app = new View(new Game(2)); //Создаем экземпляр нашего приложения
        app.setVisible(true); //С этого момента приложение запущено!
        app.addKeyListener(app);
    }
}

