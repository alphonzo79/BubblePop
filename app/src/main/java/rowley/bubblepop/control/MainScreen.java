package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

import rowley.bubblepop.models.FrameRateTracker;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.interfaces.ScreenController;

/**
 * Created by joe on 6/20/15.
 */
public class MainScreen implements ScreenController {
    private SurfaceHolder surfaceHolder;
    private MovingBubble[] bubbles;
    private int bubbleCreateIndex = 0;
    private FrameRateTracker frameRateTracker;
    private Paint paint;

    private int width, height;

    private Random random;

    public MainScreen(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        paint = new Paint();

        width = surfaceHolder.getSurfaceFrame().right;
        height = surfaceHolder.getSurfaceFrame().bottom;

        random = new Random();

        bubbles = new MovingBubble[75];
        bubbles[bubbleCreateIndex++] = createBubble();
        frameRateTracker = new FrameRateTracker();
    }

    private MovingBubble createBubble() {
        int bubbleX = random.nextInt(width);
        int bubbleY = random.nextInt(height);
        float bubbleYDir = random.nextFloat() + 0.5f;
        if(random.nextBoolean()) {
            bubbleYDir = -bubbleYDir;
        }
        float bubbleXDir = random.nextFloat() + 0.5f;
        if(random.nextBoolean()) {
            bubbleXDir = -bubbleXDir;
        }

        float speedDiff = random.nextFloat() + 0.35f;

        MovingBubble bubble = new MovingBubble(0, 0, width, height, bubbleX, bubbleY);
        bubble.setInitialDirection(bubbleXDir, bubbleYDir);
        bubble.setSpeedDifferential(speedDiff);

        return bubble;
    }

    public void update(long deltaTime) {
        for(MovingBubble bubble : bubbles) {
            if(bubble != null) {
                bubble.updateBubble(deltaTime);
            } else {
                break;
            }
        }
        frameRateTracker.update(deltaTime);
    }

    public void present() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null) {
            paint.setARGB(255, 0, 255, 255);
            canvas.drawRect(0, 0, width, height, paint);

            for(MovingBubble bubble : bubbles) {
                if(bubble != null) {
                    paint.setARGB(255, 255, 0, 0);
                    canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getBubbleRaduis(), paint);
                } else {
                    break;
                }
            }

            paint.setARGB(255, 0, 0, 0);
            paint.setTextSize(24);
            canvas.drawText(frameRateTracker.getFrameRate() + " fps", 50, 50, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
