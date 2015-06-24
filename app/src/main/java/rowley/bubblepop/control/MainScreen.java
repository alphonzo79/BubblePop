package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.List;
import java.util.Random;

import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.FrameRateTracker;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.interfaces.ScreenController;
import rowley.bubblepop.models.TouchIndicator;

/**
 * Created by joe on 6/20/15.
 */
public class MainScreen implements ScreenController {
    private MovingBubble[] bubbles;
    private TouchIndicator[] touchIndicators;
    private int bubbleCreateIndex = 0;
    private int touchIndicatorIndex = 0;
    private FrameRateTracker frameRateTracker;
    private Paint paint;

    private int width, height;

    private Random random;

    private List<TouchEvent> touchEventList;

    public MainScreen(SurfaceHolder surfaceHolder) {
        paint = new Paint();

        width = surfaceHolder.getSurfaceFrame().right;
        height = surfaceHolder.getSurfaceFrame().bottom;

        random = new Random();

        bubbles = new MovingBubble[75];
        bubbles[bubbleCreateIndex++] = createBubble();

        touchIndicators = new TouchIndicator[25];

        frameRateTracker = new FrameRateTracker();
    }

    private MovingBubble createBubble() {
        int bubbleX = random.nextInt(width);
        int bubbleY = random.nextInt(height);

        return createBubble(bubbleX, bubbleY);
    }

    private MovingBubble createBubble(int bubbleX, int bubbleY) {
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
        bubble.setColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));

        return bubble;
    }

    public void update(long deltaTime, GameController controller) {
        for(MovingBubble bubble : bubbles) {
            if(bubble != null) {
                bubble.updateBubble(deltaTime);
            } else {
                break;
            }
        }

        for(TouchIndicator indicator : touchIndicators) {
            if(indicator != null) {
                indicator.update(deltaTime);
            }
        }

        touchEventList = controller.getTouchHandler().getTouchEvents();
        if(touchEventList != null && !touchEventList.isEmpty()) {
            for(TouchEvent event : touchEventList) {
                if(event.getType() == TouchEvent.TOUCH_UP) {
                    bubbles[bubbleCreateIndex++] = createBubble(event.getX(), event.getY());
                    if(bubbleCreateIndex >= bubbles.length) {
                        bubbleCreateIndex = 0;
                    }
                    touchIndicators[touchIndicatorIndex++] = new TouchIndicator(event.getX(), event.getY());
                    if(touchIndicatorIndex >= touchIndicators.length) {
                        touchIndicatorIndex = 0;
                    }
                }
            }
        }

        frameRateTracker.update(deltaTime);
    }

    public void present(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null) {
            paint.setARGB(255, 0, 255, 255);
            canvas.drawRect(0, 0, width, height, paint);

            for(MovingBubble bubble : bubbles) {
                if(bubble != null) {
                    paint.setColor(bubble.getColor());
                    canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getBubbleRadius(), paint);
                } else {
                    break;
                }
            }

            paint.setStyle(Paint.Style.STROKE);
            for(TouchIndicator indicator : touchIndicators) {
                if(indicator != null) {
                    TouchIndicator.IndicatorCircle[] circles = indicator.getIndicatorCircles();
                    for (TouchIndicator.IndicatorCircle circle : circles) {
                        if (circle != null) {
                            paint.setColor(circle.getColor());
                            canvas.drawCircle(indicator.getX(), indicator.getY(), circle.getRadius(), paint);
                        }
                    }
                }
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(255, 0, 0, 0);
            paint.setTextSize(24);
            canvas.drawText(frameRateTracker.getFrameRate() + " fps", 50, 50, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
