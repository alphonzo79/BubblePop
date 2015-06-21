package rowley.bubblepop.models;

/**
 * Created by joe on 6/20/15.
 */
public class FrameRateTracker {
    private long[] deltaTimes;
    private int index;

    private int frameRate;
    private long deltaAccumulator;
    private int nonZeros;

    public FrameRateTracker() {
        deltaTimes = new long[10];
        index = 0;
    }

    public void update(long deltaTimeInMilliseconds) {
        deltaTimes[index++] = deltaTimeInMilliseconds;
        if(index >= deltaTimes.length) {
            index = 0;
        }
    }

    public int getFrameRate() {
        frameRate = 0;
        deltaAccumulator = 0;
        nonZeros = 0;

        for(int i = 0; i < deltaTimes.length; i++) {
            if(deltaTimes[i] > 0) {
                deltaAccumulator += deltaTimes[i];
                nonZeros++;
            }
        }

        if(deltaAccumulator > 0 && nonZeros > 0) {
            frameRate = (int) deltaAccumulator / nonZeros;
        }

        return frameRate;
    }
}
