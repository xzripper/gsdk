package gsdk.source.igui;

import com.raylib.Raylib;

import java.util.ArrayList;

import gsdk.source.grender.Texture;

import gsdk.source.generic.GShader;

import static gsdk.source.generic.Range.inRange;

import static gsdk.source.generic.GMath.clamp;
import static gsdk.source.generic.GMath.scale;

import static gsdk.r_utilities.PathResolver.resolvePath;

import static gsdk.source.generic.Assert.assert_t;

/**
 * Main Game User Interface Class.
 */
public class GUI {
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

    private static GUIStyle finalStyle;

    private static GUIStyle strSliderButtonStyle;

    private static float strSliderLeftButtonWidth;

    private static Raylib.Shader texelBleedingFixShader = null;

    private static GShader loadingIconShader = null;

    private static float prevLoadingIconProgress, loadingIconTint;

    public static final int TOP = 1, VERTICAL = 2, PIE = 3;

    /**
     * Load texel bleeding fix shader.
     */
    public static void loadTexelBleedingFixShader() {
        texelBleedingFixShader = Raylib.LoadShader(null, resolvePath("gsdk/shaders/texel_bleeding_fix2d.fs"));
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
     * Create new GUI context.
     *
     * @param style Style.
     */
    public static void newVuiCtx(GUIStyle style) {
        GUIIO.newCtx(style);

        loadingIconShader = new GShader(null, resolvePath("gsdk/shaders/loading_icon2d.fs"), GShader.FILE);

        setLoadingIconMode(TOP);

        setLoadingIconTint(0.3f);

        finalStyle = GUIIO.style;

        GUIColor focused = finalStyle.getFocusedCol();
        GUIColor pressed = finalStyle.getPressedCol();

        strSliderButtonStyle = new GUIStyle(
            new GUIColor(0, 0, 0, 0), new GUIColor(focused.get('r'), focused.get('g'), focused.get('b'), focused.get('a') / 2),
            new GUIColor(pressed.get('r'), pressed.get('g'), pressed.get('b'), pressed.get('a') / 2),
            new GUIColor(0, 0, 0, 148), 0, 0, null, 20, 0.1f, finalStyle.getTextFont(),
            GUIStyle.TEXT_ANCHOR_CENTER, new GUIColor(255, 255, 255, 255)
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
        GUIIO.beginDisabled();
    }

    /**
     * Do not disable next objects.
     */
    public static void enableNext() {
        GUIIO.endDisabled();
    }

    /**
     * Draw next objects with specified style.
     *
     * @param style Style.
     */
    public static void beginStyle(GUIStyle style) {
        GUIIO.style = style;
    }

    /**
     * Stop drawing next objects with specified style.
     */
    public static void endStyle() {
        GUIIO.style = finalStyle;
    }

    /**
     * Draw next objects with specified alpha.
     *
     * @param alpha Alpha (opacity 0->255).
     */
    public static void beginAlpha(int alpha) {
        GUIIO.style.getDefaultCol().set('a', alpha);
        GUIIO.style.getFocusedCol().set('a', alpha);
        GUIIO.style.getPressedCol().set('a', alpha);

        GUIIO.style.getBorderColor().set('a', alpha);

        GUIIO.style.getTextCol().set('a', alpha);
    }

    /**
     * Restore style alpha.
     */
    public static void endAlpha() {
        GUIIO.style.getDefaultCol().set('a', finalStyle.getDefaultCol().get('a'));
        GUIIO.style.getFocusedCol().set('a', finalStyle.getFocusedCol().get('a'));
        GUIIO.style.getPressedCol().set('a', finalStyle.getPressedCol().get('a'));

        GUIIO.style.getBorderColor().set('a', finalStyle.getBorderColor().get('a'));

        GUIIO.style.getTextCol().set('a', finalStyle.getTextCol().get('a'));
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
    public static boolean rectangle(int x, int y, int w, int h, GUIColor color, boolean retHoverEv) {
        Raylib.DrawRectangle(x, y, w, h, color.toRlCol());

        return retHoverEv ? (!GUIIO.disabled && GUIIO.mouseHovers(x, y, w, h)) : !GUIIO.disabled && GUIIO.mouseHovers(x, y, w, h) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
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
    public static boolean rectangle(int x, int y, int w, int h, GUIColor color) {
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
        return rectangle(x, y, w, h, GUIIO.style.getDefaultCol(), false);
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
        if(GUIIO.style.getBorderThickness() > 0) {
            if(GUIIO.style.getBorderRounding() > 0) {
                Raylib.DrawRectangleRoundedLines(new Raylib.Rectangle().x(x).y(y).width(w).height(h),
                    GUIIO.style.getBorderRounding(), 16,
                    GUIIO.style.getBorderThickness(),
                    GUIIO.style.getBorderColor().toRlCol()
                );
            } else {
                Raylib.DrawRectangleLinesEx(new Raylib.Rectangle().x(x).y(y).width(w).height(h),
                    GUIIO.style.getBorderThickness(),
                    GUIIO.style.getBorderColor().toRlCol()
                );
            }
        }

        return retHoverEv ? (!GUIIO.disabled && GUIIO.mouseHovers(x, y, w, h)) : !GUIIO.disabled && GUIIO.mouseHovers(x, y, w, h) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
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
            GUIIO.style.getTextFont().getFont(),
            content, new Raylib.Vector2().x(x).y(y),
            GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing(),
            GUIIO.style.getTextCol().toRlCol()
        );

        return !GUIIO.disabled && GUIIO.mouseHoversText(content, x, y) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
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

        textSize = Raylib.MeasureTextEx(GUIIO.style.getTextFont().getFont(),
            content, GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing());

        if(w == -1 && h == -1) {
            buttonWidth = textSize.x() + 10;
            buttonHeight = textSize.y() + 5;
        } else {
            buttonWidth = w;
            buttonHeight = h;
        }

        GUIColor buttonColor = GUIIO.style.getDefaultCol();

        if(!GUIIO.disabled && GUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = GUIIO.style.getPressedCol();
        } else if(!GUIIO.disabled && GUIIO.mouseHovers(x, y, buttonWidth, buttonHeight)) {
            buttonColor = GUIIO.style.getFocusedCol();
        } else if(GUIIO.disabled) {
            buttonColor = GUIIO.style.getDisabledCol();
        }

        if(GUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawRectangleRounded(
                new Raylib.Rectangle().x(x - 1).y(y - 1)
                    .width(buttonWidth + 1).height(buttonHeight + 1),
                GUIIO.style.getBorderRounding(), 16, buttonColor.toRlCol()
            );

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawRectangle(x, y, (int) buttonWidth, (int) buttonHeight, buttonColor.toRlCol());
        }

        if(GUIIO.style.getBorderThickness() > 0) {
            if(GUIIO.style.getBorderRounding() > 0) {
                Raylib.DrawRectangleRoundedLines(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    GUIIO.style.getBorderRounding(), 16,
                    GUIIO.style.getBorderThickness(), GUIIO.style.getBorderColor().toRlCol()
                );
            } else {
                Raylib.DrawRectangleLinesEx(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    GUIIO.style.getBorderThickness(),
                    GUIIO.style.getBorderColor().toRlCol()
                );
            }
        }

        Raylib.Vector2 textPos = new Raylib.Vector2().y(y + (buttonHeight - textSize.y()) / 2);

        if(w != -1 && h != -1) {
            int textAnchor = GUIIO.style.getTextAnchor();

            if(textAnchor == GUIStyle.TEXT_ANCHOR_RIGHT) {
                textPos.x(x + buttonWidth - textSize.x() - 5);
            } else if(textAnchor == GUIStyle.TEXT_ANCHOR_CENTER) {
                textPos.x(x + (buttonWidth - textSize.x()) / 2.0f);
            } else if(textAnchor == GUIStyle.TEXT_ANCHOR_LEFT) {
                textPos.x(x + 5);
            }
        } else {
            textPos.x(x + buttonWidth - 5 - textSize.x());
        }

        Raylib.DrawTextEx(
            GUIIO.style.getTextFont().getFont(), content,
            textPos, GUIIO.style.getTextSize(),
            GUIIO.style.getTextSpacing(), GUIIO.style.getTextCol().toRlCol()
        );

        return !GUIIO.disabled && GUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);
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
    public static boolean selectButton(GOutRef<Boolean> ref, String content, int x, int y, int w, int h) {
        if(ref.get() == null) ref.set(false);

        Raylib.Vector2 textSize;

        float buttonWidth, buttonHeight;

        textSize = Raylib.MeasureTextEx(GUIIO.style.getTextFont().getFont(),
            content, GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing());

        if(w == -1 && h == -1) {
            buttonWidth = textSize.x() + 10;
            buttonHeight = textSize.y() + 5;
        } else {
            buttonWidth = w;
            buttonHeight = h;
        }

        GUIColor buttonColor = null;

        if(!GUIIO.disabled && GUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT) || ref.get()) {
            buttonColor = GUIIO.style.getPressedCol();
        } else if(!GUIIO.disabled && GUIIO.mouseHovers(x, y, buttonWidth, buttonHeight)) {
            buttonColor = GUIIO.style.getFocusedCol();
        } else if(GUIIO.disabled) {
            buttonColor = GUIIO.style.getDisabledCol();
        } else if(!ref.get()) {
            buttonColor = GUIIO.style.getDefaultCol();
        }

        if(GUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawRectangleRounded(
                new Raylib.Rectangle().x(x - 1).y(y - 1)
                    .width(buttonWidth + 1).height(buttonHeight + 1),
                GUIIO.style.getBorderRounding(), 16, buttonColor.toRlCol()
            );

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawRectangle(x, y, (int) buttonWidth, (int) buttonHeight, buttonColor.toRlCol());
        }

        if(GUIIO.style.getBorderThickness() > 0) {
            if(GUIIO.style.getBorderRounding() > 0) {
                Raylib.DrawRectangleRoundedLines(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    GUIIO.style.getBorderRounding(), 16,
                    GUIIO.style.getBorderThickness(), GUIIO.style.getBorderColor().toRlCol()
                );
            } else {
                Raylib.DrawRectangleLinesEx(
                    new Raylib.Rectangle().x(x).y(y).width(buttonWidth).height(buttonHeight),
                    GUIIO.style.getBorderThickness(),
                    GUIIO.style.getBorderColor().toRlCol()
                );
            }
        }

        Raylib.Vector2 textPos = new Raylib.Vector2().y(y + (buttonHeight - textSize.y()) / 2);

        if(w != -1 && h != -1) {
            int textAnchor = GUIIO.style.getTextAnchor();

            if(textAnchor == GUIStyle.TEXT_ANCHOR_RIGHT) {
                textPos.x(x + buttonWidth - textSize.x() - 5);
            } else if(textAnchor == GUIStyle.TEXT_ANCHOR_CENTER) {
                textPos.x(x + (buttonWidth - textSize.x()) / 2.0f);
            } else if(textAnchor == GUIStyle.TEXT_ANCHOR_LEFT) {
                textPos.x(x + 5);
            }
        } else {
            textPos.x(x + buttonWidth - 5 - textSize.x());
        }

        Raylib.DrawTextEx(
            GUIIO.style.getTextFont().getFont(), content,
            textPos, GUIIO.style.getTextSize(),
            GUIIO.style.getTextSpacing(), GUIIO.style.getTextCol().toRlCol()
        );

        boolean clicked = !GUIIO.disabled && GUIIO.mouseHovers(x, y, buttonWidth, buttonHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);

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
    public static boolean selectButton(GOutRef<Boolean> ref, String content, int x, int y) {
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
    public static boolean buttonRadio(GOutRef<Boolean> ref, RadioButtonGroup rBGroup, int rId, String content, int x, int y) {
        assert_t(ref == null && rBGroup == null, "ref == null && rBGroup == null: expected reference or button group");

        if(rBGroup != null) {
            assert_t(!rBGroup.isIDValid(rId), "!rBGroup.isIDValid(rId): invalid rId");
        }

        Raylib.Vector2 textSize = Raylib.MeasureTextEx(GUIIO.style.getTextFont().getFont(), content, GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing());

        float radioButtonX = x - RADIO_BUTTON_WIDTH * 2.0f;
        float radioButtonY = y + 1 - RADIO_BUTTON_HEIGHT - textSize.y() / 2.0f;

        float radioButtonWidth = textSize.x() + RADIO_BUTTON_WIDTH + ((RADIO_BUTTON_RADIUS * 1.5f) - 1) * 2;
        float radioButtonHeight = textSize.y() + RADIO_BUTTON_HEIGHT;

        GUIColor buttonColor = GUIIO.style.getDefaultCol();

        if(!GUIIO.disabled && GUIIO.mouseHovers(radioButtonX, radioButtonY, radioButtonWidth, radioButtonHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = GUIIO.style.getPressedCol();
        } else if(!GUIIO.disabled && GUIIO.mouseHovers(radioButtonX, radioButtonY, radioButtonWidth, radioButtonHeight)) {
            buttonColor = GUIIO.style.getFocusedCol();
        } else if(GUIIO.disabled) {
            buttonColor = GUIIO.style.getDisabledCol();
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
            GUIIO.style.getTextFont().getFont(), content, new Raylib.Vector2().x(x + (RADIO_BUTTON_RADIUS * 1.5f) + 1).y(y - 1 - textSize.y() / 2.0f),
            GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing(),
            GUIIO.style.getTextCol().toRlCol()
        );

        boolean clicked = !GUIIO.disabled && GUIIO.mouseHovers(radioButtonX, radioButtonY, radioButtonWidth, radioButtonHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);

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
    public static boolean checkbox(GOutRef<Boolean> ref, String content, int x, int y) {
        if(ref.get() == null) ref.set(false);

        Raylib.Vector2 textSize = Raylib.MeasureTextEx(GUIIO.style.getTextFont().getFont(), content, GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing());

        float checkboxX = x - 2.0f;
        float checkboxY = y - 3.0f;

        float checkboxWidth = textSize.x() + CHECKBOX_WIDTH * 2.0f;
        float checkboxHeight = textSize.y() + CHECKBOX_HEIGHT / 2.0f;

        GUIColor buttonColor = GUIIO.style.getDefaultCol();

        if(!GUIIO.disabled && GUIIO.mouseHovers(checkboxX, checkboxY, checkboxWidth, checkboxHeight) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = GUIIO.style.getPressedCol();
        } else if(!GUIIO.disabled && GUIIO.mouseHovers(checkboxX, checkboxY, checkboxWidth, checkboxHeight)) {
            buttonColor = GUIIO.style.getFocusedCol();
        } else if(GUIIO.disabled) {
            buttonColor = GUIIO.style.getDisabledCol();
        }

        if(ref.get()) {
            if(GUIIO.style.getBorderRounding() > 0) {
                if(texelBleedingFixAvailable()) {
                    Raylib.BeginShaderMode(texelBleedingFixShader);
                }

                Raylib.DrawRectangleRounded(
                    new Raylib.Rectangle().x(x).y(y).width(CHECKBOX_WIDTH).height(CHECKBOX_HEIGHT),
                    GUIIO.style.getBorderRounding(), 16, buttonColor.toRlCol()
                );

                if(texelBleedingFixAvailable()) {
                    Raylib.EndShaderMode();
                }
            } else {
                Raylib.DrawRectangle(x, y, CHECKBOX_WIDTH, CHECKBOX_HEIGHT, GUIIO.style.getBorderColor().toRlCol());
            }
        } else {
            if(GUIIO.style.getBorderThickness() > 0) {
                if(GUIIO.style.getBorderRounding() > 0) {
                    if(texelBleedingFixAvailable()) {
                        Raylib.BeginShaderMode(texelBleedingFixShader);
                    }

                    Raylib.DrawRectangleRoundedLines(
                        new Raylib.Rectangle().x(x).y(y).width(CHECKBOX_WIDTH).height(CHECKBOX_HEIGHT),
                        GUIIO.style.getBorderRounding(), 16, GUIIO.style.getBorderThickness(), buttonColor.toRlCol()
                    );

                    if(texelBleedingFixAvailable()) {
                        Raylib.EndShaderMode();
                    }
                } else {
                    Raylib.DrawRectangleLinesEx(
                        new Raylib.Rectangle().x(x).y(y).width(CHECKBOX_WIDTH).height(CHECKBOX_HEIGHT),
                        GUIIO.style.getBorderThickness(), buttonColor.toRlCol()
                    );
                }
            }
        }

        Raylib.DrawTextEx(
            GUIIO.style.getTextFont().getFont(), content,
            new Raylib.Vector2().x(x + (CHECKBOX_WIDTH * 1.5f)).y(y + (CHECKBOX_HEIGHT - textSize.y()) / 2.0f),
            GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing(),
            GUIIO.style.getTextCol().toRlCol()
        );

        boolean clicked = !GUIIO.disabled && GUIIO.mouseHovers(checkboxX, checkboxY, checkboxWidth, checkboxHeight) && Raylib.IsMouseButtonReleased(Raylib.MOUSE_BUTTON_LEFT);

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
    public static void progressBar(GOutRef<Integer> pRef, int x, int y, int w, int h) {
        int progress = pRef.get() == null ? 0 : (int) clamp(0, 100, pRef.get());

        int barWidth = w == -1 ? DEFAULT_PROGRESS_BAR_WIDTH : w;
        int barHeight = h == -1 ? DEFAULT_PROGRESS_BAR_HEIGHT : h;

        int barProgress = (int) scale(progress, barWidth, 100);

        if(GUIIO.style.getBorderRounding() > 0) {
            if(texelBleedingFixAvailable()) {
                Raylib.BeginShaderMode(texelBleedingFixShader);
            }

            Raylib.DrawRectangleRounded(
                new Raylib.Rectangle().x(x - 1).y(y - 1).width(barProgress + 1).height(barHeight + 1),
                GUIIO.style.getBorderRounding(), 16, GUIIO.style.getDefaultCol().toRlCol()
            );

            if(texelBleedingFixAvailable()) {
                Raylib.EndShaderMode();
            }
        } else {
            Raylib.DrawRectangle(x, y, barProgress, barHeight, GUIIO.style.getDefaultCol().toRlCol());
        }

        if(GUIIO.style.getBorderThickness() > 0) {
            if(GUIIO.style.getBorderRounding() > 0) {
                if(texelBleedingFixAvailable()) {
                    Raylib.BeginShaderMode(texelBleedingFixShader);
                }

                Raylib.DrawRectangleRoundedLines(
                    new Raylib.Rectangle().x(x).y(y).width(barWidth).height(barHeight),
                    GUIIO.style.getBorderRounding(), 16,
                    GUIIO.style.getBorderThickness(), GUIIO.style.getBorderColor().toRlCol()
                );

                if(texelBleedingFixAvailable()) {
                    Raylib.EndShaderMode();
                }
            } else {
                Raylib.DrawRectangleLinesEx(
                    new Raylib.Rectangle().x(x).y(y).width(barWidth).height(barHeight),
                    GUIIO.style.getBorderThickness(), GUIIO.style.getBorderColor().toRlCol()
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
    public static void progressBar(GOutRef<Integer> pRef, int x, int y) {
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
    public static boolean stringSlider(GOutRef<Integer> indexRef, String[] array, int x, int y) {
        assert_t(array.length <= 0, "stringSlider.array <= 0: no values to iterate");

        int arrayIndex = indexRef.get() == null ? 0 : indexRef.get();

        Raylib.Vector2 textSize = Raylib.MeasureTextEx(GUIIO.style.getTextFont().getFont(),
            array[arrayIndex], GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing());

        Raylib.DrawTextEx(
            GUIIO.style.getTextFont().getFont(), array[arrayIndex],
            new Raylib.Vector2().x(x + 1).y(y),
            GUIIO.style.getTextSize(), GUIIO.style.getTextSpacing(),
            GUIIO.style.getTextCol().toRlCol()
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
    public static boolean floatSlider(GOutRef<Float> sliderVRef, float min, float max, int x, int y) {
        float value = sliderVRef.get() == null ? 0 : (float) clamp(min, max, sliderVRef.get());

        float sliderValue = (float) scale(value, DEFAULT_SLIDER_WIDTH - DEFAULT_SLIDER_BUTTON_WIDTH, max);

        int sliderButtonX = x + (int) sliderValue;
        int sliderButtonY = y - DEFAULT_SLIDER_BUTTON_HEIGHT / 2;

        int sliderButtonWidth = DEFAULT_SLIDER_BUTTON_WIDTH;

        boolean dragging = false;

        boolean valueChanged = false;

        Raylib.DrawLineEx(new Raylib.Vector2().x(x).y(y), new Raylib.Vector2().x(x + DEFAULT_SLIDER_WIDTH).y(y), 5, GUIIO.style.getDefaultCol().toRlCol());

        GUIColor buttonColor = GUIIO.style.getDefaultCol();

        if(!GUIIO.disabled && GUIIO.mouseHovers(sliderButtonX, sliderButtonY, sliderButtonWidth, DEFAULT_SLIDER_BUTTON_HEIGHT) && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            buttonColor = GUIIO.style.getPressedCol();
        } else if(!GUIIO.disabled && GUIIO.mouseHovers(sliderButtonX, sliderButtonY, sliderButtonWidth, DEFAULT_SLIDER_BUTTON_HEIGHT)) {
            buttonColor = GUIIO.style.getFocusedCol();
        } else if(GUIIO.disabled) {
            buttonColor = GUIIO.style.getDisabledCol();
        }

        if(!GUIIO.disabled && GUIIO.mouseHovers(sliderButtonX, sliderButtonY, sliderButtonWidth, DEFAULT_SLIDER_BUTTON_HEIGHT)) {
            if(Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
                dragging = true;

                float newValue = (float) clamp(min, max, sliderVRef.get() + (Raylib.GetMouseX() - sliderButtonX) - 10);

                if(newValue != sliderVRef.get()) {
                    sliderVRef.set(newValue);

                    valueChanged = true;
                }
            }
        }

        if(!GUIIO.disabled && GUIIO.mouseHovers(x, y, DEFAULT_SLIDER_WIDTH, DEFAULT_SLIDER_HEIGHT) && Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && !dragging) {
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
    public static float applyStepRef(GOutRef<Float> value, float step) {
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
    public static boolean image(Texture image, int x, int y, float scale, GUIColor tint) {
        assert_t(!image.valid(), "image != valid (GUI)");

        Raylib.DrawTextureEx(image.getTex(), new Raylib.Vector2().x(x).y(y), 0.0f, scale, tint.toRlCol());

        return !GUIIO.disabled
            && GUIIO.mouseHovers(x, y, image.getTex().width() * scale, image.getTex().height() * scale)
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
        return image(image, x, y, scale, new GUIColor(255, 255, 255, 255));
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
        return image(image, x, y, 1.0f, new GUIColor(255, 255, 255, 255));
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
    public static void loadingIcon(GOutRef<Float> pRef, Texture icon, int x, int y, float scale, GUIColor tint) {
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
    public static void loadingIcon(GOutRef<Float> pRef, Texture icon, int x, int y, float scale) {
        loadingIcon(pRef, icon, x, y, scale, new GUIColor(255, 255, 255, 255));
    }

    /**
     * Draw loading icon with default scale.
     *
     * @param pRef Progress reference (0.0->1.0).
     * @param icon Icon.
     * @param x X Position.
     * @param y Y Position.
     */
    public static void loadingIcon(GOutRef<Float> pRef, Texture icon, int x, int y) {
        loadingIcon(pRef, icon, x, y, 1.0f, new GUIColor(255, 255, 255, 255));
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
        color = color == null ? GUIIO.style.getDefaultCol().toRlCol() : color;

        Raylib.DrawLineBezier(new Raylib.Vector2().x(startX).y(startY), new Raylib.Vector2().x(endX).y(endY), thickness, color);

        if(GUIIO.style.getBorderRounding() > 0) {
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
     * Create new reference (GOutRef).
     *
     * @param object Reference default object.
     */
    public static <T> GOutRef<T> newRef(T object) {
        return new GOutRef<>(object);
    }

    /**
     * Create new reference on state (VOut<Boolean>).
     *
     * @param state Current state.
     */
    public static GOutRef<Boolean> newStateRef(boolean state) {
        return newRef(state);
    }

    /**
     * Create new reference on state (VOut<Boolean>) with default value false.
     */
    public static GOutRef<Boolean> newStateRef() {
        return newStateRef(false);
    }

    /**
     * Get final (original) style.
     */
    public static GUIStyle getFinalStyle() {
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
            private final GOutRef<Boolean> active;

            private final int id;

            protected RadioButton(boolean active_, int id_) {
                active = new GOutRef<>(active_);

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
