package vsdk.source.vectors;

import com.raylib.Raylib;

/**
 * Integer 3D Vector.
 */
public class Vector3Di {
    private int x, y, z;

    /**
     * Create new 3D integer vector.
     */
    public Vector3Di(int x_, int y_, int z_) {
        x = x_;
        y = y_;
        z = z_;
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
     * Cast vector to Raylib vector.
     */
    public Raylib.Vector3 toRlVec() {
        return new Raylib.Vector3().x(x).y(y).z(z);
    }

    /**
     * Cast vector to array.
     */
    public int[] toArray() {
        return new int[] {x, y, z};
    }
}
