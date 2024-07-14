package vsdk.source.vectors;

import com.raylib.Raylib;

/**
 * Integer 2D Vector.
 */
public class Vector2Di {
    private int x, y;

    /**
     * Create new 2D integer vector.
     */
    public Vector2Di(int x_, int y_) {
        x = x_;
        y = y_;
    }

    /**
     * Set X.
     */
    public void x(int x_) {
        x = x_;
    }

    /**
     * Get X.
     */
    public int x() {
        return x;
    }

    /**
     * Set Y.
     */
    public void y(int y_) {
        y = y_;
    }

    /**
     * Get Y.
     */
    public int y() {
        return y;
    }

    /**
     * Cast vector to Raylib vector.
     */
    public Raylib.Vector2 toRlVec() {
        return new Raylib.Vector2().x(x).y(y);
    }

    /**
     * Cast vector to array.
     */
    public int[] toArray() {
        return new int[] {x, y};
    }

    /**
     * Shortcut for creating 2D integer vector.
     */
    public static Vector2Di vec2di(int x, int y) {
        return new Vector2Di(x, y);
    }
}
