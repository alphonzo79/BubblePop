package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import rowley.bubblepop.components.MovingBubble;
import rowley.bubblepop.interfaces.ScreenController;

/**
 * Created by joe on 6/20/15.
 */
public class MainScreen implements ScreenController {
    private SurfaceHolder surfaceHolder;
    private MovingBubble bubble;
    private Paint paint;

    private int width, height;

    public MainScreen(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        paint = new Paint();

        width = surfaceHolder.getSurfaceFrame().right;
        height = surfaceHolder.getSurfaceFrame().bottom;

        bubble = new MovingBubble(0, 0, width, height, width / 2, height / 2);
    }

    public void update(long deltaTime) {
        bubble.updateBubble(deltaTime);
    }

    public void present() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null) {
            paint.setARGB(255, 0, 255, 255);
            canvas.drawRect(0, 0, width, height, paint);
            paint.setARGB(255, 255, 0, 0);
            canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getBubbleRaduis(), paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
