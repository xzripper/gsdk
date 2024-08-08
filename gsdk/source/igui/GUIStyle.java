package gsdk.source.igui;

import static gsdk.r_utilities.PathResolver.resolvePath;

/**
 * GUI Style Configuration.
 */
public class GUIStyle {
    private GUIColor defaultCol;
    private GUIColor focusedCol;
    private GUIColor pressedCol;
    private GUIColor disabledCol;

    private float borderThickness;
    private float borderRounding;
    private GUIColor borderColor;

    private int textSize;
    private float textSpacing;
    private GUIFont textFont;
    private int textAnchor;
    private GUIColor textCol;

    public static final int TEXT_ANCHOR_LEFT = 0;
    public static final int TEXT_ANCHOR_CENTER = 2;
    public static final int TEXT_ANCHOR_RIGHT = 3;

    /**
     * Create new GUI style configuration.
     *
     * @param defaultCol_ Default object color (not focused/pressed/etc).
     * @param focusedCol_ Focused color (object focused color).
     * @param pressedCol_ Pressed color (object pressed color).
     * @param disabledCol_ Disabled color (object disabled color).
     * @param borderThickness_ Object border thickness.
     * @param borderRounding_ Object border rounding.
     * @param borderColor_ Object border color.
     * @param textSize_ Text size.
     * @param textSpacing_ Text spacing.
     * @param textFont_ Text font.
     * @param textAnchor_ Text anchor.
     * @param textCol_ Text color.
     */
    public GUIStyle(GUIColor defaultCol_, GUIColor focusedCol_, GUIColor pressedCol_, GUIColor disabledCol_,
                    float borderThickness_, float borderRounding_, GUIColor borderColor_,
                    int textSize_, float textSpacing_, GUIFont textFont_, int textAnchor_, GUIColor textCol_) {
        defaultCol = defaultCol_;
        focusedCol = focusedCol_;
        pressedCol = pressedCol_;
        disabledCol = disabledCol_;

        borderThickness = borderThickness_;
        borderRounding = borderRounding_;
        borderColor = borderColor_;

        textSize = textSize_;
        textSpacing = textSpacing_;
        textFont = textFont_;
        textAnchor = textAnchor_;
        textCol = textCol_;
    }

    /**
     * Create new GUI style configuration (Default).
     */
    public GUIStyle() {
        defaultCol = new GUIColor(48, 48, 48, 255);
        focusedCol = new GUIColor(60, 60, 60, 255);
        pressedCol = new GUIColor(100, 100, 100, 255);
        disabledCol = new GUIColor(132, 132, 132, 248);

        borderThickness = 1.5f;
        borderRounding = 0.3f;
        borderColor = new GUIColor(186, 186, 186, 255);

        textSize = 16;
        textSpacing = 0.5f;
        textFont = new GUIFont(resolvePath("gsdk/resources/Lato-Regular.ttf"), true, true);
        textAnchor = TEXT_ANCHOR_CENTER;
        textCol = new GUIColor(248, 248, 248, 255);
    }

    /**
     * Set default color.
     *
     * @param defaultCol_ Default color.
     */
    public void setDefaultCol(GUIColor defaultCol_) {
        defaultCol = defaultCol_;
    }

    /**
     * Get default color.
     */
    public GUIColor getDefaultCol() {
        return defaultCol;
    }

    /**
     * Set focused color.
     *
     * @param focusedCol_ Focused color.
     */
    public void setFocusedCol(GUIColor focusedCol_) {
        focusedCol = focusedCol_;
    }

    /**
     * Get focused color.
     */
    public GUIColor getFocusedCol() {
        return focusedCol;
    }

    /**
     * Set pressed color.
     *
     * @param pressedCol_ Pressed color.
     */
    public void setPressedCol(GUIColor pressedCol_) {
        pressedCol = pressedCol_;
    }

    /**
     * Get pressed color.
     */
    public GUIColor getPressedCol() {
        return pressedCol;
    }

    /**
     * Set disabled color.
     *
     * @param disabledCol_ Disabled color.
     */
    public void setDisabledCol(GUIColor disabledCol_) {
        disabledCol = disabledCol_;
    }

    /**
     * Get disabled color.
     */
    public GUIColor getDisabledCol() {
        return disabledCol;
    }

    /**
     * Set border thickness.
     *
     * @param borderThickness_ Border thickness.
     */
    public void setBorderThickness(float borderThickness_) {
        borderThickness = borderThickness_;
    }

    /**
     * Get border thickness.
     */
    public float getBorderThickness() {
        return borderThickness;
    }

    /**
     * Set border rounding.
     *
     * @param borderRounding_ Border rounding.
     */
    public void setBorderRounding(float borderRounding_) {
        borderRounding = borderRounding_;
    }

    /**
     * Get border rounding.
     */
    public float getBorderRounding() {
        return borderRounding;
    }

    /**
     * Set border color.
     *
     * @param borderColor_ Border color.
     */
    public void setBorderColor(GUIColor borderColor_) {
        borderColor = borderColor_;
    }

    /**
     * Get border color.
     */
    public GUIColor getBorderColor() {
        return borderColor;
    }

    /**
     * Set text size.
     *
     * @param textSize_ Text size.
     */
    public void setTextSize(int textSize_) {
        textSize = textSize_;
    }

    /**
     * Get text size.
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * Set text size.
     *
     * @param textSpacing_ Text spacing.
     */
    public void setTextSpacing(int textSpacing_) {
        textSpacing = textSpacing_;
    }

    /**
     * Get text size.
     */
    public float getTextSpacing() {
        return textSpacing;
    }

    /**
     * Set text font.
     *
     * @param textFont_ Text font.
     */
    public void setTextFont(GUIFont textFont_) {
        textFont = textFont_;
    }

    /**
     * Get text font.
     */
    public GUIFont getTextFont() {
        return textFont;
    }

    /**
     * Set text anchor.
     *
     * @param textAnchor_ Text anchor.
     */
    public void setTextAnchor(int textAnchor_) {
        textAnchor = textAnchor_;
    }

    /**
     * Get text anchor.
     */
    public int getTextAnchor() {
        return textAnchor;
    }

    /**
     * Set text color.
     *
     * @param textCol_ Text color.
     */
    public void setTextCol(GUIColor textCol_) {
        textCol = textCol_;
    }

    /**
     * Get text color.
     */
    public GUIColor getTextCol() {
        return textCol;
    }
}
