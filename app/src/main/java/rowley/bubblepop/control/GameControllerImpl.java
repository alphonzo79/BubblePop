package rowley.bubblepop.control;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import rowley.bubblepop.inputhandling.MultiTouchHandler;
import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.ScreenController;
import rowley.bubblepop.interfaces.TouchHandler;

/**
 * Created by joe on 6/18/15.
 */
public class GameControllerImpl implements GameController {

    private ScreenController screenController;

    private SurfaceView surfaceView;

    private Thread thread;
    private long currentTime, lastTime, deltaTime;
    private volatile boolean shouldContinue;

    private TouchHandler touchHandler;

    public GameControllerImpl(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        touchHandler = new MultiTouchHandler(surfaceView, 1.0f, 1.0f);
    }

    @Override
    public void onActivityResume(Activity activity) {
        shouldContinue = true;
        thread = new Thread(mainLooper);
        thread.start();
    }

    @Override
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

    @Override
    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return surfaceView.getHolder();
    }

    private Runnable mainLooper = new Runnable() {
        @Override
        public void run() {
            lastTime = System.currentTimeMillis();
            while(shouldContinue) {
                currentTime = System.currentTimeMillis();
                deltaTime = currentTime - lastTime;
                if(screenController != null) {
                    screenController.update(deltaTime, GameControllerImpl.this);
                    screenController.present(surfaceView.getHolder());
                }
                lastTime = currentTime;
            }
        }
    };

    @Override
    public TouchHandler getTouchHandler() {
        return touchHandler;
    }
}
