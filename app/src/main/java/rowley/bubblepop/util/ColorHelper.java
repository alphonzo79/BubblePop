package rowley.bubblepop.util;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by joe on 6/24/15.
 */
public class ColorHelper {
    private static final int BACKGROUND_COLOR = Color.BLACK;
    private static final int TEXT_COLOR = Color.WHITE;
    private static final int[] COLORS = new int[]{Color.BLUE, Color.CYAN, Color.GREEN,
            Color.MAGENTA, Color.RED, Color.WHITE, Color.YELLOW};
    private static Random random = new Random(System.currentTimeMillis());

    public static int getRandomColor() {
        return COLORS[random.nextInt(COLORS.length)];
    }

    public static int getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    public static int getTextColor() {
        return TEXT_COLOR;
    }
}
