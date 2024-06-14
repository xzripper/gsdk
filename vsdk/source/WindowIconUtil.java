package vsdk.source;

import com.raylib.Raylib;

/**
 * Simple one-method utility for setting icon for window.
 */
public class WindowIconUtil {
    /**
     * Set window icon.
     *
     * @param icon Path to icon.
     */
    public static void setWinIcon(String icon) {
        Raylib.Image iconImg = Raylib.LoadImage(icon);

        Raylib.SetWindowIcon(iconImg);

        Raylib.UnloadImage(iconImg);
    }
}
