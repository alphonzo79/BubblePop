package rowley.bubblepop.models;

import android.graphics.Color;

/**
 * Created by joe on 6/18/15.
 */
public class MovingBubble {
    private int leftBound, topBound, rightBound, bottomBound;
    private int x, y;
    private float yDirection, xDirection;
    private final int BUBBLE_RADIUS = 25;
    private float movementRate;
    private int color;
    private boolean ignoreBounds = false;
    private boolean changedDirection = false;

    public MovingBubble(int leftBound, int topBound, int rightBound, int bottomBound, int initialX, int initialY) {
        this.leftBound = leftBound;
        this.topBound = topBound;
        this.rightBound = rightBound;
        this.bottomBound = bottomBound;
        this.x = initialX;
        this.y = initialY;

        yDirection = 1.0f;
        xDirection = -1.0f;

        //Traverse the width in 1.5 seconds
        movementRate = (rightBound / 1.5f) / 1000f;
        color = Color.argb(255, 255, 0, 0);
    }

    public void setSpeedDifferential(float speedDifferential) {
        movementRate = movementRate * speedDifferential;
    }

    public void setInitialDirection(float xDirection, float yDirection) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public void updateBubble(long deltaTimeInMilliseconds) {
        changedDirection = false;

        x = (int)(x + ((deltaTimeInMilliseconds * movementRate)) * xDirection);
        y = (int)(y + ((deltaTimeInMilliseconds * movementRate)) * yDirection);

        if(!ignoreBounds) {
            if (xDirection > 0) {
                if (x >= (rightBound - BUBBLE_RADIUS)) {
                    xDirection = xDirection * -1;
                    changedDirection = true;
                }
            } else {
                if (x <= (leftBound + BUBBLE_RADIUS)) {
                    xDirection = xDirection * -1;
                    changedDirection = true;
                }
            }
        }

        if(!ignoreBounds) {
            if (yDirection > 0) {
                if (y >= (bottomBound - BUBBLE_RADIUS)) {
                    yDirection = yDirection * -1;
                    changedDirection = true;
                }
            } else {
                if (y <= topBound + BUBBLE_RADIUS) {
                    yDirection = yDirection * -1;
                    changedDirection = true;
                }
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

    public int getBubbleRadius() {
        return BUBBLE_RADIUS;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isIgnoreBounds() {
        return ignoreBounds;
    }

    public void setIgnoreBounds(boolean ignoreBounds) {
        this.ignoreBounds = ignoreBounds;
    }

    public boolean isChangedDirection() {
        return changedDirection;
    }
}
