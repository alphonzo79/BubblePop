package rowley.bubblepop.models;

/**
 * Created by joe on 6/18/15.
 */
public class MovingBubble {
    private int leftBound, topBound, rightBound, bottomBound;
    private int x, y;
    private float yDirection, xDirection;
    private final int BUBBLE_RADIUS = 25;
    private float movementRate;

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
    }

    public void setSpeedDifferential(float speedDifferential) {
        movementRate = movementRate * speedDifferential;
    }

    public void setInitialDirection(float xDirection, float yDirection) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public void updateBubble(long deltaTimeInMilliseconds) {
        x = (int)(x + ((deltaTimeInMilliseconds * movementRate)) * xDirection);
        y = (int)(y + ((deltaTimeInMilliseconds * movementRate)) * yDirection);

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
