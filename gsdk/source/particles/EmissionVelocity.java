package gsdk.source.particles;

/**
 * Class-utility for saving/calculating velocities.
 */
public class EmissionVelocity {
    private float x, y, z;

    private int xFlag, zFlag;

    public static final int X_ADD_EXPL = 1;
    public static final int X_SUB_EXPL = 2;
    public static final int X_MULT_EXPL = 3;
    public static final int X_DIV_EXPL = 4;

    public static final int Z_ADD_EXPL = 5;
    public static final int Z_SUB_EXPL = 6;
    public static final int Z_MULT_EXPL = 7;
    public static final int Z_DIV_EXPL = 8;

    /**
     * Initialize emission velocity class.
     *
     * @param x_ X Position.
     * @param y_ Y Position.
     * @param z_ Z Position.
     * @param xFlag_ X Flag.
     * @param zFlag_ Z Flag.
     */
    public EmissionVelocity(float x_, float y_, float z_, int xFlag_, int zFlag_) {
        x = x_;
        y = y_;
        z = z_;

        xFlag = xFlag_;
        zFlag = zFlag_;
    }

    /**
     * Update emission velocity.
     *
     * @param x_ X Position.
     * @param y_ Y Position.
     * @param z_ Z Position.
     * @param xFlag_ X Flag.
     * @param zFlag_ Z Flag.
     */
    public void newVelocity(float x_, float y_, float z_, int xFlag_, int zFlag_) {
        x = x_;
        y = y_;
        z = z_;

        xFlag = xFlag_;
        zFlag = zFlag_;
    }

    /**
     * Calculate particle velocity.
     *
     * @param randomizedExplX Randomized X explosion.
     * @param randomizedExplZ Randomized Z explosion.
     */
    public float[] calcVelocity(float randomizedExplX, float randomizedExplZ) {
        float xVel = x * 0.1f, yVel = y, zVel = z * 0.1f;

        if(xFlag == X_ADD_EXPL) xVel += randomizedExplX;
        else if(xFlag == X_SUB_EXPL) xVel -= randomizedExplX;
        else if(xFlag == X_MULT_EXPL) xVel *= randomizedExplX;
        else if(xFlag == X_DIV_EXPL) xVel /= randomizedExplX;

        yVel += (randomizedExplX + randomizedExplZ) * 0.01f;

        if(zFlag == Z_ADD_EXPL) zVel += randomizedExplZ;
        else if(zFlag == Z_SUB_EXPL) zVel -= randomizedExplZ;
        else if(zFlag == Z_MULT_EXPL) zVel *= randomizedExplZ;
        else if(zFlag == Z_DIV_EXPL) zVel /= randomizedExplZ;

        return new float[] {xVel, yVel, zVel};
    }

    /**
     * Get XYZ array.
     */
    public float[] getXYZ() {
        return new float[] {x, y, z};
    }

    /**
     * Get X flag.
     */
    public int getXFlag() {
        return xFlag;
    }

    /**
     * Get Z flag.
     */
    public int getZFlag() {
        return zFlag;
    }
}
