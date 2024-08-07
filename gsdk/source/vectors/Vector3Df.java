package gsdk.source.vectors;

import com.raylib.Raylib;

/**
 * Float 3D Vector.
 */
public class Vector3Df {
    private float x, y, z;

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
     * Cast vector to Raylib vector.
     */
    public Raylib.Vector3 toRlVec() {
        return new Raylib.Vector3().x(x).y(y).z(z);
    }

    /**
     * Cast vector to Raylib color (auto cast from float RGBA (0.0f->1.0f to integer RGBA (0->255). Alpha value is set to 255.
     */
    public Raylib.Color toRlCol() {
        return new Raylib.Color()
            .r((byte) ((x >= 0.0f && x <= 1.0f) ? x * 255.0f : x))
            .g((byte) ((y >= 0.0f && y <= 1.0f) ? y * 255.0f : y))
            .b((byte) ((z >= 0.0f && z <= 1.0f) ? z * 255.0f : z))
            .a((byte) 255);
    }

    /**
     * Cast vector to array.
     */
    public float[] toArray() {
        return new float[] {x, y, z};
    }


    /**
     * Cast vector to double array.
     */
    public double[] toDoubleArray() {
        return new double[] {(double) x, (double) y, (double) z};
    }

    /**
     * Shortcut for creating 3D float vector.
     */
    public static Vector3Df vec3df(float x, float y, float z) {
        return new Vector3Df(x, y, z);
    }
}
