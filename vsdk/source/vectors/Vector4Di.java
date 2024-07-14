package vsdk.source.vectors;

import com.raylib.Raylib;

/**
 * Integer 4D Vector.
 */
public class Vector4Di {
    private int x, y, z, w;

    /**
     * Create new 4D integer vector.
     */
    public Vector4Di(int x_, int y_, int z_, int w_) {
        x = x_;
        y = y_;
        z = z_;
        w = w_;
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
     * Set Z.
     */
    public void z(int z_) {
        z = z_;
    }

    /**
     * Get Z.
     */
    public int z() {
        return z;
    }

    /**
     * Set W.
     */
    public void w(int w_) {
        w = w_;
    }

    /**
     * Get W.
     */
    public int w() {
        return w;
    }

    /**
     * Cast vector to Raylib vector.
     */
    public Raylib.Vector4 toRlVec() {
        return new Raylib.Vector4().x(x).y(y).z(z).w(w);
    }

    /**
     * Cast vector to Raylib rectangle.
     */
    public Raylib.Rectangle toRlRect() {
        return new Raylib.Rectangle().x(x).y(y).width(z).height(w);
    }

    /**
     * Cast vector to Raylib color.
     */
    public Raylib.Color toRlCol() {
        return new Raylib.Color().r((byte) x).g((byte) y).b((byte) z).a((byte) w);
    }

    /**
     * Cast vector to array.
     */
    public int[] toArray() {
        return new int[] {x, y, z, w};
    }

    /**
     * Shortcut for creating 4D integer vector.
     */
    public static Vector4Di vec4di(int x, int y, int z, int w) {
        return new Vector4Di(x, y, z, w);
    }
}
