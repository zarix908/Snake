package model;

import lombok.val;

import java.util.ArrayList;
import java.util.TimerTask;

public class SnakeMoveTimerTask extends TimerTask {

    private Snake snake;
    private int levelEndThreshold;
    private final ArrayList<LevelEndEventHandler> handlers = new ArrayList<>();

    public void addLevelEndHandler(LevelEndEventHandler handler){
        handlers.add(handler);
    }

    private void notifyLevelEnd(){
        for(val handler : handlers)
            handler.onLevelEnd();
    }

    public SnakeMoveTimerTask(Snake snake, int levelEndThreshold){
        this.snake = snake;
        this.levelEndThreshold = levelEndThreshold;
    }

    @Override
    public void run() {
        snake.Move();
        if(snake.getLength() > levelEndThreshold)
            notifyLevelEnd();
    }
}
