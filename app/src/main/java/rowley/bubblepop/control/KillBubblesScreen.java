package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.media.SoundPool;
import android.view.SurfaceHolder;

import java.io.IOException;

import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.BubbleBase;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.models.TouchIndicator;
import rowley.bubblepop.util.BubbleInteractionHelper;

/**
 * Created by jrowley on 7/27/15.
 */
public class KillBubblesScreen extends MovingBubbleScreenBase {
    private static final int BUBBLE_START_COUNT = 10;
    private int remainingBubbleCount = BUBBLE_START_COUNT;
    private final float MAIN_BUBBLE_SPEED_DIFF = 0.60F;
    private final float MAIN_BUBBLE_SIZE_DIFF = 1.55F;
    private MovingBubble[] bubblePops;
    private int bubblePopCreationIndex = 0;
    private final int BUBBLE_POP_COUNT = 6 * 5;
    private final float BUBBLE_POP_SPEED = 1.5f;
    private final float BUBBLE_POP_SIZE_DIFF = .75F;
    private int popSoundId;

    public KillBubblesScreen(SurfaceHolder surfaceHolder, GameController gameController) {
        super(surfaceHolder, gameController, BUBBLE_START_COUNT);

        for(; bubbleCreateIndex < BUBBLE_START_COUNT; bubbleCreateIndex++) {
            MovingBubble bubble = createBubble();
            bubble.setSpeedDifferential(MAIN_BUBBLE_SPEED_DIFF);
            bubble.setRadiusDifferential(MAIN_BUBBLE_SIZE_DIFF);
            bubbles[bubbleCreateIndex] = bubble;
        }

        try {
            popSoundId = soundPool.load(gameController.getContext().getAssets().openFd("pop.mp3"), 0);
        } catch(IOException e) {
            e.printStackTrace();
        }

        bubblePops = new MovingBubble[BUBBLE_POP_COUNT];
    }

    @Override
    protected void continueUpdate(long deltaTime, GameController controller) {
        //Do we need to kill any bubbles?
        touchEventList = controller.getTouchHandler().getTouchEvents();
        if(touchEventList != null && !touchEventList.isEmpty()) {
            for(TouchEvent event : touchEventList) {
                if(event.getType() == TouchEvent.TOUCH_DOWN || event.getType() == TouchEvent.TOUCH_UP || event.getType() == TouchEvent.TOUCH_DRAGGED) {
                    for(int i = 0; i < bubbles.length; i++) {
                        if(bubbles[i] != null && BubbleInteractionHelper.wasBubbleTouched(bubbles[i], event)) {
                            createBubblePops(bubbles[i].getX(), bubbles[i].getY(), bubbles[i].getColor());
                            bubbles[i] = null;
                            remainingBubbleCount--;
                            soundPool.play(popSoundId, 1.0f, 1.0f, 0, 0, 1f);

                            if(remainingBubbleCount == 0) {
                                controller.setScreenController(new SinglePopScreen(controller.getSurfaceHolder(), controller));
                            }
                        }
                    }
                }
            }
        }

        for(MovingBubble bubblePop : bubblePops) {
            if(bubblePop != null) {
                bubblePop.updateBubble(deltaTime);
            }
        }
    }

    private void createBubblePops(int x, int y, int color) {
        MovingBubble bubblePopOne = createBubblePop(x, y, color);
        bubblePopOne.setInitialDirection(0, -1.0f);

        MovingBubble bubblePopTwo = createBubblePop(x, y, color);
        bubblePopTwo.setInitialDirection(0.812f, -0.574f);

        MovingBubble bubblePopThree = createBubblePop(x, y, color);
        bubblePopThree.setInitialDirection(0.812f, 0.574f);

        MovingBubble bubblePopFour = createBubblePop(x, y, color);
        bubblePopFour.setInitialDirection(0f, 1.0f);

        MovingBubble bubblePopFive = createBubblePop(x, y, color);
        bubblePopFive.setInitialDirection(-0.812f, 0.574f);

        MovingBubble bubblePopSix = createBubblePop(x, y, color);
        bubblePopSix.setInitialDirection(-0.812f, -0.574f);

        bubblePops[bubblePopCreationIndex++] = bubblePopOne;
        bubblePops[bubblePopCreationIndex++] = bubblePopTwo;
        bubblePops[bubblePopCreationIndex++] = bubblePopThree;
        bubblePops[bubblePopCreationIndex++] = bubblePopFour;
        bubblePops[bubblePopCreationIndex++] = bubblePopFive;
        bubblePops[bubblePopCreationIndex++] = bubblePopSix;

        if(bubblePopCreationIndex >= BUBBLE_POP_COUNT) {
            bubblePopCreationIndex = 0;
        }
    }

    private MovingBubble createBubblePop(int x, int y, int color) {
        MovingBubble bubble = new MovingBubble(0, 0, width, height, x, y);
        bubble.setColor(color);
        bubble.setSpeedDifferential(BUBBLE_POP_SPEED);
        bubble.setIgnoreBounds(true);
        bubble.setRadiusDifferential(BUBBLE_POP_SIZE_DIFF);

        return bubble;
    }

    @Override
    protected void continuePresent(Canvas canvas) {
        for(MovingBubble bubble : bubblePops) {
            if(bubble != null) {
                paint.setColor(bubble.getColor());
                canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
            }
        }
    }
}
