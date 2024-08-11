package gsdk.source.shaders;

import gsdk.source.generic.GShader;

import static gsdk.r_utilities.PathResolver.resolvePath;

import static gsdk.source.generic.Assert.assert_t;

/**
 * Utility class for using grayscale shader.
 */
public class Grayscale {
    private static final String GRAYSCALE_SHADER_FRAG_PATH = resolvePath("gsdk/shaders/grayscale.fs");

    private static GShader grayscaleShader;

    /**
     * Is grayscale shader loaded.
     */
    public static boolean shaderLoaded() {
        return grayscaleShader != null;
    }

    /**
     * Load grayscale shader.
     */
    public static void loadGrayscaleShader() {
        grayscaleShader = new GShader(null, GRAYSCALE_SHADER_FRAG_PATH, GShader.FILE);
    }

    /**
     * Get grayscale shader.
     */
    public static GShader getShader() {
        return grayscaleShader;
    }

    /**
     * Begin grayscale shader.
     */
    public static void begin() {
        assert_t(!shaderLoaded(), "grayscaleShader == null: use loadGrayscaleShader");

        grayscaleShader.begin();
    }

    /**
     * End grayscale shader.
     */
    public static void end() {
        assert_t(!shaderLoaded(), "grayscaleShader == null: use loadGrayscaleShader");

        grayscaleShader.end();
    }

    /**
     * Unload grayscale shader.
     */
    public static void unload() {
        assert_t(!shaderLoaded(), "grayscaleShader == null: use loadGrayscaleShader");

        grayscaleShader.unload();
    }
}
