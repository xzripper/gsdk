package gsdk.source.vectors;

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
     * Cast vector to Raylib color (auto cast from float RGBA (0.0f->1.0f to integer RGBA (0->255).
     */
    public Raylib.Color toRlCol() {
        return new Raylib.Color()
            .r((byte) ((x >= 0.0f && x <= 1.0f) ? x * 255.0f : x))
            .g((byte) ((y >= 0.0f && y <= 1.0f) ? y * 255.0f : y))
            .b((byte) ((z >= 0.0f && z <= 1.0f) ? z * 255.0f : z))
            .a((byte) ((w >= 0.0f && w <= 1.0f) ? w * 255.0f : w));
    }

    /**
     * Cast vector to array.
     */
    public float[] toArray() {
        return new float[] {x, y, z, w};
    }

    /**
     * Cast vector to double array.
     */
    public double[] toDoubleArray() {
        return new double[] {(double) x, (double) y, (double) z, (double) w};
    }

    /**
     * Shortcut for creating 4D float vector.
     */
    public static Vector4Df vec4df(float x, float y, float z, float w) {
        return new Vector4Df(x, y, z, w);
    }
}
