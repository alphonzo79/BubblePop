package rowley.bubblepop.components;

import android.view.SurfaceView;

/**
 * Created by joe on 6/18/15.
 */
public class MovingBubble {
    private int leftBound, topBound, rightBound, bottomBound;
    private int x, y;
    private int yDirection, xDirection;
    private int bubbleRadius = 25;

    public MovingBubble(int leftBound, int topBound, int rightBound, int bottomBound, int initialX, int initialY) {
        this.leftBound = leftBound;
        this.topBound = topBound;
        this.rightBound = rightBound;
        this.bottomBound = bottomBound;
        this.x = initialX;
        this.y = initialY;

        yDirection = 1;
        xDirection = -1;
    }

    //move 500 pixels per second
    public void updateBubble(long deltaTimeInMilliseconds) {
        x = (int)((deltaTimeInMilliseconds / 1000) * 500) * xDirection;
        y = (int)((deltaTimeInMilliseconds / 1000) * 500) * yDirection;

        if(xDirection > 0) {
            if(x >= (rightBound - bubbleRadius)) {
                xDirection = xDirection * -1;
            }
        } else {
            if(x <= (leftBound + bubbleRadius)) {
                xDirection = xDirection * -1;
            }
        }

        if(yDirection > 0) {
            if(y >= (bottomBound - bubbleRadius)) {
                yDirection = yDirection * -1;
            }
        } else {
            if(y <= topBound + bubbleRadius) {
                yDirection = yDirection * -1;
            }
        }
    }

    public void draw(SurfaceView surfaceView) {
        //todo
    }
}
