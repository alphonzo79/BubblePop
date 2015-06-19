package rowley.bubblepop.control;

import android.app.Activity;
import android.graphics.Point;
import android.view.SurfaceView;
import android.view.WindowManager;

import rowley.bubblepop.components.MovingBubble;

/**
 * Created by joe on 6/18/15.
 */
public class GameController {
    private SurfaceView surfaceView;
    private MovingBubble bubble;

    private Thread thread;
    private long currentTime, lastTime, deltaTime;
    private volatile boolean shouldContinue;

    public GameController(SurfaceView surfaceView, Activity activity) {
        this.surfaceView = surfaceView;

        Point size = new Point();
        WindowManager wm = activity.getWindowManager();
        wm.getDefaultDisplay().getSize(size);

        bubble = new MovingBubble(0, 0, size.x, size.y, size.x / 2, size.y / 2);
    }

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

    private void update(long deltaTime) {
        bubble.updateBubble(deltaTime);
    }

    private void draw() {
        bubble.draw(surfaceView);
    }

    private Runnable mainLooper = new Runnable() {
        @Override
        public void run() {
            lastTime = System.currentTimeMillis();
            while(shouldContinue) {
                currentTime = System.currentTimeMillis();
                deltaTime = currentTime - lastTime;
                update(deltaTime);
                draw();
                lastTime = currentTime;
            }
        }
    };
}
