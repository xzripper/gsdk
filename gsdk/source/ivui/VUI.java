package vsdk.source.ivui;

import com.raylib.Raylib;

import java.util.ArrayList;

import org.bytedeco.javacpp.FloatPointer;

import vsdk.source.vrender.Texture;

import vsdk.source.utils.VShader;

import static vsdk.source.utils.Range.inRange;

import static vsdk.source.utils.VMath.clamp;
import static vsdk.source.utils.VMath.scale;

import static vsdk.r_utilities.PathResolver.resolvePath;

import static vsdk.source.utils.Assert.assert_t;

/**
 * Main Violent User Interface Class.
 */
public class VUI {
    private static final int RADIO_BUTTON_WIDTH = 5;
    private static final int RADIO_BUTTON_HEIGHT = 5;

    private static final int RADIO_BUTTON_RADIUS = RADIO_BUTTON_WIDTH + RADIO_BUTTON_HEIGHT;

    private static final int CHECKBOX_WIDTH = 20;
    private static final int CHECKBOX_HEIGHT = 20;

    private static final int DEFAULT_PROGRESS_BAR_WIDTH = 125;
    private static final int DEFAULT_PROGRESS_BAR_HEIGHT = 25;

    private static final int DEFAULT_SLIDER_WIDTH = 125;
    private static final int DEFAULT_SLIDER_HEIGHT = 25;

    private static final int DEFAULT_SLIDER_BUTTON_WIDTH = 15;
    private static final int DEFAULT_SLIDER_BUTTON_HEIGHT = 25;

    private static VUIStyle finalStyle;

    private static VUIStyle strSliderButtonStyle;

    private static float strSliderLeftButtonWidth;

    private static Raylib.Shader texelBleedingFixShader = null;

    private static VShader loadingIconShader = null;

    private static float prevLoadingIconProgress, loadingIconTint;

    public static final int TOP = 1, VERTICAL = 2, PIE = 3;

    /**
     * Load texel bleeding fix shader.
     */
    public static void loadTexelBleedingFixShader() {
        texelBleedingFixShader = Raylib.LoadShader(null, resolvePath("vsdk/shaders/texel_bleeding_fix2d.fs"));
    }

    /**
     * Unload texel bleeding fix shader.
     */
    public static void unloadTexelBleedingFixShader() {
        Raylib.UnloadShader(texelBleedingFixShader);
    }

    /**
     * Is texture bleeding fix shader loaded?
     */
    public static boolean texelBleedingFixAvailable() {
        return texelBleedingFixShader != null;
    }

    /**
     * Create new VUI context.
     *
     * @param style Style.
     */
    public static void newVuiCtx(VUIStyle style) {
        VUIIO.newCtx(style);

        loadingIconShader = new VShader(null, resolvePath("vsdk/shaders/loading_icon2d.fs"), VShader.FILE);

        setLoadingIconMode(TOP);

        setLoadingIconTint(0.3f);

        finalStyle = VUIIO.style;

        VUIColor focused = finalStyle.getFocusedCol();
        VUIColor pressed = finalStyle.getPressedCol();

        strSliderButtonStyle = new VUIStyle(
            new VUIColor(0, 0, 0, 0), new VUIColor(focused.get('r'), focused.get('g'), focused.get('b'), focused.get('a') / 2),
            new VUIColor(pressed.get('r'), pressed.get('g'), pressed.get('b'), pressed.get('a') / 2),
            new VUIColor(0, 0, 0, 148), 0, 0, null, 20, 0.1f, finalStyle.getTextFont(),
            VUIStyle.TEXT_ANCHOR_CENTER, new VUIColor(255, 255, 255, 255)
        );

        strSliderLeftButtonWidth = Raylib.MeasureTextEx(finalStyle.getTextFont().getFont(), "<", 20.0f, 0.1f).x();
    }

    /**
     * Set loading icon tint (0.0->1.0).
     *
     * @param tint Tint.
     */
    public static void setLoadingIconTint(float tint) {
        loadingIconTint = (float) clamp(0.0, 1.0, tint);

        loadingIconShader.setUniformFloat("tint", loadingIconTint);
    }

    /**
     * Get loading icon tint.
     */
    public static float getLoadingIconTint() {
        return loadingIconTint;
    }

    /**
     * Set loading icon mode.
     * 
     * @param mode Mode (Top/Vertical/Pie).
     */
    public static void setLoadingIconMode(int mode) {
        loadingIconShader.rmFragPreProcDefs();

        loadingIconShader.preProcDefsFrag(330, mode == TOP ? "top" : (mode == VERTICAL ? "vertical" : "pie"));

        loadingIconShader.setUniformFloat("tint", loadingIconTint);
    }

    /**
     * Unload resources (shaders) required by IVUI.
     */
    public static void unloadResources() {
        if(texelBleedingFixAvailable()) unloadTexelBleedingFixShader();

        loadingIconShader.unload();
    }

