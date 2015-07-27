package rowley.bubblepop.util;

import rowley.bubblepop.interfaces.TouchEvent;
import rowley.bubblepop.models.BubbleBase;
import rowley.bubblepop.models.MovingBubble;

/**
 * Created by jrowley on 7/27/15.
 */
public class BubbleInteractionHelper {
    public static boolean wasBubbleTouched(BubbleBase bubble, TouchEvent touchEvent) {
        int bubbleX = bubble.getX();
        int bubbleY = bubble.getY();
        int bubbleRadius = bubble.getRadius();

        if(touchEvent.getX() > bubbleX - bubbleRadius
                && touchEvent.getX() < bubbleX + bubbleRadius
                && touchEvent.getY() > bubbleY - bubbleRadius
                && touchEvent.getY() < bubbleY + bubbleRadius) {
            //Passed the minimum criteria. But this looks at a box and we want
            //to evaluate whether the touch was within a circle.
            int xDiff = bubbleX - touchEvent.getX();
            int yDiffThreshold = (int)Math.sqrt(Math.pow(bubbleRadius, 2) - Math.pow(xDiff, 2));
            if(touchEvent.getY() > bubbleY - yDiffThreshold
                    || touchEvent.getY() < bubbleY + yDiffThreshold) {
                return true;
            }
        }

        return false;
    }

    public static void collideBubbles(MovingBubble bubbleOne, MovingBubble bubbleTwo) {
        
    }
}
