package vsdk.source;

import com.raylib.Raylib;

/**
 * Float 3D Vector.
 */
public class Vector3Df {
    private float x;
    private float y;
    private float z;

    /**
     * Create new 3D float vector.
     */
    public Vector3Df(float x_, float y_, float z_) {
        x = x_;
        y = y_;
        z = z_;
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
     * Cast vector Raylib vector.
     */
    public Raylib.Vector3 toRlVec() {
        return new Raylib.Vector3().x(x).y(y).z(z);
    }

    /**
     * Cast vector to array.
     */
    public float[] toArray() {
        return new float[] {x, y, z};
    }
}
