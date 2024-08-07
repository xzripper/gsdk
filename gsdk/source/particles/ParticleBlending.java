package vsdk.source.particles;

/**
 * Particle blending modes.
 */
public enum ParticleBlending {
    /**
     * No blending mode.
     */
    NONE,

    /**
     * Alpha blending mode.
     */
    ALPHA,

    /**
     * Additive blending mode.
     */
    ADDITIVE;

    /**
     * Is current blending mode none.
     */
    public boolean isNone() {
        return this == NONE;
    }
}
