package vsdk.source.vectors;

import com.raylib.Raylib;

/**
 * Float 4D Vector.
 */
public class Vector4Df {
    private float x, y, z, w;

    /**
     * Create new 4D float vector.
     */
    public Vector4Df(float x_, float y_, float z_, float w_) {
        x = x_;
        y = y_;
        z = z_;
        w = w_;
    }

    /**
     * Set X.
     */
    public void x(float x_) {
        x = x_;
    }

    /**
     * Get X.
     */
    public float x() {
        return x;
    }

    /**
     * Set Y.
     */
    public void y(float y_) {
        y = y_;
    }

    /**
     * Get Y.
     */
    public float y() {
        return y;
    }

    /**
     * Set Z.
     */
    public void z(float z_) {
        z = z_;
    }

    /**
     * Get Z.
     */
    public float z() {
        return z;
    }

    /**
     * Set W.
     */
    public void w(float w_) {
        w = w_;
    }

    /**
     * Get W.
     */
    public float w() {
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
     * Cast vector to array.
     */
    public float[] toArray() {
        return new float[] {x, y, z, w};
    }
}
