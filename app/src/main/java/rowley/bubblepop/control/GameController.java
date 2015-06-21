package rowley.bubblepop.control;

import android.app.Activity;

import rowley.bubblepop.interfaces.ScreenController;

/**
 * Created by joe on 6/18/15.
 */
public class GameController {

    private ScreenController screenController;

    private Thread thread;
    private long currentTime, lastTime, deltaTime;
    private volatile boolean shouldContinue;

    public void onActivityResume(Activity activity) {
        shouldContinue = true;
        thread = new Thread(mainLooper);
        thread.start();
    }

    public void onActivityPause(Activity activity) {
        shouldContinue = false;
        boolean isStopped = false;
        while(!isStopped) {
            try {
                thread.join();
                isStopped = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    private Runnable mainLooper = new Runnable() {
        @Override
        public void run() {
            lastTime = System.currentTimeMillis();
            while(shouldContinue) {
                currentTime = System.currentTimeMillis();
                deltaTime = currentTime - lastTime;
                if(screenController != null) {
                    screenController.update(deltaTime);
                    screenController.present();
                }
                lastTime = currentTime;
            }
        }
    };
}
