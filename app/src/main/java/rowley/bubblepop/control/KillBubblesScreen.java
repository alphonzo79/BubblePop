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
    private static final int BUBBLE_START_COUNT = 25;
    private MovingBubble[] bubblePops;
    private int bubblePopCreationIndex = 0;
    private final int BUBBLE_POP_COUNT = 6 * 5;
    private final float BUBBLE_POP_SPEED = 1.5f;
    private int popSoundId;

    public KillBubblesScreen(SurfaceHolder surfaceHolder, GameController gameController) {
        super(surfaceHolder, gameController, BUBBLE_START_COUNT);

        for(; bubbleCreateIndex < BUBBLE_START_COUNT; bubbleCreateIndex++) {
            bubbles[bubbleCreateIndex++] = createBubble();
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
                if(event.getType() == TouchEvent.TOUCH_UP) {
                    for(BubbleBase bubble : bubbles) {
                        if(/*!bubble.isPopped*/BubbleInteractionHelper.wasBubbleTouched(bubble, event)) {
                            //todo pop the bubble
                            createBubblePops(bubble.getX(), bubble.getY(), bubble.getColor());
                        }
                    }
//                    bubbles[bubbleCreateIndex++] = createBubble(event.getX(), event.getY());
//                    if(bubbleCreateIndex >= bubbles.length) {
//                        bubbleCreateIndex = 0;
//                        controller.setScreenController(new SinglePopScreen(controller.getSurfaceHolder(), controller));
//                    }
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
    protected void continuePresent(Canvas canvas) {
        //todo handle the bubble pops
    }
}
