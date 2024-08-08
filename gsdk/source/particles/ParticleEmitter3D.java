/**
 * 3D Particles' implementation by XZRipper in the GSDK project: VFlux.
 * VFlux is currently in an unstable state and under active development.
 * Known issues:
 * - Particles have invalid rotation origin (only if rotation is used).
 * - Particles may have black borders (see https://www.reddit.com/r/raylib/comments/1e11xdo/weird_texture_billboard_black_borders/):
 *      | Possible fix is to use setPFColThreshold and calibrate alpha for your particle texture.
 * - Particles have wrong facing: particles only face the camera horizontally, not vertically (Raylib problem).
 * - Particles have wrong positioning (no camera distance sorting).
 * - Particles can spawn quickly at startup, even if emission rate is greater than 1.0f.
 * - Particles may turn black when opacity is lowered, depending on their lifetime.
 * - Particle simulation is slow: using trivial for-loop to iterate particles.
 * - Particles may spawn with more black tint than others after some emission time (probably fixed).
 * - Particle simulation can freeze game/application after some emission time.
 *
 * TODO Features:
 * - Color curve based on lifetime (e.g red at start and green at end).
 * - Add support for multiple textures (each particle emission - new texture).
 * - Random fade delay for each particle (based of pFade).
 * - Random initial rotation for each particle.
 * - Simulate only visible particles (optimization).
 * - Improve code quality (generic).
 */

package gsdk.source.particles;

import com.raylib.Raylib;

import java.util.Random;

import org.bytedeco.javacpp.FloatPointer;

import gsdk.source.grender.Texture;

import gsdk.source.vectors.Vector4Df;
import gsdk.source.vectors.Vector4Di;
import gsdk.source.vectors.Vector3Df;
import gsdk.source.vectors.Vector2Df;

import static gsdk.source.generic.ImagePixelsFilter.filterPixels;

import static gsdk.source.generic.VMath.clamp;

import static gsdk.source.generic.VLogger.warning;

import static gsdk.source.generic.Assert.assert_t;

import static gsdk.r_utilities.PathResolver.resolvePath;

/**
 * 3D particle emitter for the GSDK project: VFlux.
 * Handles the creation, simulation, and rendering of particles in a 3D environment.
**/
public class ParticleEmitter3D {
    private final ParticleEmitterConfig emitterConfig;

    private final Particle[] particleContainer;

    private Texture particleTex = null;

    private float lastSpawn;

    private final Raylib.Shader pixelsFormatShader;

    private final int pixelsFormatShaderThresholdLoc;

    private final Random random;

    public static final Vector4Df PFCOL_THRESHOLD_DEFAULT = new Vector4Df(0.15f, 0.15f, 0.15f, 0.75f);

    public static final float[] CENTER = new float[] {0, 0, 0};

    /**
     * Initialize 3D particle emitter.
     *
     * @param emitterConfig_ Emitter config.
     */
    public ParticleEmitter3D(ParticleEmitterConfig emitterConfig_) {
        emitterConfig = emitterConfig_;

        particleContainer = new Particle[emitterConfig.getMaxParticles()];

        pixelsFormatShader = Raylib.LoadShader(null, resolvePath("gsdk/shaders/pe3d_ppfilter.fs"));

        pixelsFormatShaderThresholdLoc = Raylib.GetShaderLocation(pixelsFormatShader, "threshold");

        setPFColThreshold(PFCOL_THRESHOLD_DEFAULT);

        lastSpawn = 1.0f / emitterConfig.getEmissionRate();

        random = new Random();
    }

    /**
     * Set pixel filter shader color threshold.
     *
     * @param threshold Threshold vector.
     */
    public void setPFColThreshold(Vector4Df threshold) {
        assert_t(pixelsFormatShaderThresholdLoc == -1, "pixelsFormatShaderThresholdLoc == -1");

        Raylib.SetShaderValue(pixelsFormatShader, pixelsFormatShaderThresholdLoc, new FloatPointer(threshold.toArray()), Raylib.SHADER_UNIFORM_VEC4);
    }

    /**
     * Set pixel filter shader color threshold.
     *
     * @param thrR Red threshold.
     * @param thrG Green threshold.
     * @param thrB Blue threshold.
     * @param thrA Alpha threshold.
     */
    public void setPFColThreshold(float thrR, float thrG, float thrB, float thrA) {
        setPFColThreshold(new Vector4Df(thrR, thrG, thrB, thrA));
    }

    /**
     * Set particle texture. The texture will not be updated if the particle type is not TEXTURE.
     *
     * @param tex Texture.
     */
    public void setParticleTex(Texture tex) {
        particleTex = emitterConfig.getPType() == ParticleType.TEXTURE ? tex : particleTex;

        Raylib.GenTextureMipmaps(particleTex.getTex());

        tex.setTexFilter(Texture.TEX_FILTER_BILINEAR);
    }

