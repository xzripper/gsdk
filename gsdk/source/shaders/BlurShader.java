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
    private static final String BLUR_SHADER_FRAG_PATH = resolvePath("gsdk/shaders/blur.fs");

    private static Vector2Di texSizeShader;
    private static float blurRadiusShader;

    private static GShader blurShader;

    /**
     * Is blur shader loaded.
     */
    public static boolean shaderLoaded() {
        return blurShader != null;
    }

    /**
     * Load blur shader.
     * 
     * @param texSize Texture size.
     * @param radius Blur radius.
     */
    public static void loadBlurShader(Vector2Di texSize, float radius) {
        blurShader = new GShader(BLUR_SHADER_VERT_PATH, BLUR_SHADER_FRAG_PATH, GShader.FILE);

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
