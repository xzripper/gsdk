package gsdk.source.ivui;

import static gsdk.r_utilities.PathResolver.resolvePath;

/**
 * VUI Style Configuration.
 */
public class VUIStyle {
    private VUIColor defaultCol;
    private VUIColor focusedCol;
    private VUIColor pressedCol;
    private VUIColor disabledCol;

    private float borderThickness;
    private float borderRounding;
    private VUIColor borderColor;

    private int textSize;
    private float textSpacing;
    private VUIFont textFont;
    private int textAnchor;
    private VUIColor textCol;

    public static final int TEXT_ANCHOR_LEFT = 0;
    public static final int TEXT_ANCHOR_CENTER = 2;
    public static final int TEXT_ANCHOR_RIGHT = 3;

    /**
     * Create new VUI style configuration.
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
    public VUIStyle(VUIColor defaultCol_, VUIColor focusedCol_, VUIColor pressedCol_, VUIColor disabledCol_,
                    float borderThickness_, float borderRounding_, VUIColor borderColor_,
                    int textSize_, float textSpacing_, VUIFont textFont_, int textAnchor_, VUIColor textCol_) {
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
     * Create new VUI style configuration (Default).
     */
    public VUIStyle() {
        defaultCol = new VUIColor(48, 48, 48, 255);
        focusedCol = new VUIColor(60, 60, 60, 255);
        pressedCol = new VUIColor(100, 100, 100, 255);
        disabledCol = new VUIColor(132, 132, 132, 248);

        borderThickness = 1.5f;
        borderRounding = 0.3f;
        borderColor = new VUIColor(186, 186, 186, 255);

        textSize = 16;
        textSpacing = 0.5f;
        textFont = new VUIFont(resolvePath("gsdk/resources/Lato-Regular.ttf"), true, true);
        textAnchor = TEXT_ANCHOR_CENTER;
        textCol = new VUIColor(248, 248, 248, 255);
    }

    /**
     * Set default color.
     *
     * @param defaultCol_ Default color.
     */
    public void setDefaultCol(VUIColor defaultCol_) {
        defaultCol = defaultCol_;
    }

    /**
     * Get default color.
     */
    public VUIColor getDefaultCol() {
        return defaultCol;
    }

    /**
     * Set focused color.
     *
     * @param focusedCol_ Focused color.
     */
    public void setFocusedCol(VUIColor focusedCol_) {
        focusedCol = focusedCol_;
    }

    /**
     * Get focused color.
     */
    public VUIColor getFocusedCol() {
        return focusedCol;
    }

    /**
     * Set pressed color.
     *
     * @param pressedCol_ Pressed color.
     */
    public void setPressedCol(VUIColor pressedCol_) {
        pressedCol = pressedCol_;
    }

    /**
     * Get pressed color.
     */
    public VUIColor getPressedCol() {
        return pressedCol;
    }

    /**
     * Set disabled color.
     *
     * @param disabledCol_ Disabled color.
     */
    public void setDisabledCol(VUIColor disabledCol_) {
        disabledCol = disabledCol_;
    }

    /**
     * Get disabled color.
     */
    public VUIColor getDisabledCol() {
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
    public void setBorderColor(VUIColor borderColor_) {
        borderColor = borderColor_;
    }

    /**
     * Get border color.
     */
    public VUIColor getBorderColor() {
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
    public void setTextFont(VUIFont textFont_) {
        textFont = textFont_;
    }

    /**
     * Get text font.
     */
    public VUIFont getTextFont() {
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
    public void setTextCol(VUIColor textCol_) {
        textCol = textCol_;
    }

    /**
     * Get text color.
     */
    public VUIColor getTextCol() {
        return textCol;
    }
}