    /**
     * Disable next objects.
     */
    public static void disableNext() {
        VUIIO.beginDisabled();
    }

    /**
     * Do not disable next objects.
     */
    public static void enableNext() {
        VUIIO.endDisabled();
    }

    /**
     * Draw next objects with specified style.
     *
     * @param style Style.
     */
    public static void beginStyle(VUIStyle style) {
        VUIIO.style = style;
    }

    /**
     * Stop drawing next objects with specified style.
     */
    public static void endStyle() {
        VUIIO.style = finalStyle;
    }

    /**
     * Draw next objects with specified alpha.
     *
     * @param alpha Alpha (opacity 0->255).
     */
    public static void beginAlpha(int alpha) {
        VUIIO.style.getDefaultCol().set('a', alpha);
        VUIIO.style.getFocusedCol().set('a', alpha);
        VUIIO.style.getPressedCol().set('a', alpha);

        VUIIO.style.getBorderColor().set('a', alpha);

        VUIIO.style.getTextCol().set('a', alpha);
    }

    /**
     * Restore style alpha.
     */
    public static void endAlpha() {
        VUIIO.style.getDefaultCol().set('a', finalStyle.getDefaultCol().get('a'));
        VUIIO.style.getFocusedCol().set('a', finalStyle.getFocusedCol().get('a'));
        VUIIO.style.getPressedCol().set('a', finalStyle.getPressedCol().get('a'));

        VUIIO.style.getBorderColor().set('a', finalStyle.getBorderColor().get('a'));

        VUIIO.style.getTextCol().set('a', finalStyle.getTextCol().get('a'));
    }

    /**
     * Draw simple rectangle.
     *
     * @param x X Position.
     * @param y Y Position.
     * @param w Rectangle width.
     * @param h Rectangle Height.
     * @param color Rectangle color.
     * @param retHoverEv Return true if rectangle hovered?
     * @return Is rectangle clicked or hovered.
     */
    public static boolean rectangle(int x, int y, int w, int h, VUIColor color, boolean retHoverEv) {
        Raylib.DrawRectangle(x, y, w, h, color.toRlCol());

        return retHoverEv ? (!VUIIO.disabled && VUIIO.mouseHovers(x, y, w, h)) : !VUIIO.disabled && VUIIO.mouseHovers(x, y, w, h) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
    }

    /**
     * Draw simple rectangle (no hover event).
     *
     * @param x X Position.
     * @param y Y Position.
     * @param w Rectangle width.
     * @param h Rectangle Height.
     * @param color Rectangle color.
     * @return Is rectangle clicked.
     */
    public static boolean rectangle(int x, int y, int w, int h, VUIColor color) {
        return rectangle(x, y, w, h, color, false);
    }

    /**
     * Draw simple rectangle (default color + no hover event).
     *
     * @param x X Position.
     * @param y Y Position.
     * @param w Rectangle width.
     * @param h Rectangle Height.
     * @return Is rectangle clicked.
     */
    public static boolean rectangle(int x, int y, int w, int h) {
        return rectangle(x, y, w, h, VUIIO.style.getDefaultCol(), false);
    }

    /**
     * Draw hollow rectangle (RectangleLines).
     *
     * @param x X Position.
     * @param y Y Position.
     * @param w Rectangle Width.
     * @param h Rectangle Height.
     * @param retHoverEv Return true if rectangle hovered?
     * @return Is rectangle (hollow part also) clicked or hovered.
     */
    public static boolean hollowRectangle(int x, int y, int w, int h, boolean retHoverEv) {
        if(VUIIO.style.getBorderThickness() > 0) {
            if(VUIIO.style.getBorderRounding() > 0) {
                Raylib.DrawRectangleRoundedLines(new Raylib.Rectangle().x(x).y(y).width(w).height(h),
                    VUIIO.style.getBorderRounding(), 16,
                    VUIIO.style.getBorderThickness(),
                    VUIIO.style.getBorderColor().toRlCol()
                );
            } else {
                Raylib.DrawRectangleLinesEx(new Raylib.Rectangle().x(x).y(y).width(w).height(h),
                    VUIIO.style.getBorderThickness(),
                    VUIIO.style.getBorderColor().toRlCol()
                );
            }
        }

        return retHoverEv ? (!VUIIO.disabled && VUIIO.mouseHovers(x, y, w, h)) : !VUIIO.disabled && VUIIO.mouseHovers(x, y, w, h) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
    }

    /**
     * Draw hollow rectangle (RectangleLines) (No hover event).
     *
     * @param x X Position.
     * @param y Y Position.
     * @param w Rectangle Width.
     * @param h Rectangle Height.
     * @return Is rectangle (hollow part also) clicked.
     */
    public static boolean hollowRectangle(int x, int y, int w, int h) {
        return hollowRectangle(x, y, w, h, false);
    }

