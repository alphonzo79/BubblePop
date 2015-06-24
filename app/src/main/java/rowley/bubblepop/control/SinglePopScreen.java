package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.ScreenController;
import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.FrameRateTracker;
import rowley.bubblepop.models.GrowingBubble;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.models.TouchIndicator;

/**
 * Created by joe on 6/23/15.
 */
public class SinglePopScreen implements ScreenController {
    private GrowingBubble bubble;
    private FrameRateTracker frameRateTracker;
    private Paint paint;

    private int width, height;
    private int score;

    private String scoreString;
    private float scoreStringWidth;

    private Random random;

    private List<TouchEvent> touchEventList;

    public SinglePopScreen(SurfaceHolder surfaceHolder) {
        paint = new Paint();

        width = surfaceHolder.getSurfaceFrame().right;
        height = surfaceHolder.getSurfaceFrame().bottom;

        random = new Random();

        frameRateTracker = new FrameRateTracker();

        bubble = getBubble();

        score = 0;
    }

    private GrowingBubble getBubble() {
        int x = random.nextInt(width - (GrowingBubble.MAXIMUM_RADIUS * 2)) + GrowingBubble.MAXIMUM_RADIUS;
        int y = random.nextInt(height - (GrowingBubble.MAXIMUM_RADIUS * 2)) + GrowingBubble.MAXIMUM_RADIUS;
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return new GrowingBubble(x, y, color);
    }

    @Override
    public void update(long deltaTime, GameController gameController) {
        int bubbleX = bubble.getX();
        int bubbleY = bubble.getY();
        int bubbleRadius = bubble.getRadius();
        touchEventList = gameController.getTouchHandler().getTouchEvents();
        if(touchEventList != null && !touchEventList.isEmpty()) {
            for(TouchEvent event : touchEventList) {
                if(event.getType() == TouchEvent.TOUCH_DOWN) {
                    if(event.getX() > bubbleX - bubbleRadius
                            && event.getX() < bubbleX + bubbleRadius
                            && event.getY() > bubbleY - bubbleRadius
                            && event.getY() < bubbleY + bubbleRadius) {
                        //Passed the minimum criteria. But this looks at a box and we want
                        //to evaluate whether the touch was within a circle.
                        int xDiff = bubbleX - event.getX();
                        int yDiffThreshold = (int)Math.sqrt(Math.pow(bubbleRadius, 2) - Math.pow(xDiff, 2));
                        if(event.getY() > bubbleY - yDiffThreshold
                                || event.getY() < bubbleY + yDiffThreshold) {
                            bubble.pop();
                        }
                    }
                }
            }
        }

        bubble.update(deltaTime);

        if(bubble.getState() == GrowingBubble.State.POPPED) {
            if(bubble.wasPopped()) {
                score += 10;
                bubble = getBubble();
            } else {
                gameController.setScreenController(new MainScreen(gameController.getSurfaceHolder()));
                //todo
            }
        }

        frameRateTracker.update(deltaTime);
    }

    @Override
    public void present(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null) {
            paint.setARGB(255, 0, 200, 200);
            canvas.drawRect(0, 0, width, height, paint);

            if(bubble.getState() == GrowingBubble.State.GROWING) {
                paint.setColor(bubble.getColor());
                canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
            } else if(bubble.getState() == GrowingBubble.State.POPPING) {
                //todo
            }

            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setARGB(255, 0, 0, 0);
            paint.setTextSize(62);
            scoreString = ("Score: " + score);
            scoreStringWidth = paint.measureText(scoreString);
            canvas.drawText("Score: " + score, width - scoreStringWidth - 50, 75, paint);

            paint.setTextSize(24);
            paint.setTypeface(Typeface.DEFAULT);
            canvas.drawText(frameRateTracker.getFrameRate() + " fps", 50, 50, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
