package vsdk.source;

import com.raylib.Raylib;

import static com.raylib.Jaylib.BLANK;

import org.bytedeco.javacpp.FloatPointer;

import static vsdk.source.Assert.assert_t;
import static vsdk.source.Assert.assert_f;

import static vsdk.r_utilities.PathResolver.resolvePath;

/**
 * 2D Vignette.
 */
public class Vignette2D {
    private Raylib.Shader vShader;

    private Raylib.RenderTexture vRenderTex;

    private final int vRadiusLoc;
    private final int vBlurLoc;
    private final int vColLoc;

    private final int vPosXLoc;
    private final int vPosYLoc;

    private float vRadius;
    private float vBlur;
    private Vector3Df vCol;

    private float vPosX;
    private float vPosY;

    /**
     * Initialize 2D Vignette.
     *
     * @param vRadius_ Vignette radius.
     * @param vBlur_ Vignette center blur.
     * @param vCol_ Vignette color.
     * @param vPosX_ Vignette position X.
     * @param vPosY_ Vignette position Y.
     */
    public Vignette2D(float vRadius_, float vBlur_, Vector3Df vCol_, float vPosX_, float vPosY_) {
        if(vBlur_ < 0.0f || vBlur_ > 2.0f) {
            vBlur_ = Math.min(0.0f, Math.max(2.0f, vBlur_));

            VLogger.warning("Invalid vignette blur value: clamped it between 0.0f and 2.0f.");
        }

        vRadius = vRadius_;
        vBlur = vBlur_;
        vCol = vCol_;

        vPosX = vPosX_;
        vPosY = vPosY_;

        vShader = Raylib.LoadShader(null, resolvePath("vsdk/shaders/vignette2d.fs"));

        assert_f(Raylib.IsShaderReady(vShader), "vShader != valid");

        vRenderTex = Raylib.LoadRenderTexture(Raylib.GetScreenWidth(), Raylib.GetScreenHeight());

        assert_f(Raylib.IsRenderTextureReady(vRenderTex), "vRenderTex != valid");

        vRadiusLoc = Raylib.GetShaderLocation(vShader, "vRadius"); assert_t(vRadiusLoc == -1, "vRadiusLoc == -1");
        vBlurLoc = Raylib.GetShaderLocation(vShader, "vBlur"); assert_t(vBlurLoc == -1, "vBlurLoc == -1");
        vColLoc = Raylib.GetShaderLocation(vShader, "vColor"); assert_t(vColLoc == -1, "vColLoc == -1");

        vPosXLoc = Raylib.GetShaderLocation(vShader, "vPosX"); assert_t(vPosXLoc == -1, "vPosX == -1");
        vPosYLoc = Raylib.GetShaderLocation(vShader, "vPosY"); assert_t(vPosYLoc == -1, "vPosY == -1");

        Raylib.SetShaderValue(vShader, vRadiusLoc, new FloatPointer(vRadius), Raylib.SHADER_UNIFORM_FLOAT);
        Raylib.SetShaderValue(vShader, vBlurLoc, new FloatPointer(vBlur), Raylib.SHADER_UNIFORM_FLOAT);
        Raylib.SetShaderValue(vShader, vColLoc, new FloatPointer(vCol.toArray()), Raylib.SHADER_UNIFORM_VEC3);

        Raylib.SetShaderValue(vShader, vPosXLoc, new FloatPointer(vPosX), Raylib.SHADER_UNIFORM_FLOAT);
        Raylib.SetShaderValue(vShader, vPosYLoc, new FloatPointer(vPosY), Raylib.SHADER_UNIFORM_FLOAT);
    }

    /**
     * Render vignette.
     */
    public void renderVignette() {
        Raylib.BeginShaderMode(vShader);

        Raylib.DrawTextureRec(vRenderTex.texture(), new Raylib.Rectangle()
                .x(0).y(0)
                .width(vRenderTex.texture().width()).height(vRenderTex.texture().height()),
                new Raylib.Vector2().x(0).y(0), BLANK);

        Raylib.EndShaderMode();
    }

    /**
     * Set vignette radius.
     *
     * @param radius Radius.
     */
    public void setVRadius(float radius) {
        assert_t(vRadiusLoc == -1, "vRadiusLoc == -1");

        vRadius = radius;

        Raylib.SetShaderValue(vShader, vRadiusLoc, new FloatPointer(radius), Raylib.SHADER_UNIFORM_FLOAT);
    }

    /**
     * Get vignette radius.
     */
    public float getVRadius() {
        return vRadius;
    }

    /**
     * Set vignette blur.
     *
     * @param blur Blur.
     */
    public void setVBlur(float blur) {
        assert_t(vBlurLoc == -1, "vBlurLoc == -1");

        if(blur < 0.0f || blur > 2.0f) {
            blur = Math.min(0.0f, Math.max(2.0f, blur));

            VLogger.warning("Invalid vignette blur value: clamped it between 0.0f and 2.0f.");
        }

        vBlur = blur;

        Raylib.SetShaderValue(vShader, vBlurLoc, new FloatPointer(blur), Raylib.SHADER_UNIFORM_FLOAT);
    }

    /**
     * Get vignette blur.
     */
    public float getVBlur() {
        return vBlur;
    }

    /**
     * Set vignette color.
     *
     * @param color Color.
     */
    public void setVColor(Vector3Df color) {
        assert_t(vColLoc == -1, "vColLoc == -1");

        vCol = color;

        Raylib.SetShaderValue(vShader, vColLoc, new FloatPointer(color.toArray()), Raylib.SHADER_UNIFORM_VEC3);
    }

    /**
     * Get vignette color.
     */
    public Vector3Df getVColor() {
        return vCol;
    }

    /**
     * Set vignette position X.
     *
     * @param x X.
     */
    public void setVPosX(float x) {
        assert_t(vPosXLoc == -1, "vPosXLoc == -1");

        vPosX = x;

        Raylib.SetShaderValue(vShader, vPosXLoc, new FloatPointer(x), Raylib.SHADER_UNIFORM_FLOAT);
    }

    /**
     * Get vignette position x.
     */
    public float getVPosX() {
        return vPosX;
    }

    /**
     * Set vignette position Y.
     *
     * @param y Y.
     */
    public void setVPosY(float y) {
        assert_t(vPosYLoc == -1, "vPosYLoc == -1");

        vPosY = y;

        Raylib.SetShaderValue(vShader, vPosYLoc, new FloatPointer(y), Raylib.SHADER_UNIFORM_FLOAT);
    }

    /**
     * Get vignette position Y.
     */
    public float getVPosY() {
        return vPosY;
    }

    /**
     * Unload vignette.
     */
    public void unloadVignette() {
        Raylib.UnloadShader(vShader);
        Raylib.UnloadRenderTexture(vRenderTex);
    }
}
