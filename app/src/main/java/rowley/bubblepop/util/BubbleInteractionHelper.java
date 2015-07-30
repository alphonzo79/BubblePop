package rowley.bubblepop.util;

import android.util.Log;

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

    private static float bubbleOneNewX, bubbleOneNewY, bubbleTwoNewX, bubbleTwoNewY, xRatio, yRatio,
            xDiff, yDiff, diffDistance;
    public static void collideBubbles(MovingBubble bubbleOne, MovingBubble bubbleTwo) {
        xDiff = bubbleOne.getX() - bubbleTwo.getX();
        yDiff = bubbleOne.getY() - bubbleTwo.getY();
        diffDistance = (xDiff * xDiff) + (yDiff * yDiff);

        xRatio = (xDiff * xDiff) / diffDistance;
        yRatio = (yDiff * yDiff) / diffDistance;

        double bubbleOneTotalToMaintain = (bubbleOne.getxDirection() * bubbleOne.getxDirection()) + (bubbleOne.getyDirection() * bubbleOne.getyDirection());
        bubbleOneNewX = (float)Math.sqrt(bubbleOneTotalToMaintain * xRatio);
        if(xRatio >= .5F && xDiff * bubbleOneNewX < 0) {
            bubbleOneNewX = bubbleOneNewX * -1;
        }
        bubbleOneNewY = (float)Math.sqrt(bubbleOneTotalToMaintain * yRatio);
        if(yRatio >= .5F && yDiff * bubbleOneNewY < 0) {
            bubbleOneNewY = bubbleOneNewY * -1;
        }

        double bubbleTwoTotalToMaintain = (bubbleTwo.getxDirection() * bubbleTwo.getxDirection()) + (bubbleTwo.getyDirection() * bubbleTwo.getyDirection());
        bubbleTwoNewX = (float)Math.sqrt(bubbleTwoTotalToMaintain * xRatio);
        if(xRatio >= .5F && xDiff * bubbleTwoNewX > 0) {
            bubbleTwoNewX = bubbleTwoNewX * -1;
        }
        bubbleTwoNewY = (float)Math.sqrt(bubbleTwoTotalToMaintain * yRatio);
        if(yRatio >= .5F && yDiff * bubbleTwoNewY > 0) {
            bubbleTwoNewY = bubbleTwoNewY * -1;
        }

//        Log.d("JAR", String.format("xDiff: %f yDiff: %f xRatio: %f yRatio: %f", xDiff, yDiff, xRatio, yRatio));
//        Log.d("JAR", String.format("BubbleOne: OldXdir: %f OldYDir: %f NewXDir: %f NewYDir %f", bubbleOne.getxDirection(), bubbleOne.getyDirection(), bubbleOneNewX, bubbleOneNewY));
//        Log.d("JAR", String.format("BubbleTwo: OldXdir: %f OldYDir: %f NewXDir: %f NewYDir %f\n\n\n", bubbleTwo.getxDirection(), bubbleTwo.getyDirection(), bubbleTwoNewX, bubbleTwoNewY));

        bubbleOne.setxDirection(bubbleOneNewX);
        bubbleOne.setyDirection(bubbleOneNewY);
        bubbleTwo.setxDirection(bubbleTwoNewX);
        bubbleTwo.setyDirection(bubbleTwoNewY);
    }
}