    /**
     * Removes all pixels with specified color with possible threshold.
     *
     * @param color Color.
     * @param threshold Threshold.
     */
    public void filterParticleTex(Vector4Di color, Vector4Di threshold) {
        if(particleTex.getTex() == null) return;

        Raylib.Image image = Raylib.LoadImageFromTexture(particleTex.getTex());

        Raylib.Image filtered = filterPixels(
            image, new Raylib.Color()
                .r((byte) color.x())
                .g((byte) color.y())
                .b((byte) color.z())
                .a((byte) color.w()), threshold);

        particleTex = new Texture(filtered);

        Raylib.GenTextureMipmaps(particleTex.getTex());

        Raylib.UnloadImage(image);
        Raylib.UnloadImage(filtered);
    }

    /**
     * Removes all pixels with specified color without any threshold.
     *
     * @param color Color.
     */
    public void filterParticleTex(Vector4Di color) {
        filterParticleTex(color, new Vector4Di(0, 0, 0, 0));
    }

    /**
     * Fill particle container with particles.
     */
    public void loadParticles() {
        warning("VFlux is currently in an unstable state and under active development.");

        for(int i=0; i < emitterConfig.getMaxParticles(); i++) {
            particleContainer[i] = new Particle(
                CENTER,
                emitterConfig.getPColor().toArray(),
                emitterConfig.getPAlpha(),
                emitterConfig.getPScale(),
                emitterConfig.getPRotation() > 0 ? Raylib.GetRandomValue(0, 360) : 0,
                emitterConfig.getEmissionVelocity()
                    .calcVelocity(
                        randomFloat(random, -emitterConfig.getExplosiveness(), emitterConfig.getExplosiveness()),
                        randomFloat(random, -emitterConfig.getExplosiveness(), emitterConfig.getExplosiveness())), i);
        }
    }

    /**
     * Iterates each particle and simulates its behaviour.
     */
    public void simulateParticles() {
        assert_t(!emitterConfig.deltaUpdated(), "deltaUpdated == false: use ParticleEmitterConfig::setDelta to update delta");

        if(emitterConfig.getPLifetime() < 2.0f) warning("pLifetime_ < 2.0f! particles lifetime is TOO low!; i.e expect non-smooth fades & etc");

        lastSpawn += emitterConfig.getDelta();

        for(Particle particle : particleContainer) {
            float[] velocity = particle.getDesignatedVelocity();

            if (emitterConfig.getInversedEmission()) {
                velocity[0] = -velocity[0];
                velocity[1] = -velocity[1];
                velocity[2] = -velocity[2];
            }

            if(particle.getLifetime() >= emitterConfig.getPLifetime() / 2) particle.addSize(emitterConfig.getPScale() * 0.001f);

            particle.addPos(velocity[0], velocity[1], velocity[2]);
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

                    particle.setSize((float) clamp(0, emitterConfig.getPScale(), particle.getSize() - (emitterConfig.getPScale() * 0.1f)));
                }

                if(particle.isDead()) {
                    if(lastSpawn >= (1.0 / emitterConfig.getEmissionRate())) {
                        particle.setSize(emitterConfig.getPScale());
                        particle.setPos(CENTER);

                        particle.setAlpha(0);

                        particle.setLifetime(emitterConfig.getPLifetime());

                        particle.setSpawningProcess(true);

                        lastSpawn = 0.0f;
                    }
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

        Raylib.BeginShaderMode(pixelsFormatShader);

        if(emitterConfig.blendingAvailable()) {
            Raylib.BeginBlendMode(emitterConfig.getBlending() == ParticleBlending.ALPHA ? Raylib.BLEND_ALPHA : Raylib.BLEND_ADDITIVE);
        }

        for(Particle particle : particleContainer) {
            if(emitterConfig.getPType() == ParticleType.RECTANGLE) {
                // Rectangle...
            } else if(emitterConfig.getPType() == ParticleType.CIRCLE) {
                // Circle...
            } else if(emitterConfig.getPType() == ParticleType.HEXAGON) {
                // Hexagon...
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
                // Custom...
            }
        }

        if(emitterConfig.blendingAvailable()) {
            Raylib.EndBlendMode();
        }

        Raylib.EndShaderMode();
    }

    /**
     * Get emitter config.
     */
    public ParticleEmitterConfig getEmitterConfig() {
        return emitterConfig;
    }

    /**
     * Unload emitter resources.
     */
    public void unloadResources() {
        if(particleTex != null) particleTex.unload();

        Raylib.UnloadShader(pixelsFormatShader);
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
