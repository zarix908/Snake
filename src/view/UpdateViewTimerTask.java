package view;

import java.util.TimerTask;

class UpdateViewTimerTask extends TimerTask {

    private View view;

    public UpdateViewTimerTask(View view){
        this.view = view;
    }

    @Override
    public void run() {
        view.getGame().makeGameIteration();
        view.repaint();
    }
}
