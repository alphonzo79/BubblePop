package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.ScreenController;
import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.FrameRateTracker;
import rowley.bubblepop.models.GrowingBubble;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.util.ColorHelper;

/**
 * Created by joe on 6/23/15.
 */
public class SinglePopScreen implements ScreenController {
    private GrowingBubble bubble;
    private float bubbleGrowSoundSpeed;
    private final int GROW_SOUND_LENGTH = 1;
    private MovingBubble[] bubblePops;
    private final int BUBBLE_POP_COUNT = 6;
    private final float BUBBLE_POP_SPEED = 1.5f;
    private SoundPool soundPool;
    private int growSoundId;
    private int popSoundId;

    private FrameRateTracker frameRateTracker;
    private Paint paint;
    private final int BACKGROUND_COLOR;

    private int width, height;
    private int score;

    private String scoreString;
    private float scoreStringWidth;

    private Random random;

    private List<TouchEvent> touchEventList;

    public SinglePopScreen(SurfaceHolder surfaceHolder, GameController gameController) {
        paint = new Paint();
        BACKGROUND_COLOR = ColorHelper.getBackgroundColor();

        width = surfaceHolder.getSurfaceFrame().right;
        height = surfaceHolder.getSurfaceFrame().bottom;

        random = new Random();

        frameRateTracker = new FrameRateTracker();

        soundPool = gameController.getSoundPool();
        try {
            growSoundId = soundPool.load(gameController.getContext().getAssets().openFd("stretch.mp3"), 0);
            popSoundId = soundPool.load(gameController.getContext().getAssets().openFd("pop.mp3"), 0);
        } catch(IOException e) {
            e.printStackTrace();
        }

        bubble = getBubble();
        bubbleGrowSoundSpeed = bubble.getSoundPlaybackRate(GROW_SOUND_LENGTH);
        soundPool.play(growSoundId, 1.0f, 1.0f, 0, 0, bubbleGrowSoundSpeed);
        bubblePops = new MovingBubble[BUBBLE_POP_COUNT];

        score = 0;
    }

    private GrowingBubble getBubble() {
        int x = random.nextInt(width - (GrowingBubble.MAXIMUM_RADIUS * 2)) + GrowingBubble.MAXIMUM_RADIUS;
        int y = random.nextInt(height - (GrowingBubble.MAXIMUM_RADIUS * 2)) + GrowingBubble.MAXIMUM_RADIUS;
        return new GrowingBubble(x, y, ColorHelper.getRandomColor());
    }

    @Override
    public void update(long deltaTime, GameController gameController) {
        int bubbleX = bubble.getX();
        int bubbleY = bubble.getY();
        int bubbleRadius = bubble.getRadius();
        touchEventList = gameController.getTouchHandler().getTouchEvents();
        if(touchEventList != null && !touchEventList.isEmpty() && bubble.getState() == GrowingBubble.State.GROWING) {
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
                            soundPool.stop(growSoundId);
                            soundPool.play(popSoundId, 1.0f, 1.0f, 0, 0, 1f);
                            createBubblePops(bubbleX, bubbleY, bubble.getColor());
                        }
                    }
                }
            }
        }

        bubble.update(deltaTime);

        for(MovingBubble bubblePop : bubblePops) {
            if(bubblePop != null) {
                bubblePop.updateBubble(deltaTime);
            }
        }

        if(bubble.getState() == GrowingBubble.State.POPPED) {
            for(int i = 0; i < bubblePops.length; i++) {
                bubblePops[i] = null;
            }
            if(bubble.wasPopped()) {
                score += 10;
                bubble = getBubble();
                bubbleGrowSoundSpeed = bubble.getSoundPlaybackRate(GROW_SOUND_LENGTH);
                soundPool.play(growSoundId, 1.0f, 1.0f, 0, 0, bubbleGrowSoundSpeed);
            } else {
                gameController.setScreenController(new AddBubblesScreen(gameController.getSurfaceHolder(), gameController));
                //todo
            }
        }

        frameRateTracker.update(deltaTime);
    }

    private void createBubblePops(int x, int y, int color) {
        MovingBubble bubblePopOne = createBaseBubblePop(x, y, color);
        bubblePopOne.setInitialDirection(0, -1.0f);

        MovingBubble bubblePopTwo = createBaseBubblePop(x, y, color);
        bubblePopTwo.setInitialDirection(0.812f, -0.574f);

        MovingBubble bubblePopThree = createBaseBubblePop(x, y, color);
        bubblePopThree.setInitialDirection(0.812f, 0.574f);

        MovingBubble bubblePopFour = createBaseBubblePop(x, y, color);
        bubblePopFour.setInitialDirection(0f, 1.0f);

        MovingBubble bubblePopFive = createBaseBubblePop(x, y, color);
        bubblePopFive.setInitialDirection(-0.812f, 0.574f);

        MovingBubble bubblePopSix = createBaseBubblePop(x, y, color);
        bubblePopSix.setInitialDirection(-0.812f, -0.574f);

        bubblePops[0] = bubblePopOne;
        bubblePops[1] = bubblePopTwo;
        bubblePops[2] = bubblePopThree;
        bubblePops[3] = bubblePopFour;
        bubblePops[4] = bubblePopFive;
        bubblePops[5] = bubblePopSix;
    }

    private MovingBubble createBaseBubblePop(int x, int y, int color) {
        MovingBubble bubble = new MovingBubble(0, 0, width, height, x, y);
        bubble.setColor(color);
        bubble.setSpeedDifferential(BUBBLE_POP_SPEED);
        bubble.setIgnoreBounds(true);

        return bubble;
    }

    @Override
    public void present(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas != null) {
            paint.setColor(BACKGROUND_COLOR);
            canvas.drawRect(0, 0, width, height, paint);

            if(bubble.getState() == GrowingBubble.State.GROWING) {
                paint.setColor(bubble.getColor());
                canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
            } else if(bubble.getState() == GrowingBubble.State.POPPING) {
                paint.setColor(bubble.getColor());
                for(MovingBubble bubblePop : bubblePops) {
                    if(bubblePop != null) {
                        canvas.drawCircle(bubblePop.getX(), bubblePop.getY(), bubblePop.getBubbleRadius(), paint);
                    }
                }
                //todo
            }

            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setColor(ColorHelper.getTextColor());
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
