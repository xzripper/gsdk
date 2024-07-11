package vsdk.source.particles;

import com.raylib.Raylib;

import java.util.Random;

import vsdk.source.vrender.Texture;

import vsdk.source.vectors.Vector4Di;
import vsdk.source.vectors.Vector3Df;
import vsdk.source.vectors.Vector2Df;

import static vsdk.source.utils.VMath.clamp;

import static vsdk.source.utils.VLogger.warning;

import static vsdk.source.utils.Assert.assert_t;

public class ParticleEmitter3D {
    private final ParticleEmitterConfig emitterConfig;

    private final Particle[] particleContainer;

    private Texture particleTex = null;

    private final Random random;

    public static final float[] CENTER = new float[] {0, 0, 0};

    /**
     * Initialize 3D particle emitter.
     *
     * @param emitterConfig_ Emitter config.
     */
    public ParticleEmitter3D(ParticleEmitterConfig emitterConfig_) {
        emitterConfig = emitterConfig_;

        particleContainer = new Particle[emitterConfig.getMaxParticles()];

        random = new Random();
    }

    /**
     * Set particle texture. The texture will not be updated if the particle type is not TEXTURE.
     *
     * @param tex Texture.
     */
    public void setParticleTex(Texture tex) {
        particleTex = emitterConfig.getPType() == ParticleType.TEXTURE ? tex : particleTex;
    }

    /**
     * Fill particle container with particles.
     */
    public void loadParticles() {
        for(int i=0; i < emitterConfig.getMaxParticles(); i++) {
            particleContainer[i] = new Particle(
                new float[] {0, 0, 0},
                emitterConfig.getPColor().toArray(),
                emitterConfig.getPAlpha(),
                emitterConfig.getPScale(),
                emitterConfig.getPRotation() > 0 ? Raylib.GetRandomValue(0, 360) : 0,
                new float[]{
                    emitterConfig.getEmissionVelocity()[0] * 0.1f * randomFloat(random, 0.1f, emitterConfig.getExplosiveness()),
                    emitterConfig.getEmissionVelocity()[1] * 0.1f,
                    emitterConfig.getEmissionVelocity()[2] * 0.1f * randomFloat(random, 0.1f, emitterConfig.getExplosiveness())
                },
                i);
        }
    }

    public void simulateParticles() {
        assert_t(!emitterConfig.deltaUpdated(), "deltaUpdated == false: use ParticleEmitterConfig::setDelta to update delta");

        if(emitterConfig.getPLifetime() < 2.0f) warning("pLifetime_ < 2.0f! particles lifetime is TOO low!; i.e expect non-smooth fades & etc");

        for(Particle particle : particleContainer) {
            // Base velocity
//            float[] velocity = new float[]{
//                emitterConfig.getEmissionVelocity()[0] * 0.1f,
//                emitterConfig.getEmissionVelocity()[1] * 0.1f,
//                emitterConfig.getEmissionVelocity()[2] * 0.1f
//            };

            if (emitterConfig.getInversedEmission()) {
                particle.getDesignatedVelocity()[0] = -particle.getDesignatedVelocity()[0];
                particle.getDesignatedVelocity()[1] = -particle.getDesignatedVelocity()[1];
                particle.getDesignatedVelocity()[2] = -particle.getDesignatedVelocity()[2];
            }

//            // Apply random offset based on explosiveness
//            float explosiveness = emitterConfig.getExplosiveness();
//            float randomOffsetX = (random.nextFloat() - 0.5f) * explosiveness;
//            float randomOffsetY = (random.nextFloat() - 0.5f) * explosiveness;
//            float randomOffsetZ = (random.nextFloat() - 0.5f) * explosiveness;
//
//            particle.addPos(velocity[0] + randomOffsetX, velocity[1] + randomOffsetY, velocity[2] + randomOffsetZ);


            particle.addPos(particle.getDesignatedVelocity()[0], particle.getDesignatedVelocity()[1], particle.getDesignatedVelocity()[2]);

            particle.addSize(emitterConfig.getPScale() * 0.01f);

            particle.addRotation(emitterConfig.getPRotation());

            particle.subLifetime(emitterConfig.getDelta());

            if(particle.getSpawningProcess()) {
                particle.addAlpha(emitterConfig.getPFade() * 0.1f);

                if(particle.getAlpha() >= emitterConfig.getPAlpha()) {
                    particle.setAlpha(emitterConfig.getPAlpha());

                    particle.setSpawningProcess(false);
                }
            } else {
                if(particle.getLifetime() <= emitterConfig.getPLifetime() / 2) {
                    particle.setAlpha((float) clamp(0, emitterConfig.getPAlpha(), particle.getAlpha() - (emitterConfig.getPFade() * 0.1f)));
                }

                if(particle.isDead() || particle.getAlpha() <= 0) {
                    particle.setSize(emitterConfig.getPScale());

                    particle.setPos(CENTER);

                    particle.setAlpha(0);

                    particle.setLifetime(emitterConfig.getPLifetime());

                    particle.setSpawningProcess(true);

                }
            }
        }
    }

    /**
     * Render particles as billboard.
     *
     * @param cam 3D Camera.
     * @param pos Position.
     */
    public void renderParticles(Raylib.Camera3D cam, Vector3Df pos) {
        assert_t(emitterConfig.getPType() == ParticleType.TEXTURE && particleTex == null, "can't render: particle type is texture but texture is null");

        for(Particle particle : particleContainer) {
            if(emitterConfig.blendingAvailable()) {
                Raylib.BeginBlendMode(emitterConfig.getBlending() == ParticleBlending.ADDITIVE ? Raylib.BLEND_ADDITIVE : Raylib.BLEND_ALPHA);
            }

            if(emitterConfig.getPType() == ParticleType.RECTANGLE) {
                // RECTANGLE...
            } else if(emitterConfig.getPType() == ParticleType.CIRCLE) {
                // circle...
            } else if(emitterConfig.getPType() == ParticleType.HEXAGON) {
                // hexagon..
            } else if(emitterConfig.getPType() == ParticleType.TEXTURE) {
                Raylib.DrawBillboardPro(
                    cam, particleTex.getTex(), new Vector4Di(0, 0, particleTex.getTexWidth(), particleTex.getTexHeight()).toRlRect(),

                    new Vector3Df(
                        pos.x() + particle.getPos()[0],
                        pos.y() + particle.getPos()[1],
                        pos.z() + particle.getPos()[2]).toRlVec(),

                    new Vector3Df(0.0f, 1.0f, 0.0f).toRlVec(),

                    new Vector2Df(particle.getSize(), particle.getSize()).toRlVec(),

                    new Vector2Df(particle.getSize() / 4, particle.getSize() / 4).toRlVec(),

                    particle.getRotation(),

                    new Raylib.Color()
                        .r((byte) particle.getColor()[0])
                        .g((byte) particle.getColor()[1])
                        .b((byte) particle.getColor()[2])
                        .a((byte) (particle.getAlpha() * 255.0f))
                );
            } else if(emitterConfig.getPType() == ParticleType.CUSTOM) {
                // custom..
            }

            if(emitterConfig.blendingAvailable()) {
                Raylib.EndBlendMode();
            }
        }
    }

    /**
     * Generate random float between start and end.
     *
     * @param random Random class.
     * @param start Float start.
     * @param end Float end.
     */
    public static float randomFloat(Random random, float start, float end) {
        return start + random.nextFloat() * (end - start);
    }
}
