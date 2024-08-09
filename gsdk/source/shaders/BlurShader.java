package gsdk.source.shaders;

import gsdk.source.vectors.Vector2Di;

import gsdk.source.generic.GShader;

import static gsdk.source.generic.GLogger.warning;

import static gsdk.r_utilities.PathResolver.resolvePath;

import static gsdk.source.generic.Assert.assert_t;

/**
 * Utility class for using blur shader.
 */
public class BlurShader {
    private static final String BLUR_SHADER_VERT_PATH = resolvePath("gsdk/shaders/blur.vs");
    private static final String BLUR1_SHADER_FRAG_PATH = resolvePath("gsdk/shaders/blur.fs");
    private static final String BLUR2_SHADER_FRAG_PATH = resolvePath("gsdk/shaders/blur2.fs");

    private static Vector2Di texSizeShader;
    private static float blurRadiusShader;

    private static GShader blurShader;

    public static final int BLUR_1 = 1, BLUR_2 = 2;

    /**
     * Is blur shader loaded.
     */
    public static boolean shaderLoaded() {
        return blurShader != null;
    }

    /**
     * Load blur shader and set texture size and blur radius.
     * 
     * @param blurVar Blur variant.
     * @param texSize Texture size.
     * @param radius Blur radius.
     */
    public static void loadBlurShader(int blurVar, Vector2Di texSize, float radius) {
        assert_t(blurVar != BLUR_1 && blurVar != BLUR_2, "invalid blur variant (expected 1 or 2)");

        String blurFragment = blurVar == BLUR_1 ? BLUR1_SHADER_FRAG_PATH : BLUR2_SHADER_FRAG_PATH;

        blurShader = new GShader(BLUR_SHADER_VERT_PATH, blurFragment, GShader.FILE);

        blurShader.setUniformFloat("xs", texSize.x());
        blurShader.setUniformFloat("ys", texSize.y());

        blurShader.setUniformFloat("r", radius);

        texSizeShader = texSize;
        blurRadiusShader = radius;

        if(radius >= 24) {
            warning("High blur radius may cause performance issues (%f >= 24).".formatted(radius));
        }
    }

    /**
     * Load blur shader.
     * 
     * @param blurVar Blur variant.
     */
    public static void loadBlurShader(int blurVar) {
        assert_t(blurVar != BLUR_1 && blurVar != BLUR_2, "invalid blur variant (expected 1 or 2)");

        String blurFragment = blurVar == BLUR_1 ? BLUR1_SHADER_FRAG_PATH : BLUR2_SHADER_FRAG_PATH;

        blurShader = new GShader(BLUR_SHADER_VERT_PATH, blurFragment, GShader.FILE);
    }

    /**
     * Set texture size (shader).
     * 
     * @param texSize Texture size.
     */
    public static void setTexSize(Vector2Di texSize) {
        assert_t(!shaderLoaded(), "blurShader == null: use loadBlurShader");

        blurShader.setUniformFloat("xs", texSize.x());
        blurShader.setUniformFloat("ys", texSize.y());

        texSizeShader = texSize;
    }

    /**
     * Set blur radius.
     * 
     * @param radius Blur radius.
     */
    public static void setRadius(float radius) {
        assert_t(!shaderLoaded(), "blurShader == null: use loadBlurShader");

        blurShader.setUniformFloat("r", radius);

        blurRadiusShader = radius;

        if(radius >= 24) {
            warning("High blur radius may cause performance issues (%f >= 24).".formatted(radius));
        }
    }

    /**
     * Get current set texture size.
     */
    public static Vector2Di getCurrTexSize() {
        return texSizeShader;
    }

    /**
     * Get current set blur radius.
     */
    public static float getCurrBlurRadius() {
        return blurRadiusShader;
    }

    /**
     * Get blur shader.
     */
    public static GShader getShader() {
        return blurShader;
    }

    /**
     * Begin blur shader.
     */
    public static void begin() {
        assert_t(!shaderLoaded(), "blurShader == null: use loadBlurShader");

        blurShader.begin();
    }

    /**
     * End blur shader.
     */
    public static void end() {
        assert_t(!shaderLoaded(), "blurShader == null: use loadBlurShader");

        blurShader.end();
    }

    /**
     * Unload blur shader.
     */
    public static void unload() {
        assert_t(!shaderLoaded(), "blurShader == null: use loadBlurShader");

        blurShader.unload();
    }
}