    /**
     * Draw regular text.
     *
     * @param content Text content.
     * @param x X Position.
     * @param y Y Position.
     * @return Is text clicked.
     */
    public static boolean text(String content, int x, int y) {
        Raylib.DrawTextEx(
            VUIIO.style.getTextFont().getFont(),
            content, new Raylib.Vector2().x(x).y(y),
            VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing(),
            VUIIO.style.getTextCol().toRlCol()
        );

        return !VUIIO.disabled && VUIIO.mouseHoversText(content, x, y) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
    }

    /**
     * Draw regular button.
     *
     * @param content Button text.
     * @param x X Position.
     * @param y Y Position.
     * @param w Button width.
     * @param h Button height.
     * @return Is button clicked.
     */
    public static boolean button(String content, int x, int y, int w, int h) {
        Raylib.Vector2 textSize;

        float buttonWidth, buttonHeight;

        textSize = Raylib.MeasureTextEx(VUIIO.style.getTextFont().getFont(),
            content, VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing());

        if(w == -1 && h == -1) {
            buttonWidth = textSize.x() + 10;
            buttonHeight = textSize.y() + 5;
        } else {
            buttonWidth = w;
            buttonHeight = h;
        }

        VUIColor buttonColor = VUIIO.style.getDefaultCol();

        if(!VUIIO.disabled && VUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = VUIIO.style.getPressedCol();
        } else if(!VUIIO.disabled && VUIIO.mouseHovers(x, y, buttonWidth, buttonHeight)) {
            buttonColor = VUIIO.style.getFocusedCol();
        } else if(VUIIO.disabled) {
            buttonColor = VUIIO.style.getDisabledCol();
        }

        if(VUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawRectangleRounded(
                new Raylib.Rectangle().x(x - 1).y(y - 1)
                    .width(buttonWidth + 1).height(buttonHeight + 1),
                VUIIO.style.getBorderRounding(), 16, buttonColor.toRlCol()
            );

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawRectangle(x, y, (int) buttonWidth, (int) buttonHeight, buttonColor.toRlCol());
        }

        if(VUIIO.style.getBorderThickness() > 0) {
            if(VUIIO.style.getBorderRounding() > 0) {
                Raylib.DrawRectangleRoundedLines(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    VUIIO.style.getBorderRounding(), 16,
                    VUIIO.style.getBorderThickness(), VUIIO.style.getBorderColor().toRlCol()
                );
            } else {
                Raylib.DrawRectangleLinesEx(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    VUIIO.style.getBorderThickness(),
                    VUIIO.style.getBorderColor().toRlCol()
                );
            }
        }

        Raylib.Vector2 textPos = new Raylib.Vector2().y(y + (buttonHeight - textSize.y()) / 2);

        if(w != -1 && h != -1) {
            int textAnchor = VUIIO.style.getTextAnchor();

            if(textAnchor == VUIStyle.TEXT_ANCHOR_RIGHT) {
                textPos.x(x + buttonWidth - textSize.x() - 5);
            } else if(textAnchor == VUIStyle.TEXT_ANCHOR_CENTER) {
                textPos.x(x + (buttonWidth - textSize.x()) / 2.0f);
            } else if(textAnchor == VUIStyle.TEXT_ANCHOR_LEFT) {
                textPos.x(x + 5);
            }
        } else {
            textPos.x(x + buttonWidth - 5 - textSize.x());
        }

        Raylib.DrawTextEx(
            VUIIO.style.getTextFont().getFont(), content,
            textPos, VUIIO.style.getTextSize(),
            VUIIO.style.getTextSpacing(), VUIIO.style.getTextCol().toRlCol()
        );

        return !VUIIO.disabled && VUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
    }

    /**
     * Draw regular button (without size specifies).
     *
     * @param content Button content.
     * @param x X Position.
     * @param y Y Position.
     * @return Is button clicked.
     */
    public static boolean button(String content, int x, int y) {
        return button(content, x, y, -1, -1);
    }

    /**
     * Draw selectable button (based on <code>button</code>).
     *
     * @param ref Selected button reference.
     * @param content Button text.
     * @param x X Position.
     * @param y Y Position.
     * @param w Button width.
     * @param h Button height.
     * @return Is selectable button clicked.
     */
    public static boolean selectButton(VOutRef<Boolean> ref, String content, int x, int y, int w, int h) {
        if(ref.get() == null) ref.set(false);

        Raylib.Vector2 textSize;

        float buttonWidth, buttonHeight;

        textSize = Raylib.MeasureTextEx(VUIIO.style.getTextFont().getFont(),
            content, VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing());

        if(w == -1 && h == -1) {
            buttonWidth = textSize.x() + 10;
            buttonHeight = textSize.y() + 5;
        } else {
            buttonWidth = w;
            buttonHeight = h;
        }

        VUIColor buttonColor = null;

        if(!VUIIO.disabled && VUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT) || ref.get()) {
            buttonColor = VUIIO.style.getPressedCol();
        } else if(!VUIIO.disabled && VUIIO.mouseHovers(x, y, buttonWidth, buttonHeight)) {
            buttonColor = VUIIO.style.getFocusedCol();
        } else if(VUIIO.disabled) {
            buttonColor = VUIIO.style.getDisabledCol();
        } else if(!ref.get()) {
            buttonColor = VUIIO.style.getDefaultCol();
        }

