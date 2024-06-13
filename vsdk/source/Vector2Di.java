package vsdk.source;

import com.raylib.Raylib;

/**
 * Int 2D Vector.
 */
public class Vector2Di {
    private int x;
    private int y;

    /**
     * Create new 2D int vector.
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
     * Cast vector Raylib vector.
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
}
