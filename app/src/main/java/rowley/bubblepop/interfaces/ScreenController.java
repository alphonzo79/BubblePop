package rowley.bubblepop.interfaces;

import android.view.SurfaceHolder;

/**
 * Created by joe on 6/20/15.
 */
public interface ScreenController {
    public void update(long deltaTime, GameController gameController);
    public void present(SurfaceHolder surfaceHolder);
}
