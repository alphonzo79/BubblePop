package rowley.bubblepop.models;

/**
 * Created by joe on 6/23/15.
 */
public class GrowingBubble extends BubbleBase {
    private static final int INITIAL_RADIUS = 25;
    public static final int MAXIMUM_RADIUS = 250;
    private int growthRate = 150; //pixels per second
    private final int POP_TIME_IN_MILLIS = 500;

    private State state;
    private boolean wasPopped = false;
    private long popDuration;

    public GrowingBubble(int x, int y, int color) {
        super(x, y, INITIAL_RADIUS, color);
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

    public enum State {
        GROWING, POPPING, POPPED
    }
}