        if(VUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawRectangleRounded(
                new Raylib.Rectangle().x(x - 1).y(y - 1)
                    .width(buttonWidth + 1).height(buttonHeight + 1),
                VUIIO.style.getBorderRounding(), 16, buttonColor.toRlCol()
            );

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawRectangle(x, y, (int) buttonWidth, (int) buttonHeight, buttonColor.toRlCol());
        }

        if(VUIIO.style.getBorderThickness() > 0) {
            if(VUIIO.style.getBorderRounding() > 0) {
                Raylib.DrawRectangleRoundedLines(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    VUIIO.style.getBorderRounding(), 16,
                    VUIIO.style.getBorderThickness(), VUIIO.style.getBorderColor().toRlCol()
                );
            } else {
                Raylib.DrawRectangleLinesEx(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    VUIIO.style.getBorderThickness(),
                    VUIIO.style.getBorderColor().toRlCol()
                );
            }
        }

        Raylib.Vector2 textPos = new Raylib.Vector2().y(y + (buttonHeight - textSize.y()) / 2);

        if(w != -1 && h != -1) {
            int textAnchor = VUIIO.style.getTextAnchor();

            if(textAnchor == VUIStyle.TEXT_ANCHOR_RIGHT) {
                textPos.x(x + buttonWidth - textSize.x() - 5);
            } else if(textAnchor == VUIStyle.TEXT_ANCHOR_CENTER) {
                textPos.x(x + (buttonWidth - textSize.x()) / 2.0f);
            } else if(textAnchor == VUIStyle.TEXT_ANCHOR_LEFT) {
                textPos.x(x + 5);
            }
        } else {
            textPos.x(x + buttonWidth - 5 - textSize.x());
        }

        Raylib.DrawTextEx(
            VUIIO.style.getTextFont().getFont(), content,
            textPos, VUIIO.style.getTextSize(),
            VUIIO.style.getTextSpacing(), VUIIO.style.getTextCol().toRlCol()
        );

