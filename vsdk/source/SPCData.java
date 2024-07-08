package vsdk.source;

import com.raylib.Raylib;

import static com.raylib.Jaylib.GREEN;

/**
 * Sprite Pixel Collider Data.
 */
public class SPCData {
    /**
     * Total collider points loaded.
     */
    public static int TOTAL_POINTS_LOADED = 0;

    /**
     * Total bake requests.
     */
    public static int TOTAL_BAKE_REQUESTS = 0;

    /**
     * Total collider points baked.
     */
    public static int TOTAL_POINTS_BAKED = 0;

    /**
     * Total collider collision points baked.
     */
    public static int TOTAL_COLLISION_POINTS_BAKED = 0;

    /**
     * Total collision checks.
     */
    public static int TOTAL_COLLISION_CHECKS = 0;

    /**
     * Total succeeded collisions.
     */
    public static int TOTAL_COLLISIONS_SUCCEEDED = 0;

    /**
     * Total non-collision cases.
     */
    public static int TOTAL_COLLISIONS_FAILED = 0;

    /**
     * Render debug information.
     *
     * @param x Position X.
     * @param y Position Y.
     * @param color Color.
     */
    public static void debug(int x, int y, Raylib.Color color) {
        Raylib.DrawText("VSDK SPPS-technique | SpritePixelCollider Debug:", x, y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_POINTS_LOADED: %d", SPCData.TOTAL_POINTS_LOADED), x, 15 + y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_BAKE_REQUESTS: %d", SPCData.TOTAL_BAKE_REQUESTS), x, 30 + y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_POINTS_BAKED: %d", SPCData.TOTAL_POINTS_BAKED), x, 45 + y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_COLLISION_POINTS_BAKED: %d", SPCData.TOTAL_COLLISION_POINTS_BAKED), x, 60 + y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_COLLISION_CHECKS: %d", SPCData.TOTAL_COLLISION_CHECKS), x, 75 + y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_COLLISIONS_SUCCEEDED: %d", SPCData.TOTAL_COLLISIONS_SUCCEEDED), x, 90 + y, 16, color);
        Raylib.DrawText(String.format("SPCData::TOTAL_COLLISIONS_FAILED: %d", SPCData.TOTAL_COLLISIONS_FAILED), x, 105 + y, 16, color);
    }

    /**
     * Render debug information.
     *
     * @param x Position X.
     * @param y Position Y.
     */
    public static void debug(int x, int y) {
        debug(x, y, GREEN);
    }

    /**
     * Render debug information.
     *
     * @param x Position X.
     */
    public static void debug(int x) {
        debug(x, 0);
    }
}
