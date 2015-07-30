package rowley.bubblepop.models;

/**
 * Created by jrowley on 7/27/15.
 */
public abstract class BubbleBase {
    protected int color;
    protected int x;
    protected int y;
    protected int radius;

    public BubbleBase(int x, int y, int initialRadius, int color) {
        this.x = x;
        this.y = y;
        this.radius = initialRadius;
        this.color = color;
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

    public void setRadiusDifferential(float differential) {
        radius = (int)(radius * differential);
    }
}
