package rowley.bubblepop.control;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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
    private Activity context;

    private ScreenController screenController;

    private SurfaceView surfaceView;

    private Thread thread;
    private long currentTime, lastTime, deltaTime;
    private volatile boolean shouldContinue;

    private TouchHandler touchHandler;
    private SoundPool soundPool;

    public GameControllerImpl(SurfaceView surfaceView, Activity activity) {
        this.context = activity;
        this.surfaceView = surfaceView;
        touchHandler = new MultiTouchHandler(surfaceView, 1.0f, 1.0f);
        if(Build.VERSION.SDK_INT < 21) {
            soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        } else {
            soundPool = new SoundPool.Builder().setMaxStreams(20).setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()).build();
        }
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
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

    @Override
    public SoundPool getSoundPool() {
        return soundPool;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
