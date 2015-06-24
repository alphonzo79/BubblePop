package rowley.bubblepop.models;

/**
 * Created by joe on 6/23/15.
 */
public class GrowingBubble {
    private final int INITIAL_RADIUS = 25;
    public static final int MAXIMUM_RADIUS = 250;
    private final int GROWTH_RATE = 100; //pixels per second
    private final int POP_TIME_IN_MILLIS = 200;

    private int color;
    private int x;
    private int y;
    private int radius;
    private State state;
    private boolean wasPopped = false;
    private long popDuration;

    public GrowingBubble(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.radius = INITIAL_RADIUS;
        this.state = State.GROWING;
        this.color = color;
    }


    public void update(long deltaTimeInMillis) {
        if(state == State.GROWING) {
            int growthFactor = (int) (deltaTimeInMillis * GROWTH_RATE) / 1000;
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

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public State getState() {
        return state;
    }

    public boolean wasPopped() {
        return wasPopped;
    }

    public void pop() {
        this.wasPopped = true;
        this.state = State.POPPING;
    }

    public enum State {
        GROWING, POPPING, POPPED
    }
}