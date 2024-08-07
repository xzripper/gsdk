package gsdk.source.utils;

import com.raylib.Raylib;

import static gsdk.r_utilities.PathResolver.resolvePath;

/**
 * Class for window utilities.
 */
public class WindowUtilities {
    public static final float STANDARD_DPI_THRESHOLD = 120.0f;
    public static final float MODERN_DPI_THRESHOLD = 144.0f;
    public static final float HIGH_RESOLUTION_DPI_THRESHOLD = 160.0f;

    public static final int FULLSCREEN_MODE = 1;
    public static final int BORDERLESS_MODE = 2;
    public static final int WINDOWED_MODE = 3;

    /**
     * Window initialization utility.
     * 
     * @param title Window title.
     * @param width Window width.
     * @param height Window height.
     * @param targetFPS Window target FPS (leave 0 to disable).
     * @param mode Window mode (leave 0 to toggle default windowed mode and apply flags).
     * @param flags Window flags (leave 0)
     * @param initAudio Initialize audio device?
     */
    public static WindowFlags initWindow(String title, int width, int height, int targetFPS, int mode, int flags, boolean initAudio) {
        WindowFlags wFlags = new WindowFlags(flags);

        Raylib.InitWindow(width, height, title);

        if(mode == FULLSCREEN_MODE) setWinFullscreen(wFlags);
        else if(mode == BORDERLESS_MODE) setWinBorderless(wFlags);
        else if(mode == WINDOWED_MODE) setWinWindowed(wFlags, width, height);
        else if(mode == 0) wFlags.setWindowFlags();

        if(targetFPS != 0) Raylib.SetTargetFPS(targetFPS);

        if(initAudio) Raylib.InitAudioDevice();

        return wFlags;
    }

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

    /**
     * Set window fullscreen mode.
     * 
     * @param flags Window flags.
     */
    public static void setWinFullscreen(WindowFlags flags) {
        Raylib.SetWindowSize(
            Raylib.GetMonitorWidth(Raylib.GetCurrentMonitor()),
            Raylib.GetMonitorHeight(Raylib.GetCurrentMonitor())
        );

        flags.append(Raylib.FLAG_FULLSCREEN_MODE);

        flags.setWindowFlags();
    }

    /**
     * Set window borderless mode.
     * 
     * @param flags Window flags.
     */
    public static void setWinBorderless(WindowFlags flags) {
        flags.append(Raylib.FLAG_WINDOW_UNDECORATED);

        flags.setWindowFlags();

        Raylib.SetWindowSize(
            Raylib.GetMonitorWidth(Raylib.GetCurrentMonitor()),
            Raylib.GetMonitorHeight(Raylib.GetCurrentMonitor())
        );

        Raylib.SetWindowPosition(0, 0);
    }

    /**
     * Set windowed mode.
     * 
     * @param flags Window flags.
     * @param width Window width.
     * @param height Window height.
     */
    public static void setWinWindowed(WindowFlags flags, int width, int height) {
        Raylib.SetWindowSize(width, height);

        flags.remove(Raylib.FLAG_FULLSCREEN_MODE, Raylib.FLAG_WINDOW_UNDECORATED);

        flags.setWindowFlags();

        Raylib.SetWindowPosition(
            (Raylib.GetMonitorWidth(Raylib.GetCurrentMonitor()) - width) / 2,
            (Raylib.GetMonitorHeight(Raylib.GetCurrentMonitor()) - height) / 2
        );
    }

    /**
     * Get window mode.

     * @param flags Window flags.
     */
    public static int getWinMode(WindowFlags flags) {
        return flags.has(Raylib.FLAG_FULLSCREEN_MODE) ? FULLSCREEN_MODE : (flags.has(Raylib.FLAG_WINDOW_UNDECORATED) ? BORDERLESS_MODE : WINDOWED_MODE);
    }

    /**
     * Appends HIGHDPI flag to window is device has high DPI.
     * 
     * @param flags Window flags.
     * @param threshold DPI Threshold.
     */
    public static void setWinHighDpi(WindowFlags flags, float threshold) {
        if(isHighDpi(threshold)) {
            flags.append(Raylib.FLAG_WINDOW_HIGHDPI);

            flags.setWindowFlags();
        }
    }

    /**
     * Is window has HIGHDPI flag set.
     * 
     * @param flags Window flags.
     */
    public static boolean isWinHighDpi(WindowFlags flags) {
        return flags.has(Raylib.FLAG_WINDOW_HIGHDPI);
    }

    /**
     * Get device DPI.
     */
    public static float[] getDpi() {
        int cMonitor = Raylib.GetCurrentMonitor();

        return new float[] {
            Raylib.GetMonitorWidth(cMonitor) / (Raylib.GetMonitorPhysicalWidth(cMonitor) / 25.4f),
            Raylib.GetMonitorHeight(cMonitor) / (Raylib.GetMonitorPhysicalHeight(cMonitor) / 25.4f),
        };
    }

    /**
     * Detects is device has high DPI. Works approximately and depends on threshold.
     * 
     * @param threshold DPI Threshold.
     */
    public static boolean isHighDpi(float threshold) {
        float[] dpi = getDpi();

        return dpi[0] > threshold || dpi[1] > threshold;
    }

    /**
     * Window flags container.
     */
    public static class WindowFlags {
        private int flags;

        /**
         * Initialize window flags.
         * 
         * @param flags_ Flags.
         */
        public WindowFlags(int flags_) {
            flags = flags_;
        }

        /**
         * Initialize window flags.
         */
        public WindowFlags() {
            flags = 0;
        }

        /**
         * Append flags to existing flags.
         * 
         * @param newFlags New flags.
         */
        public void append(int newFlags) {
            flags = flags | newFlags;
        }

        /**
         * Remove existing flag.
         * 
         * @param flag Flags.
         */
        public void remove(int flag) {
            flags &= ~flag;
        }

        /**
         * Remove existing flags.
         * 
         * @param flags_ Flags.
         */
        public void remove(int ...flags_) {
            for(int flag : flags_) {
                remove(flag);
            }
        }

        /**
         * Is flags contain one single flag.
         * 
         * @param flag Flag.
         */
        public boolean has(int flag) {
            return (flags & flag) != 0;
        }

        /**
         * Get window flags.
         */
        public int get() {
            return flags;
        }

        /**
         * Update window flags.
         */
        public void setWindowFlags() {
            Raylib.SetWindowState(flags);
        }
    }
}
