package rowley.bubblepop.interfaces;

import java.util.List;

/**
 * Created by joe on 6/20/15.
 */
public interface TouchHandler {
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<TouchEvent> getTouchEvents();
}
