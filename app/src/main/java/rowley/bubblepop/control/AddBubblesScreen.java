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
    private TouchIndicator[] touchIndicators;
    private int touchIndicatorIndex = 0;

    public AddBubblesScreen(SurfaceHolder surfaceHolder, GameController gameController) {
        super(surfaceHolder, gameController, 25);

        bubbles[bubbleCreateIndex++] = createBubble();

        touchIndicators = new TouchIndicator[25];
    }

    @Override
    protected void continueUpdate(long deltaTime, GameController controller) {

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

    @Override
    protected void continuePresent(Canvas canvas) {
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
    }
}
