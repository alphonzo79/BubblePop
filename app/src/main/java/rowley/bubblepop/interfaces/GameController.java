package rowley.bubblepop.interfaces;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;
import android.view.SurfaceHolder;

/**
 * Created by joe on 6/21/15.
 */
public interface GameController {
    public TouchHandler getTouchHandler();
    public void setScreenController(ScreenController newScreen);
    public void onActivityPause(Activity activity);
    public void onActivityResume(Activity activity);
    public SurfaceHolder getSurfaceHolder();
    public SoundPool getSoundPool();
    public Context getContext();
}
