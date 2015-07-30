package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.SoundPool;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.ScreenController;
import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.FrameRateTracker;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.models.TouchIndicator;
import rowley.bubblepop.util.BubbleInteractionHelper;
import rowley.bubblepop.util.ColorHelper;

/**
 * Created by jrowley on 7/27/15.
 */
public abstract class MovingBubbleScreenBase implements ScreenController {
    protected SoundPool soundPool;
    protected int bounceSoundId;

    protected MovingBubble[] bubbles;
    protected int bubbleCreateIndex = 0;
    protected FrameRateTracker frameRateTracker;
    protected Paint paint;
    protected final int BACKGROUND_COLOR;

    protected int width, height;

    protected Random random;

    protected List<TouchEvent> touchEventList;

    public MovingBubbleScreenBase(SurfaceHolder surfaceHolder, GameController gameController, int bubbleArrayLength) {
        paint = new Paint();
        BACKGROUND_COLOR = ColorHelper.getBackgroundColor();

        width = surfaceHolder.getSurfaceFrame().right;
        height = surfaceHolder.getSurfaceFrame().bottom;

        random = new Random();

        bubbles = new MovingBubble[bubbleArrayLength];

        frameRateTracker = new FrameRateTracker();

        soundPool = gameController.getSoundPool();
        try {
            bounceSoundId = soundPool.load(gameController.getContext().getAssets().openFd("bounce.mp3"), 0);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected MovingBubble createBubble() {
        int bubbleX = random.nextInt(width);
        int bubbleY = random.nextInt(height);

        return createBubble(bubbleX, bubbleY);
    }

    protected MovingBubble createBubble(int bubbleX, int bubbleY) {
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
        bubble.setColor(ColorHelper.getRandomColor());

        return bubble;
    }

    public void update(long deltaTime, GameController controller) {
        for(MovingBubble bubble : bubbles) {
            if(bubble != null) {
                bubble.updateBubble(deltaTime);
            }
        }

        //Check for collissions between bubbles
        for(int i = 0; i < bubbles.length; i++) {
            for(int j = i + 1; j < bubbles.length; j++) {
                if (bubbles[i] != null && bubbles[j] != null) {
                    double ax = (double)bubbles[i].getX();
                    double ay = (double)bubbles[i].getY();
                    double bx = (double)bubbles[j].getX();
                    double by = (double)bubbles[j].getY();
                    double distx = (ax-bx)*(ax-bx);
                    double disty = (ay-by)*(ay-by);
                    double distance = Math.sqrt(distx + disty);
                    if(Math.floor(distance) <= bubbles[i].getRadius() * 2){
                        BubbleInteractionHelper.collideBubbles(bubbles[i], bubbles[j]);
                    }
                }
            }
        }

        continueUpdate(deltaTime, controller);

        frameRateTracker.update(deltaTime);
    }

    protected abstract void continueUpdate(long deltaTime, GameController controller);

    public void present(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null) {
            paint.setColor(BACKGROUND_COLOR);
            canvas.drawRect(0, 0, width, height, paint);

            for(MovingBubble bubble : bubbles) {
                if(bubble != null) {
                    paint.setColor(bubble.getColor());
                    canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
                    if(bubble.isChangedDirection()) {
                        soundPool.play(bounceSoundId, 1f, 1f, 0, 0, 1f);
                    }
                }
            }

            continuePresent(canvas);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ColorHelper.getTextColor());
            paint.setTextSize(24);
            canvas.drawText(frameRateTracker.getFrameRate() + " fps", 50, 50, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    protected abstract void continuePresent(Canvas canvas);
}
