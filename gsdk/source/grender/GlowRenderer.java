package gsdk.source.grender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import java.util.ArrayList;

import static gsdk.source.generic.Range.inRange;

import static gsdk.source.generic.Assert.assert_f;

/**
 * Class for rendering 2D glow objects.
 */
public class GlowRenderer {
    private final ArrayList<BakedGlow> bakedGlowList;

    private boolean renderEnabled;

    /**
     * Initialize glow renderer.
     * 
     * @param bakedGlowObjs Baked glow objects.
     */
    public GlowRenderer(BakedGlow ...bakedGlowObjs) {
        bakedGlowList = new ArrayList<>();

        appendGlowTex(bakedGlowObjs);

        renderEnabled = true;
    }

    /**
     * Append baked glow textures.
     * 
     * @param bakedGlowObjs Baked glow objects.
     */
    public void appendGlowTex(BakedGlow ...bakedGlowObjs) {
        for(BakedGlow bakedGlow : bakedGlowObjs) bakedGlowList.add(bakedGlow);
    }

    /**
     * Get amount of registered baked glow textures.
     */
    public int getBakedGlowTexAmount() {
        return bakedGlowList.size();
    }

    /**
     * Clear all baked glow textures.
     */
    public void clearBakedGlowTexList() {
        bakedGlowList.clear();
    }

    /**
     * Enable objects rendering.
     */
    public void enableRendering() {
        renderEnabled = true;
    }

    /**
     * Disable objects rendering.
     */
    public void disableRendering() {
        renderEnabled = false;
    }

    /**
     * Enable rendering if rendering is disabled and vice-versa.
     */
    public void invertRenderingState() {
        if(renderEnabled) disableRendering();
        else enableRendering();
    }

    /**
     * Get baked glow.

     * @param pos Position.
     */
    public BakedGlow getBakedGlow(int pos) {
        assert_f(inRange(pos, 1, getBakedGlowTexAmount()), "invalid baked glow id/pos");

        return bakedGlowList.get(pos - 1);
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
        if(!renderEnabled) return;

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

    /**
     * Unload all baked glow textures.
     */
    public void unload() {
        for(BakedGlow glow : bakedGlowList) {
            Raylib.UnloadTexture(glow.getBakedGlow());
        }
    }
}
