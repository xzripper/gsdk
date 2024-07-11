package vsdk.source.particles;

import vsdk.source.vectors.Vector3Di;

/**
 * Particle emitter config.
 */
public class ParticleEmitterConfig {
    private final int maxParticles;

    private float explosiveness, emissionRate;

    private float[] emissionVelocity;

    private boolean inversedEmission;

    private ParticleBlending blending;

    private float pLifetime, pScale, pRotation, pAlpha, pFade;

    private Vector3Di pColor;

    private ParticleType pType;

    private float delta = -1.0f;

    /**
     * Create new config for particle emitter.
     *
     * @param maxParticles_ Maximal particles possible (constant).
     * @param explosiveness_ "Explosiveness" level for particles.
     * @param emissionRate_ Emission rate.
     * @param emissionVelocity_ Emission velocity.
     * @param inversedEmission_ Make emission inversed?
     * @param blending_ Particles blending.
     * @param pLifetime_ Each particle lifetime.
     * @param pScale_ Each particle scale.
     * @param pRotation_ Each particle rotation.
     * @param pAlpha_ Each particle alpha.
     * @param pFade_ Each particle fade delay.
     * @param pColor_ Each particle color.
     * @param pType_ Each particle type.
     */
    public ParticleEmitterConfig(
        int maxParticles_,

        float explosiveness_,
        float emissionRate_,

        float[] emissionVelocity_,

        boolean inversedEmission_,

        ParticleBlending blending_,

        float pLifetime_,
        float pScale_,
        float pRotation_,
        float pAlpha_,
        float pFade_,

        Vector3Di pColor_,

        ParticleType pType_
    ) {
        maxParticles = maxParticles_;

        explosiveness = explosiveness_;
        emissionRate = emissionRate_;

        emissionVelocity = emissionVelocity_;

        inversedEmission = inversedEmission_;

        blending = blending_;

        pLifetime = pLifetime_ + (pLifetime_ <= 5.0f ? 1.0f : 0.0f); // Particle fade out additional time.

        pScale = pScale_;
        pRotation = pRotation_;
        pAlpha = pAlpha_;

        pFade = pFade_ + (pLifetime <= 4.5f ? 0.1f : 0.0f); // Particle fade out additional smoothing.

        pColor = pColor_;

        pType = pType_;
    }

    /**
     * Set emitter delta.
     *
     * @param delta_ Frame time (delta).
     */
    public void setDelta(float delta_) {
        delta = delta_;
    }

    /**
     * Get emitter delta.
     */
    public float getDelta() {
        return delta;
    }

    /**
     * Is delta being updated?
     */
    public boolean deltaUpdated() {
        return delta != -1.0f;
    }

    /**
     * Get maximal particles.
     */
    public int getMaxParticles() {
        return maxParticles;
    }

    /**
     * Set particles explosiveness.
     *
     * @param explosiveness_ Explosiveness.
     */
    public void setExplosiveness(float explosiveness_) {
        explosiveness = explosiveness_;
    }

    /**
     * Get particles explosiveness.
     */
    public float getExplosiveness() {
        return explosiveness;
    }

    /**
     * Set particles emission rate.
     *
     * @param emissionRate_ Emission rate.
     */
    public void setEmissionRate(float emissionRate_) {
        emissionRate = emissionRate_;
    }

    /**
     * Get particles emission rate.
     */
    public float getEmissionRate() {
        return emissionRate;
    }

    /**
     * Set emission velocity.
     *
     * @param velocity Velocity.
     */
    public void setEmissionVelocity(float[] velocity) {
        emissionVelocity = velocity;
    }

    /**
     * Get emission velocity.
     */
    public float[] getEmissionVelocity() {
        return emissionVelocity;
    }

    /**
     * Set particles fade.
     *
     * @param pFade_ Particle fade.
     */
    public void setPFade(float pFade_) {
        pFade = pFade_;
    }

    /**
     * Get particles fade.
     */
    public float getPFade() {
        return pFade;
    }

    /**
     * Set inversed emission for particles.
     *
     * @param inversedEmission_ Inversed emission?
     */
    public void setInversedEmission(boolean inversedEmission_) {
        inversedEmission = inversedEmission_;
    }

    /**
     * Is emission inversed?
     */
    public boolean getInversedEmission() {
        return inversedEmission;
    }

    /**
     * Set particles blending.
     *
     * @param blending_ Particles blending.
     */
    public void setBlending(ParticleBlending blending_) {
        blending = blending_;
    }

    /**
     * Get particles blending.
     */
    public ParticleBlending getBlending() {
        return blending;
    }

    /**
     * Is blending available?
     */
    public boolean blendingAvailable() {
        return blending != null && !blending.isNone();
    }

    /**
     * Set particles lifetime.
     *
     * @param pLifetime_ Particles lifetime.
     */
    public void setPLifetime(float pLifetime_) {
        pLifetime = pLifetime_;
    }

    /**
     * Get particles lifetime.
     */
    public float getPLifetime() {
        return pLifetime;
    }

    /**
     * Set particles scale.
     *
     * @param pScale_ Particles scale.
     */
    public void setPScale(float pScale_) {
        pScale = pScale_;
    }

    /**
     * Get particles scale.
     */
    public float getPScale() {
        return pScale;
    }

    /**
     * Set particles rotation.
     *
     * @param rotation Rotation.
     */
    public void setPRotation(float rotation) {
        pRotation = rotation;
    }

    /**
     * Get particles rotation.
     */
    public float getPRotation() {
        return pRotation;
    }

    /**
     * Set particles alpha.
     *
     * @param pAlpha_ Particles alpha.
     */
    public void setPAlpha(float pAlpha_) {
        pAlpha = pAlpha_;
    }

    /**
     * Get particles alpha.
     */
    public float getPAlpha() {
        return pAlpha;
    }

    /**
     * Set particles color.
     *
     * @param pColor_ Particles color.
     */
    public void setPColor(Vector3Di pColor_) {
        pColor = pColor_;
    }

    /**
     * Get particles color.
     */
    public Vector3Di getPColor() {
        return pColor;
    }

    /**
     * Set particles type.
     *
     * @param pType_ Particles type.
     */
    public void setPType(ParticleType pType_) {
        pType = pType_;
    }

    /**
     * Get particles type.
     */
    public ParticleType getPType() {
        return pType;
    }
}
