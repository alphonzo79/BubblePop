package rowley.bubblepop.models;

import android.graphics.Color;

import java.util.List;
import java.util.Random;

import rowley.bubblepop.interfaces.TouchEvent;

/**
 * Created by joe on 6/21/15.
 */
public class TouchIndicator {
    private int x, y;
    private IndicatorCircle[] indicatorCircles;
    private int index;

    private final int RADIUS_GROWTH_RATE = 150; //pixels per second
    private final int RADIUS_MAX = 120;
    private final int RADIUS_INITIAL = 20;
    private final int GAP = 40;
    private final int MAX_INDICATORS = 1;

    private Random random;

    public TouchIndicator(int x, int y) {
        this.x = x;
        this.y = y;
        this.indicatorCircles = new IndicatorCircle[MAX_INDICATORS];
        index = 0;
        random = new Random(System.currentTimeMillis());

        indicatorCircles[index++] = createNewCircle();
    }

    private IndicatorCircle createNewCircle() {
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return new IndicatorCircle(RADIUS_INITIAL, color);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IndicatorCircle[] getIndicatorCircles() {
        return indicatorCircles;
    }

    public void update(long deltaTimeInMilliseconds) {
        int growthFactor = (int)(deltaTimeInMilliseconds * RADIUS_GROWTH_RATE) / 1000;
        for(int i = 0; i < indicatorCircles.length; i++) {
            if(indicatorCircles[i] != null) {
                if(indicatorCircles[i].getRadius() >= RADIUS_MAX) {
                    indicatorCircles[i] = null;
                } else {
                    indicatorCircles[i].setRadius(indicatorCircles[i].getRadius() + growthFactor);
                }
            }
        }

        if(index < MAX_INDICATORS && indicatorCircles[index - 1].getRadius() > (RADIUS_INITIAL + GAP)) {
            indicatorCircles[index++] = createNewCircle();
        }
    }

    public class IndicatorCircle {
        private int radius, color;

        private IndicatorCircle(int radius, int color) {
            this.radius = radius;
            this.color = color;
        }

        public int getRadius() {
            return radius;
        }

        private void setRadius(int radius) {
            this.radius = radius;
        }

        public int getColor() {
            return color;
        }

        private void setColor(int color) {
            this.color = color;
        }
    }
}
