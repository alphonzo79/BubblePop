package rowley.bubblepop.models;

/**
 * Created by joe on 6/23/15.
 */
public class GrowingBubble extends BubbleBase {
    private static final int INITIAL_RADIUS_RATIO = 28;
    private final int INITIAL_RADIUS;
    private static final int MAXIMUM_RADIUS_RATIO = 4;
    public final int MAXIMUM_RADIUS;
    private int growthRate = 150; //pixels per second
    private final int POP_TIME_IN_MILLIS = 500;

    private State state;
    private boolean wasPopped = false;
    private long popDuration;

    public GrowingBubble(int leftBound, int rightBound, int x, int y, int color) {
        super(x, y, (rightBound - leftBound) / INITIAL_RADIUS_RATIO, color);
        INITIAL_RADIUS = this.radius;
        MAXIMUM_RADIUS = getMaximumRadius(leftBound, rightBound);

        this.state = State.GROWING;
    }


    public void update(long deltaTimeInMillis) {
        if(state == State.GROWING) {
            int growthFactor = (int) (deltaTimeInMillis * growthRate) / 1000;
            radius += growthFactor;
            if(radius >= MAXIMUM_RADIUS) {
                state = State.POPPING;
                popDuration = 0;
            }
        } else if(state == State.POPPING) {
            popDuration += deltaTimeInMillis;
            if(popDuration >= POP_TIME_IN_MILLIS) {
                state = State.POPPED;
            }
        }
    }

    public State getState() {
        return state;
    }

    public boolean wasPopped() {
        return wasPopped;
    }

    public void adjustGrowthRate(float differential) {
        growthRate = (int)(growthRate * differential);
    }

    public void pop() {
        this.wasPopped = true;
        this.state = State.POPPING;
    }

    public float getSoundPlaybackRate(int lengthInSeconds) {
        int needToGrow = MAXIMUM_RADIUS - INITIAL_RADIUS;
        float secondsNeededToGrow = needToGrow / growthRate;
        return secondsNeededToGrow / lengthInSeconds;
    }

    public static int getMaximumRadius(int leftBound, int rightBound) {
        return (rightBound - leftBound) / MAXIMUM_RADIUS_RATIO;
    }

    public enum State {
        GROWING, POPPING, POPPED
    }
}
