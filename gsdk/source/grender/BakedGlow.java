package gsdk.source.grender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import static com.raylib.Jaylib.BLANK;

import gsdk.source.vectors.Vector2Di;

import gsdk.source.shaders.BlurShader;

import static gsdk.source.generic.GMath.clamp;
import static gsdk.source.generic.GMath.scale;

import static gsdk.source.generic.Assert.assert_f;

/**
 * Class for baking 2D glow texture/image.
 */
public class BakedGlow {
    private static final float WEAK_BLUR_VAL = 6.0f, MID_BLUR_VAL = 12.5f, HARD_BLUR_VAL = 16.5f, INTENSE_BLUR_VAL = 21.0f, TRANSCENDENT_BLUR_VAL = 25.0f;

    private Raylib.Texture bakedGlow;

    /**
     * Bake new glow texture.
     * 
     * @param gShape Glow shape.
     * @param gSize Glow size.
     * @param gIntensity Glow intensity (0.0f -> 1.0f).
     * @param gBlur Glow blur level.
     * @param gColor Glow color.
     */
    public BakedGlow(GlowShape gShape, Vector2Di gSize, float gIntensity, GlowBlur gBlur, Raylib.Color gColor) {
        bakeGlow(gShape, gSize, gIntensity, gBlur, gColor);
    }

    /**
     * Bake glow texture.
     * 
     * @param gShape Glow shape.
     * @param gSize Glow size.
     * @param gIntensity Glow intensity (0.0f -> 1.0f).
     * @param gBlur Glow blur level.
     * @param gColor Glow color.
     */
    public Raylib.Texture bakeGlow(GlowShape gShape, Vector2Di gSize, float gIntensity, GlowBlur gBlur, Raylib.Color gColor) {
        float blurRadius = 0;

        if(gBlur.equals(GlowBlur.WEAK)) blurRadius = WEAK_BLUR_VAL;
        else if(gBlur.equals(GlowBlur.MID)) blurRadius = MID_BLUR_VAL;
        else if(gBlur.equals(GlowBlur.HARD)) blurRadius = HARD_BLUR_VAL;
        else if(gBlur.equals(GlowBlur.INTENSE)) blurRadius = INTENSE_BLUR_VAL;
        else if(gBlur.equals(GlowBlur.TRANSCENDENT)) blurRadius = TRANSCENDENT_BLUR_VAL;

        Vector2Di preTexSize = null;

        float preRadius = -1;

        assert_f(BlurShader.shaderLoaded(), "can't bake glow texture: blur shader is not loaded (BlurShader::loadBlurShader)");

        preTexSize = BlurShader.getCurrTexSize();

        preRadius = BlurShader.getCurrBlurRadius();

        BlurShader.setTexSize(gSize);

        BlurShader.setRadius(blurRadius);

        gColor.a((byte) clamp(0, 255, scale(gIntensity, 255, 1.0)));

        Raylib.RenderTexture glowShapeRTex = Raylib.LoadRenderTexture(gSize.x() + 5, gSize.y() + 5);

        Raylib.BeginTextureMode(glowShapeRTex);

        Raylib.ClearBackground(BLANK);

        if(gShape.equals(GlowShape.RECTANGLE)) {
            Raylib.DrawRectangle(5, 5, gSize.x(), gSize.y(), gColor);
        } else if(gShape.equals(GlowShape.ELLIPSE)) {
            Raylib.DrawEllipse(gSize.x() / 2, 5 + gSize.y() / 2, gSize.x() / 2, gSize.y() / 2, gColor);
        } else if(gShape.equals(GlowShape.SPOTLIGHT)) {
            spotlight(gSize, gIntensity, gColor, 1);
        } else if(gShape.name().startsWith("NARROWX")) {
            spotlight(gSize, gIntensity, gColor, Integer.parseInt(gShape.name().replace("NARROWX", "").replace("_SPOTLIGHT", "")));
        }

        Raylib.EndTextureMode();

        if(blurRadius != 0) {
            Raylib.RenderTexture glowBlurredRTex = Raylib.LoadRenderTexture(gSize.x(), gSize.y());

            Raylib.BeginTextureMode(glowBlurredRTex);

            BlurShader.begin();

            Raylib.DrawTexture(glowShapeRTex.texture(), 0, 0, WHITE);

            BlurShader.end();

            Raylib.EndTextureMode();

            if(preTexSize != null) BlurShader.setTexSize(preTexSize);
            if(preRadius != -1) BlurShader.setRadius(preRadius);

            bakedGlow = Raylib.LoadTextureFromImage(Raylib.LoadImageFromTexture(glowBlurredRTex.texture())); // Trick for resource managment (copy texture).

            Raylib.UnloadRenderTexture(glowBlurredRTex);

            return bakedGlow;
        } else {
            Raylib.Image rotatedTex = Raylib.LoadImageFromTexture(glowShapeRTex.texture());

            Raylib.ImageRotate(rotatedTex, 180);

            bakedGlow = Raylib.LoadTextureFromImage(rotatedTex);

            Raylib.UnloadRenderTexture(glowShapeRTex);

            return bakedGlow;
        }
    }

