package model;

import lombok.val;

import java.util.ArrayList;
import java.util.TimerTask;

public class SnakeMoveTimerTask extends TimerTask {

    private Snake snake;
    private int levelEndTreshold;
    private final ArrayList<LeveEndlEventHandler> handlers = new ArrayList<>();

    public void addLevelEndHandler(LeveEndlEventHandler handler){
        handlers.add(handler);
    }

    private void notyfyLevelEnd(){
        for(val handler : handlers)
            handler.onLevelEnd();
    }

    public SnakeMoveTimerTask(Snake snake, int levelEndTreshold){
        this.snake = snake;
        this.levelEndTreshold = levelEndTreshold;
    }

    @Override
    public void run() {
        snake.Move();
        if(snake.getLength() >= levelEndTreshold)
            notyfyLevelEnd();
    }
}
