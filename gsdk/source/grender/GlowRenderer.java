package gsdk.source.grender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

/**
 * Class for rendering 2D glow objects.
 */
public class GlowRenderer {
    private final BakedGlow[] bakedGlowArr;

    /**
     * Initialize glow renderer.
     * 
     * @param bakedGlowObjs Baked glow objects.
     */
    public GlowRenderer(BakedGlow ...bakedGlowObjs) {
        bakedGlowArr = new BakedGlow[bakedGlowObjs.length];

        System.arraycopy(bakedGlowObjs, 0, bakedGlowArr, 0, bakedGlowObjs.length);
    }

    /**
     * Get baked glow.

     * @param pos Position.
     */
    public BakedGlow getBakedGlow(int pos) {
        return bakedGlowArr[pos - 1];
    }

    /**
     * Render glow.
     * 
     * @param pos Glow position.
     * @param x Glow X.
     * @param y Glow Y.
     * @param tint Glow tint.
     * @param blendMode Glow blend mode.
     */
    public void renderGlow(int pos, int x, int y, Raylib.Color tint, int blendMode) {
        if(blendMode != 0) Raylib.BeginBlendMode(blendMode);    

        Raylib.DrawTexture(getBakedGlow(pos).getBakedGlow(), x, y, tint);

        if(blendMode != 0) Raylib.EndBlendMode();
    }

    /**
     * Render glow with additive blend mode.
     * 
     * @param pos Glow position.
     * @param x Glow X.
     * @param y Glow Y.
     * @param tint Glow tint.
     */
    public void renderGlow(int pos, int x, int y, Raylib.Color tint) {
        renderGlow(pos, x, y, tint, Raylib.BLEND_ADDITIVE);
    }

    /**
     * Render glow with additive blend mode and white tint.
     * 
     * @param pos Glow position.
     * @param x Glow X.
     * @param y Glow Y.
     */
    public void renderGlow(int pos, int x, int y) {
        renderGlow(pos, x, y, WHITE);
    }
}