        boolean clicked = !VUIIO.disabled && VUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);

        if(clicked) {
            ref.set(!ref.get());
        }

        return clicked;
    }

    /**
     * Draw selectable button (without size specifies; based on <code>button</code>).
     *
     * @param ref Selected button reference.
     * @param content Button content.
     * @param x X Position.
     * @param y Y Position.
     * @return Is selectable button clicked.
     */
    public static boolean selectButton(VOutRef<Boolean> ref, String content, int x, int y) {
        return selectButton(ref, content, x, y, -1, -1);
    }

    /**
     * Draw radio button.
     *
     * @param ref Radio button value reference.
     * @param rBGroup Radio button group.
     * @param rId Radio button ID in group.
     * @param content Radio button text.
     * @param x X Position.
     * @param y Y Position.
     * @return Is radio button clicked.
     */
    public static boolean buttonRadio(VOutRef<Boolean> ref, RadioButtonGroup rBGroup, int rId, String content, int x, int y) {
        assert_t(ref == null && rBGroup == null, "ref == null && rBGroup == null: expected reference or button group");

        if(rBGroup != null) {
            assert_t(!rBGroup.isIDValid(rId), "!rBGroup.isIDValid(rId): invalid rId");
        }

        Raylib.Vector2 textSize = Raylib.MeasureTextEx(VUIIO.style.getTextFont().getFont(), content, VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing());

        float radioButtonX = x - RADIO_BUTTON_WIDTH * 2.0f;
        float radioButtonY = y + 1 - RADIO_BUTTON_HEIGHT - textSize.y() / 2.0f;

        float radioButtonWidth = textSize.x() + RADIO_BUTTON_WIDTH + ((RADIO_BUTTON_RADIUS * 1.5f) - 1) * 2;
        float radioButtonHeight = textSize.y() + RADIO_BUTTON_HEIGHT;

        VUIColor buttonColor = VUIIO.style.getDefaultCol();

        if(!VUIIO.disabled && VUIIO.mouseHovers(radioButtonX, radioButtonY, radioButtonWidth, radioButtonHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = VUIIO.style.getPressedCol();
        } else if(!VUIIO.disabled && VUIIO.mouseHovers(radioButtonX, radioButtonY, radioButtonWidth, radioButtonHeight)) {
            buttonColor = VUIIO.style.getFocusedCol();
        } else if(VUIIO.disabled) {
            buttonColor = VUIIO.style.getDisabledCol();
        }

        if(ref == null ? rBGroup.isActive(rId) : (ref.get() != null && ref.get())) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawCircle(x, y, RADIO_BUTTON_WIDTH + RADIO_BUTTON_HEIGHT, buttonColor.toRlCol());

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawCircleLines(x, y, RADIO_BUTTON_WIDTH + RADIO_BUTTON_HEIGHT, buttonColor.toRlCol());
        }

        Raylib.DrawTextEx(
            VUIIO.style.getTextFont().getFont(), content, new Raylib.Vector2().x(x + (RADIO_BUTTON_RADIUS * 1.5f) + 1).y(y - 1 - textSize.y() / 2.0f),
            VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing(),
            VUIIO.style.getTextCol().toRlCol()
        );

        boolean clicked = !VUIIO.disabled && VUIIO.mouseHovers(radioButtonX, radioButtonY, radioButtonWidth, radioButtonHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);

        if(clicked) {
            if(ref == null) {
                rBGroup.switchAll(rId);
            } else {
                ref.set(ref.get() != null && !ref.get());
            }
        }

        return clicked;
    }

    /**
     * Draw radio button (Radio Button Group).
     *
     * @param rBGroup Radio button group.
     * @param rId Radio button ID in group.
     * @param content Radio button text.
     * @param x X Position.
     * @param y Y Position.
     * @return Is radio button clicked.
     */
    public static boolean buttonRadio(RadioButtonGroup rBGroup, int rId, String content, int x, int y) {
        return buttonRadio(null, rBGroup, rId, content, x, y);
    }

    /**
     * Draw checkbox.
     *
     * @param ref Radio button value reference.
     * @param content Radio button text.
     * @param x X Position.
     * @param y Y Position.
     * @return Is checkbox clicked.
     */
    public static boolean checkbox(VOutRef<Boolean> ref, String content, int x, int y) {
        if(ref.get() == null) ref.set(false);

        Raylib.Vector2 textSize = Raylib.MeasureTextEx(VUIIO.style.getTextFont().getFont(), content, VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing());

        float checkboxX = x - 2.0f;
        float checkboxY = y - 3.0f;

        float checkboxWidth = textSize.x() + CHECKBOX_WIDTH * 2.0f;
        float checkboxHeight = textSize.y() + CHECKBOX_HEIGHT / 2.0f;

        VUIColor buttonColor = VUIIO.style.getDefaultCol();

        if(!VUIIO.disabled && VUIIO.mouseHovers(checkboxX, checkboxY, checkboxWidth, checkboxHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = VUIIO.style.getPressedCol();
        } else if(!VUIIO.disabled && VUIIO.mouseHovers(checkboxX, checkboxY, checkboxWidth, checkboxHeight)) {
            buttonColor = VUIIO.style.getFocusedCol();
        } else if(VUIIO.disabled) {
            buttonColor = VUIIO.style.getDisabledCol();
        }

        if(ref.get()) {
            if(VUIIO.style.getBorderRounding() > 0) {
                if(texelBleedingFixAvailable()) {
                    Raylib.BeginShaderMode(texelBleedingFixShader);
                }

                Raylib.DrawRectangleRounded(
                    new Raylib.Rectangle().x(x).y(y).width(CHECKBOX_WIDTH).height(CHECKBOX_HEIGHT),
                    VUIIO.style.getBorderRounding(), 16, buttonColor.toRlCol()
                );

                if(texelBleedingFixAvailable()) {
                    Raylib.EndShaderMode();
                }
            } else {
                Raylib.DrawRectangle(x, y, CHECKBOX_WIDTH, CHECKBOX_HEIGHT, VUIIO.style.getBorderColor().toRlCol());
            }
        } else {
            if(VUIIO.style.getBorderThickness() > 0) {
                if(VUIIO.style.getBorderRounding() > 0) {
                    if(texelBleedingFixAvailable()) {
                        Raylib.BeginShaderMode(texelBleedingFixShader);
                    }

                    Raylib.DrawRectangleRoundedLines(
                        new Raylib.Rectangle().x(x).y(y).width(CHECKBOX_WIDTH).height(CHECKBOX_HEIGHT),
                        VUIIO.style.getBorderRounding(), 16, VUIIO.style.getBorderThickness(), buttonColor.toRlCol()
                    );

                    if(texelBleedingFixAvailable()) {
                        Raylib.EndShaderMode();
                    }
                } else {
                    Raylib.DrawRectangleLinesEx(
                        new Raylib.Rectangle().x(x).y(y).width(CHECKBOX_WIDTH).height(CHECKBOX_HEIGHT),
                        VUIIO.style.getBorderThickness(), buttonColor.toRlCol()
                    );
                }
            }
        }

        Raylib.DrawTextEx(
            VUIIO.style.getTextFont().getFont(), content,
            new Raylib.Vector2().x(x + (CHECKBOX_WIDTH * 1.5f)).y(y + (CHECKBOX_HEIGHT - textSize.y()) / 2.0f),
            VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing(),
            VUIIO.style.getTextCol().toRlCol()
        );

        boolean clicked = !VUIIO.disabled && VUIIO.mouseHovers(checkboxX, checkboxY, checkboxWidth, checkboxHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);

        if(clicked) {
            ref.set(!ref.get());
        }

        return clicked;
    }

    /**
     * Draw progress bar.
     *
     * @param pRef Progress reference.
     * @param x Position X.
     * @param y Position Y.
     * @param w Width.
     * @param h Height.
     */
    public static void progressBar(VOutRef<Integer> pRef, int x, int y, int w, int h) {
        int progress = pRef.get() == null ? 0 : (int) clamp(0, 100, pRef.get());

        int barWidth = w == -1 ? DEFAULT_PROGRESS_BAR_WIDTH : w;
        int barHeight = h == -1 ? DEFAULT_PROGRESS_BAR_HEIGHT : h;

        int barProgress = (int) scale(progress, barWidth, 100);

        if(VUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawRectangleRounded(
                new Raylib.Rectangle().x(x - 1).y(y - 1).width(barProgress + 1).height(barHeight + 1),
                VUIIO.style.getBorderRounding(), 16, VUIIO.style.getDefaultCol().toRlCol()
            );

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawRectangle(x, y, barProgress, barHeight, VUIIO.style.getDefaultCol().toRlCol());
        }

        if(VUIIO.style.getBorderThickness() > 0) {
            if(VUIIO.style.getBorderRounding() > 0) {
                if(texelBleedingFixAvailable()) {
                    Raylib.BeginShaderMode(texelBleedingFixShader);
                }

                Raylib.DrawRectangleRoundedLines(
                    new Raylib.Rectangle().x(x).y(y).width(barWidth).height(barHeight),
                    VUIIO.style.getBorderRounding(), 16,
                    VUIIO.style.getBorderThickness(), VUIIO.style.getBorderColor().toRlCol()
                );

                if(texelBleedingFixAvailable()) {
                    Raylib.EndShaderMode();
                }
            } else {
                Raylib.DrawRectangleLinesEx(
                    new Raylib.Rectangle().x(x).y(y).width(barWidth).height(barHeight),
                    VUIIO.style.getBorderThickness(), VUIIO.style.getBorderColor().toRlCol()
                );
            }
        }
    }

    /**
     * Draw progress bar with default size.
     *
     * @param pRef Progress reference.
     * @param x Position X.
     * @param y Position Y.
     */
    public static void progressBar(VOutRef<Integer> pRef, int x, int y) {
        progressBar(pRef, x, y, -1, -1);
    }

    /**
     * String almost-slider.
     *
     * @param indexRef Selected string index in array reference.
     * @param array Array.
     * @param x X Position.
     * @param y Y Position.
     * @return Is selected string changed.
     */
    public static boolean stringSlider(VOutRef<Integer> indexRef, String[] array, int x, int y) {
        assert_t(array.length <= 0, "stringSlider.array <= 0: no values to iterate");

        int arrayIndex = indexRef.get() == null ? 0 : indexRef.get();

        Raylib.Vector2 textSize = Raylib.MeasureTextEx(VUIIO.style.getTextFont().getFont(),
            array[arrayIndex], VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing());

        Raylib.DrawTextEx(
            VUIIO.style.getTextFont().getFont(), array[arrayIndex],
            new Raylib.Vector2().x(x + 1).y(y),
            VUIIO.style.getTextSize(), VUIIO.style.getTextSpacing(),
            VUIIO.style.getTextCol().toRlCol()
        );

        boolean backwardsChanged, forwardsChanged;

        beginStyle(strSliderButtonStyle);

        backwardsChanged = button("<", x - (int) strSliderLeftButtonWidth * 2, y - (int) (textSize.y() / 2) / 2);

        if(backwardsChanged) {
            indexRef.set(indexRef.get() == 0 ? array.length - 1 : indexRef.get() - 1);
        }

        forwardsChanged = button(">", x + (int) textSize.x() + 2, y - (int) (textSize.y() / 2) / 2);

        if(forwardsChanged) {
            indexRef.set(indexRef.get() == array.length - 1 ? 0 : indexRef.get() + 1);
        }

        endStyle();

        return backwardsChanged || forwardsChanged;
    }

    /**
     * Float horizontal slider.
     *
     * @param sliderVRef Slider value reference.
     * @param min Minimal slider value.
     * @param max Maximal slider value.
     * @param x X Position.
     * @param y Y Position.
     * @return Is slider value changed?
     */
    public static boolean floatSlider(VOutRef<Float> sliderVRef, float min, float max, int x, int y) {
        float value = sliderVRef.get() == null ? 0 : (float) clamp(min, max, sliderVRef.get());

        float sliderValue = (float) scale(value, DEFAULT_SLIDER_WIDTH - DEFAULT_SLIDER_BUTTON_WIDTH, max);

        int sliderButtonX = x + (int) sliderValue;
        int sliderButtonY = y - DEFAULT_SLIDER_BUTTON_HEIGHT / 2;

        int sliderButtonWidth = DEFAULT_SLIDER_BUTTON_WIDTH;

        boolean dragging = false;

        boolean valueChanged = false;

        Raylib.DrawLineEx(new Raylib.Vector2().x(x).y(y), new Raylib.Vector2().x(x + DEFAULT_SLIDER_WIDTH).y(y), 5, VUIIO.style.getDefaultCol().toRlCol());

        VUIColor buttonColor = VUIIO.style.getDefaultCol();

        if(!VUIIO.disabled && VUIIO.mouseHovers(sliderButtonX, sliderButtonY, sliderButtonWidth, DEFAULT_SLIDER_BUTTON_HEIGHT) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = VUIIO.style.getPressedCol();
        } else if(!VUIIO.disabled && VUIIO.mouseHovers(sliderButtonX, sliderButtonY, sliderButtonWidth, DEFAULT_SLIDER_BUTTON_HEIGHT)) {
            buttonColor = VUIIO.style.getFocusedCol();
        } else if(VUIIO.disabled) {
            buttonColor = VUIIO.style.getDisabledCol();
        }

        if(!VUIIO.disabled && VUIIO.mouseHovers(sliderButtonX, sliderButtonY, sliderButtonWidth, DEFAULT_SLIDER_BUTTON_HEIGHT)) {
            if(Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
                dragging = true;

                float newValue = (float) clamp(min, max, sliderVRef.get() + (Raylib.GetMouseX() - sliderButtonX) - 10);

                if(newValue != sliderVRef.get()) {
                    sliderVRef.set(newValue);

                    valueChanged = true;
                }
            }
        }

        if(!VUIIO.disabled && VUIIO.mouseHovers(x, y, DEFAULT_SLIDER_WIDTH, DEFAULT_SLIDER_HEIGHT) && Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && !dragging) {
            sliderVRef.set((float) clamp(min, max, sliderVRef.get() + (Raylib.GetMouseX() - sliderButtonX) - 10));

            valueChanged = true;
        }

        Raylib.DrawRectangle(sliderButtonX, sliderButtonY, DEFAULT_SLIDER_BUTTON_WIDTH, DEFAULT_SLIDER_BUTTON_HEIGHT, buttonColor.toRlCol());

        return valueChanged;
    }

    /**
     * Apply step to value.
     *
     * @param value Value.
     * @param step Step.
     */
    public static float applyStepRef(VOutRef<Float> value, float step) {
        return value.get() * step;
    }

    /**
     * Draw image.
     *
     * @param image Image.
     * @param x X Position.
     * @param y Y Position.
     * @param scale Image scale.
     * @param tint Image tint.
     * @return Is image clicked.
     */
    public static boolean image(Texture image, int x, int y, float scale, VUIColor tint) {
        assert_t(!image.valid(), "image != valid (vui)");

        Raylib.DrawTextureEx(image.getTex(), new Raylib.Vector2().x(x).y(y), 0.0f, scale, tint.toRlCol());

        return !VUIIO.disabled
            && VUIIO.mouseHovers(x, y, image.getTex().width() * scale, image.getTex().height() * scale)
                && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
    }

    /**
     * Draw image with white tint.
     *
     * @param image Image.
     * @param x X Position.
     * @param y Y Position.
     * @param scale Image scale.
     * @return Is image clicked.
     */
    public static boolean image(Texture image, int x, int y, float scale) {
        return image(image, x, y, scale, new VUIColor(255, 255, 255, 255));
    }

    /**
     * Draw image with white tint and default scale.
     *
     * @param image Image.
     * @param x X Position.
     * @param y Y Position.
     * @return Is image clicked.
     */
    public static boolean image(Texture image, int x, int y) {
        return image(image, x, y, 1.0f, new VUIColor(255, 255, 255, 255));
    }


    /**
     * Draw loading icon.
     *
     * @param pRef Progress reference.
     * @param icon Icon.
     * @param x X Position.
     * @param y Y Position.
     * @param scale Icon scale.
     * @param tint Icon tint.
     */
    public static void loadingIcon(VOutRef<Float> pRef, Texture icon, int x, int y, float scale, VUIColor tint) {
        float progress = (float) clamp(0.0, 1.0, pRef.get() == null ? 0.0 : pRef.get());

        if(prevLoadingIconProgress != progress) {
            loadingIconShader.setUniformFloat("progress", progress);

            prevLoadingIconProgress = progress;
        }

        loadingIconShader.begin();

        image(icon, x, y, scale, tint);

        loadingIconShader.end();
    }

    /**
     * Draw loading icon with default scale.
     *
     * @param pRef Progress reference (0.0->1.0).
     * @param icon Icon.
     * @param x X Position.
     * @param y Y Position.
     * @param scale Icon scale.
     */
    public static void loadingIcon(VOutRef<Float> pRef, Texture icon, int x, int y, float scale) {
        loadingIcon(pRef, icon, x, y, scale, new VUIColor(255, 255, 255, 255));
    }

    /**
     * Draw loading icon with default scale.
     *
     * @param pRef Progress reference (0.0->1.0).
     * @param icon Icon.
     * @param x X Position.
     * @param y Y Position.
     */
    public static void loadingIcon(VOutRef<Float> pRef, Texture icon, int x, int y) {
        loadingIcon(pRef, icon, x, y, 1.0f, new VUIColor(255, 255, 255, 255));
    }

    /**
     * Draw bezier line.
     *
     * @param startX Line Start X.
     * @param startY Line Start Y.
     * @param endX Line End X.
     * @param endY Line End Y.
     * @param thickness Line thickness.
     * @param color Line color.
     */
    public static void bezierCurvePair(int startX, int startY, int endX, int endY, float thickness, Raylib.Color color) {
        color = color == null ? VUIIO.style.getDefaultCol().toRlCol() : color;

        Raylib.DrawLineBezier(new Raylib.Vector2().x(startX).y(startY), new Raylib.Vector2().x(endX).y(endY), thickness, color);

        if(VUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawCircle(startX, startY, thickness / 2.0f, color);
            Raylib.DrawCircle(endX, endY, thickness / 2.0f, color);

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        }
    }

    /**
     * Draw bezier line with default color.
     *
     * @param startX Line Start X.
     * @param startY Line Start Y.
     * @param endX Line End X.
     * @param endY Line End Y.
     * @param thickness Line thickness.
     */
    public static void bezierCurvePair(int startX, int startY, int endX, int endY, float thickness) {
        bezierCurvePair(startX, startY, endX, endY, thickness, null);
    }

    /**
     * Create new reference (VOutRef).
     *
     * @param object Reference default object.
     */
    public static <T> VOutRef<T> newRef(T object) {
        return new VOutRef<>(object);
    }

    /**
     * Create new reference on state (VOut<Boolean>).
     *
     * @param state Current state.
     */
    public static VOutRef<Boolean> newStateRef(boolean state) {
        return newRef(state);
    }

    /**
     * Create new reference on state (VOut<Boolean>) with default value false.
     */
    public static VOutRef<Boolean> newStateRef() {
        return newStateRef(false);
    }

    /**
     * Get final (original) style.
     */
    public static VUIStyle getFinalStyle() {
        return finalStyle;
    }

    /**
     * Create RadioButtonGroup and allocate space for radio buttons.
     *
     * @param amount Amount of space to allocate.
     * @param idActive Activate specific ID (only one, because only one allowed).
     */
    public static RadioButtonGroup allocRBGroup(int amount, int idActive) {
        RadioButtonGroup group = new RadioButtonGroup();

        group.allocate(amount, idActive);

        return group;
    }

    /**
     * Class for containing all radio buttons in one list, for example allows making only one radio button selectable.
     */
    public static class RadioButtonGroup {
        private static class RadioButton {
            private final VOutRef<Boolean> active;

            private final int id;

            protected RadioButton(boolean active_, int id_) {
                active = new VOutRef<>(active_);

                id = id_;
            }
        }

        private final ArrayList<RadioButton> group;

        /**
         * Initialize radio button group.
         */
        public RadioButtonGroup() {
            group = new ArrayList<>();
        }

        /**
         * Allocate new space in group for new radio button.
         *
         * @param active Is radio button active?
         */
        public void allocate(boolean active) {
            group.add(new RadioButton(active, group.size() + 1));
        }

        /**
         * Allocate N amount of space in group for new radio button.
         *
         * @param amount Amount of space to allocate.
         * @param idActive Activate specific ID (only one, because only one allowed).
         */
        public void allocate(int amount, int idActive) {
            for(int n=1; n <= amount + 1; n++) {
                allocate(n == idActive);
            }
        }

        /**
         * Allocate N amount of space in group for new radio button.
         *
         * @param amount Amount of space to allocate.
         */
        public void allocate(int amount) {
            allocate(amount, -1);
        }

        /**
         * Is button with ID is active.
         *
         * @param id Button ID.
         */
        public boolean isActive(int id) {
            return group.get(id - 1).active.get();
        }

        /**
         * Is ID valid.
         *
         * @param id Button ID.
         */
        public boolean isIDValid(int id) {
            return inRange(id, 1, group.size());
        }

        /**
         * Deactivate all buttons and activate button with next ID.
         *
         * @param id Button ID.
         */
        public void switchAll(int id) {
            for(RadioButton rButton : group) {
                rButton.active.set(rButton.id == id);
            }
        }

        /**
         * Deactivate all buttons.
         */
        public void deactivateAll() {
            for(RadioButton rButton : group) {
                rButton.active.set(false);
            }
        }
    }
}
