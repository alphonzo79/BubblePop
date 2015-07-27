package rowley.bubblepop.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Random;

import rowley.bubblepop.interfaces.GameController;
import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.FrameRateTracker;
import rowley.bubblepop.models.MovingBubble;
import rowley.bubblepop.models.TouchIndicator;
import rowley.bubblepop.util.ColorHelper;

/**
 * Created by joe on 6/20/15.
 */
public class AddBubblesScreen extends MovingBubbleScreenBase {

    public AddBubblesScreen(SurfaceHolder surfaceHolder, GameController gameController) {
        super(surfaceHolder, gameController, 25);
    }

    @Override
    protected void handleTouchEvents(GameController controller) {
        touchEventList = controller.getTouchHandler().getTouchEvents();
        if(touchEventList != null && !touchEventList.isEmpty()) {
            for(TouchEvent event : touchEventList) {
                if(event.getType() == TouchEvent.TOUCH_UP) {
                    bubbles[bubbleCreateIndex++] = createBubble(event.getX(), event.getY());
                    if(bubbleCreateIndex >= bubbles.length) {
                        bubbleCreateIndex = 0;
                        controller.setScreenController(new SinglePopScreen(controller.getSurfaceHolder(), controller));
                    }
                    touchIndicators[touchIndicatorIndex++] = new TouchIndicator(event.getX(), event.getY());
                    if(touchIndicatorIndex >= touchIndicators.length) {
                        touchIndicatorIndex = 0;
                    }
                }
            }
        }
    }
}
