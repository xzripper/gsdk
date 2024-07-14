package vsdk.source.utils;

import com.raylib.Raylib;

import static com.raylib.Jaylib.BLACK;

import vsdk.sdk_vendor.org.apfelstrudel.tech.dynstatj.DynStat;
import vsdk.sdk_vendor.org.apfelstrudel.tech.dynstatj.DynStatEffect;

/**
 * Utility for screen fading.
 */
public class ScreenFade {
    private final DynStat alpha;

    private final DynStatEffect fadeInEffect;
    private final DynStatEffect fadeOutEffect;

    private Raylib.Color color;

    /**
     * Initialize screen fade utility.
     *
     * @param fadeSpeed Fade speed.
     * @param color_ Fade color.
     */
    public ScreenFade(float fadeSpeed, Raylib.Color color_) {
        alpha = new DynStat(0, 255, 0);

        fadeInEffect = new DynStatEffect((stat, sNMod) -> stat + DynStat.applyMod(fadeSpeed, sNMod), 0.1f, false);
        fadeOutEffect = new DynStatEffect((stat, sNMod) -> stat - DynStat.applyMod(fadeSpeed, sNMod), 0.1f, false);

        alpha.addEffect(fadeInEffect);
        alpha.addEffect(fadeOutEffect);

        color = color_;
    }

    /**
     * Initialize screen fade utility with black fade color.
     *
     * @param fadeSpeed Fade speed.
     */
    public ScreenFade(float fadeSpeed) {
        alpha = new DynStat(0, 255, 0);

        fadeInEffect = new DynStatEffect((stat, sNMod) -> stat + DynStat.applyMod(fadeSpeed, sNMod), 0.1f, false);
        fadeOutEffect = new DynStatEffect((stat, sNMod) -> stat - DynStat.applyMod(fadeSpeed, sNMod), 0.1f, false);

        alpha.addEffect(fadeInEffect);
        alpha.addEffect(fadeOutEffect);

        color = BLACK;
    }

    /**
     * Set fade color.
     *
     * @param color_ Color.
     */
    public void setColor(Raylib.Color color_) {
        color = color_;
    }

    /**
     * Get fade color.
     */
    public Raylib.Color getColor() {
        return color;
    }

    /**
     * Stop fading out and start fading in.
     */
    public void fadeIn() {
        alpha.effectDisable(DynStat.LATEST_EFFECT);

        alpha.effectEnable(DynStat.FIRST_EFFECT);
    }

    /**
     * Stop fading in and start fading out.
     */
    public void fadeOut() {
        alpha.effectDisable(DynStat.FIRST_EFFECT);

        alpha.effectEnable(DynStat.LATEST_EFFECT);
    }

    /**
     * Process and draw fade.
     */
    public void processNDraw() {
        alpha.proc();

        if(alpha.getStat() >= 255) alpha.effectDisable(DynStat.FIRST_EFFECT);
        else if(alpha.getStat() <= 0) alpha.effectDisable(DynStat.LATEST_EFFECT);

        color.a((byte) alpha.getStatRndf());

        Raylib.DrawCircle(Raylib.GetScreenWidth() / 2, Raylib.GetScreenHeight() / 2, Raylib.GetScreenWidth() + Raylib.GetScreenHeight(), color);
    }
}