    /**
     * Resize baked glow texture.
     * 
     * @param width New width.
     * @param height New height.
     */
    public void resizeGlow(int width, int height) {
        Raylib.Image resized = getBakedGlowImage();

        Raylib.ImageResize(resized, width, height);

        bakedGlow = Raylib.LoadTextureFromImage(resized);
    }

    /**
     * Rotate baked glow texture.
     * 
     * @param degrees Angle.
     */
    public void rotateGlow(int degrees) {
        Raylib.Image rotated = getBakedGlowImage();

        Raylib.ImageRotate(rotated, degrees);

        bakedGlow = Raylib.LoadTextureFromImage(rotated);
    }

    /**
     * Get baked glow.
     */
    public Raylib.Texture getBakedGlow() {
        return bakedGlow;
    }

    /**
     * Get baked glow as image.
     */
    public Raylib.Image getBakedGlowImage() {
        return Raylib.LoadImageFromTexture(bakedGlow);
    }

    private static void spotlight(Vector2Di size, float intensity, Raylib.Color color, int narrowness) {
        int baseWidth = size.x() / narrowness;
        int topWidth = size.x();

        int height = size.y();

        for(int y = 0; y < height; y++) {
            int width = baseWidth + (int) ((topWidth - baseWidth) * ((float) y / height));
    
            Raylib.DrawRectangle(
                size.x() / 2 - width / 2,
                5 + y, width, 1,

                new Raylib.Color()
                    .r(color.r())
                    .g(color.g())
                    .b(color.b())
                    .a((byte) clamp(0, 255, scale(intensity * (height - y) / height, 255, 1.0)))
            );
        }
    }

    /**
     * Glow shapes.
     */
    public static enum GlowShape {
        /**
         * Rectangle shape.
         */
        RECTANGLE,

        /**
         * Ellipse shape.
         */
        ELLIPSE,

        /**
         * Spotlight shape.
         */
        SPOTLIGHT,

        /**
         * Spotlight shape with second level of narrowness.
         */
        NARROWX2_SPOTLIGHT,

        /**
         * Spotlight shape with fourth level of narrowness.
         */
        NARROWX4_SPOTLIGHT,

        /**
         * Spotlight shape with sixth level of narrowness.
         */
        NARROWX6_SPOTLIGHT,

        /**
         * Spotlight shape with eighth level of narrowness.
         */
        NARROWX8_SPOTLIGHT,

        /**
         * Spotlight shape with tenth level of narrowness.
         */
        NARROWX10_SPOTLIGHT,

        /**
         * Spotlight shape with twelveth level of narrowness.
         */
        NARROWX12_SPOTLIGHT,

        /**
         * Spotlight shape with fourteenth level of narrowness.
         */
        NARROWX14_SPOTLIGHT,

        /**
         * Spotlight shape with sixteenth level of narrowness.
         */
        NARROWX16_SPOTLIGHT,
    }

    /**
     * Glow blur levels.
     */
    public static enum GlowBlur {
        /**
         * No glow blur.
         */
        NONE,

        /**
         * Weak glow blur.
         */
        WEAK,

        /**
         * Middle glow blur.
         */
        MID,

        /**
         * Hard glow blur.
         */
        HARD,

        /**
         * Intense glow blur.
         */
        INTENSE,

        /**
         * Transcendent glow blur.
         */
        TRANSCENDENT,
    }
}
