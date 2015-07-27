package rowley.bubblepop.control;

import android.view.SurfaceHolder;

import rowley.bubblepop.interfaces.GameController;

/**
 * Created by jrowley on 7/27/15.
 */
public class KillBubblesScreen extends MovingBubbleScreenBase {
    private static final int BUBBLE_START_COUNT = 25;

    public KillBubblesScreen(SurfaceHolder surfaceHolder, GameController gameController) {
        super(surfaceHolder, gameController, BUBBLE_START_COUNT);

        for(; bubbleCreateIndex < BUBBLE_START_COUNT; bubbleCreateIndex++) {
            bubbles[bubbleCreateIndex++] = createBubble();
        }
    }

    @Override
    protected void handleTouchEvents(long deltaTime, GameController controller) {
        //Do we need to kill any bubbles?
    }
}
