package gsdk.source.shaders;

import gsdk.source.vectors.Vector2Di;

import gsdk.source.generic.VShader;

import static gsdk.source.generic.VLogger.warning;

import static gsdk.r_utilities.PathResolver.resolvePath;

import static gsdk.source.generic.Assert.assert_t;

/**
 * Utility class for using blur shader.
 */
public class BlurShader {
    private static final String BLUR_SHADER_VERT_PATH = resolvePath("gsdk/shaders/blur.vs");
    private static final String BLUR_SHADER_FRAG_PATH = resolvePath("gsdk/shaders/blur.fs");

    private static VShader blurShader;

    /**
     * Load blur shader.
     * 
     * @param texSize Texture size.
     * @param radius Blur radius.
     */
    public static void loadBlurShader(Vector2Di texSize, float radius) {
        blurShader = new VShader(BLUR_SHADER_VERT_PATH, BLUR_SHADER_FRAG_PATH, VShader.FILE);

        blurShader.setUniformFloat("xs", texSize.x());
        blurShader.setUniformFloat("ys", texSize.y());

        blurShader.setUniformFloat("r", radius);

        if(radius >= 24) {
            warning("High blur radius may cause performance issues (%f >= 24).".formatted(radius));
        }
    }

    /**
     * Set texture size (shader).
     * 
     * @param texSize Texture size.
     */
    public static void setTexSize(Vector2Di texSize) {
        assert_t(blurShader == null, "blurShader == null: use loadBlurShader");

        blurShader.setUniformFloat("xs", texSize.x());
        blurShader.setUniformFloat("ys", texSize.y());
    }

    /**
     * Set blur radius.
     * 
     * @param radius Blur radius.
     */
    public static void setRadius(float radius) {
        assert_t(blurShader == null, "blurShader == null: use loadBlurShader");

        blurShader.setUniformFloat("r", radius);

        if(radius >= 24) {
            warning("High blur radius may cause performance issues (%f >= 24).".formatted(radius));
        }
    }

    /**
     * Get blur shader.
     */
    public static VShader getShader() {
        return blurShader;
    }

    /**
     * Begin blur shader.
     */
    public static void begin() {
        assert_t(blurShader == null, "blurShader == null: use loadBlurShader");

        blurShader.begin();
    }

    /**
     * End blur shader.
     */
    public static void end() {
        assert_t(blurShader == null, "blurShader == null: use loadBlurShader");

        blurShader.end();
    }

    /**
     * Unload blur shader.
     */
    public static void unload() {
        assert_t(blurShader == null, "blurShader == null: use loadBlurShader");

        blurShader.unload();
    }
}
