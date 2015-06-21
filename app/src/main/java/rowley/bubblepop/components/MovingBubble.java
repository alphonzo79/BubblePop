package rowley.bubblepop.components;

import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by joe on 6/18/15.
 */
public class MovingBubble {
    private int leftBound, topBound, rightBound, bottomBound;
    private int x, y;
    private float yDirection, xDirection;
    private final int BUBBLE_RADIUS = 25;

    public MovingBubble(int leftBound, int topBound, int rightBound, int bottomBound, int initialX, int initialY) {
        this.leftBound = leftBound;
        this.topBound = topBound;
        this.rightBound = rightBound;
        this.bottomBound = bottomBound;
        this.x = initialX;
        this.y = initialY;

        yDirection = 0.9f;
        xDirection = -1.1f;
    }

    //move 500 pixels per second
    public void updateBubble(long deltaTimeInMilliseconds) {
        x = (int)(x + (((deltaTimeInMilliseconds / 10) * 5) * xDirection));
        y = (int)(y + (((deltaTimeInMilliseconds / 10) * 5) * yDirection));

        if(xDirection > 0) {
            if(x >= (rightBound - BUBBLE_RADIUS)) {
                xDirection = xDirection * -1;
            }
        } else {
            if(x <= (leftBound + BUBBLE_RADIUS)) {
                xDirection = xDirection * -1;
            }
        }

        if(yDirection > 0) {
            if(y >= (bottomBound - BUBBLE_RADIUS)) {
                yDirection = yDirection * -1;
            }
        } else {
            if(y <= topBound + BUBBLE_RADIUS) {
                yDirection = yDirection * -1;
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBubbleRaduis() {
        return BUBBLE_RADIUS;
    }
}
