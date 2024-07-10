package vsdk.source.utils;

import com.raylib.Raylib;

import static vsdk.r_utilities.PathResolver.resolvePath;

/**
 * Simple one-method utility for setting icon for window.
 */
public class WindowIconUtil {
    /**
     * Set window icon.
     *
     * @param icon Path to icon.
     * @param resolve Resolve path?
     */
    public static void setWinIcon(String icon, boolean resolve) {
        if(resolve) icon = resolvePath(icon);

        Raylib.Image iconImg = Raylib.LoadImage(icon);

        Raylib.SetWindowIcon(iconImg);

        Raylib.UnloadImage(iconImg);
    }
}
