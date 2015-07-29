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
            xDiff, yDiff, bubbleOneXEffect, bubbleOneYEffect, bubbleTwoXEffect, bubbleTwoYEffect;
    public static void collideBubbles(MovingBubble bubbleOne, MovingBubble bubbleTwo) {
        xDiff = bubbleOne.getX() - bubbleTwo.getX();
        yDiff = bubbleOne.getY() - bubbleTwo.getY();

        xRatio = xDiff / (bubbleOne.getBubbleRadius() * 2);
        yRatio = yDiff / (bubbleOne.getBubbleRadius() * 2);

        //We need to figure out whether the effect of each of the direction components will be magnified
        //or diminished -- If the bubble was moving in the direction of the impact point, then the
        //effect is multiplied. If it was moving away from the direction of the impact point then it
        //is diminished. To determine this we will look to see if the movement direction times the
        //impact direction is greater than or less than 0.
        //
        //The impact direction is the opposite of the diff. For example, if bubbleOne is on the left
        //and moving right, the xDiff (bubbleOne#x - bubbleTwo#x) will be negative, but the impact
        //point is to the right.
//        if(-xDiff * bubbleOne.getxDirection() > 0) {
            //The bubble was moving toward the impact point
            bubbleOneXEffect = bubbleOne.getxDirection() * -xRatio;
//        } else {
//            bubbleOneXEffect = bubbleOne.getxDirection() * -xRatio;
//        }

        //for bubble two we would expect the signs to match without inverting the diff
//        if(xDiff * bubbleTwo.getxDirection() > 0) {
            //The bubble was moving toward the impact point
            bubbleTwoXEffect = bubbleTwo.getxDirection() * xRatio;
//        } else {
//            bubbleTwoXEffect = bubbleTwo.getxDirection() * xRatio;
//        }

//        if(-yDiff * bubbleOne.getyDirection() > 0) {
            //The bubble was moving toward the impact point
            bubbleOneYEffect = bubbleOne.getyDirection() * -yRatio;
//        } else {
//            bubbleOneYEffect = bubbleOne.getyDirection() * -yRatio;
//        }

//        if(yDiff * bubbleTwo.getyDirection() > 0) {
            //The bubble was moving toward the impact point
            bubbleTwoYEffect = bubbleTwo.getyDirection() * yRatio;
//        } else {
//            bubbleTwoYEffect = bubbleTwo.getyDirection() * yRatio;
//        }

        bubbleTwoNewX = bubbleTwo.getxDirection() + bubbleOneXEffect;
        bubbleTwoNewY = bubbleTwo.getyDirection() + bubbleOneYEffect;
        bubbleOneNewX = bubbleOne.getxDirection() + bubbleTwoXEffect;
        bubbleOneNewY = bubbleOne.getyDirection() + bubbleTwoYEffect;

        Log.d("JAR", String.format("xDiff: %f yDiff: %f xRatio: %f yRatio: %f", xDiff, yDiff, xRatio, yRatio));
        Log.d("JAR", String.format("BubbleOneXEffect: %f BubbleOneYEffect: %f BubbleTwoXEffect: %f BubbleTwoYEffect: %f", bubbleOneXEffect, bubbleOneYEffect, bubbleTwoXEffect, bubbleTwoYEffect));
        Log.d("JAR", String.format("BubbleOne: OldXdir: %f OldYDir: %f NewXDir: %f NewYDir %f", bubbleOne.getxDirection(), bubbleOne.getyDirection(), bubbleOneNewX, bubbleOneNewY));
        Log.d("JAR", String.format("BubbleTwo: OldXdir: %f OldYDir: %f NewXDir: %f NewYDir %f\n\n\n", bubbleTwo.getxDirection(), bubbleTwo.getyDirection(), bubbleTwoNewX, bubbleTwoNewY));

        bubbleOne.setxDirection(bubbleOneNewX);
        bubbleOne.setyDirection(bubbleOneNewY);
        bubbleTwo.setxDirection(bubbleTwoNewX);
        bubbleTwo.setyDirection(bubbleTwoNewY);

        //todo figure out which ways we need to flip and at which angle and speed
    }
}
