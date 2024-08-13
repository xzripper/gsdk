package gsdk.source.grender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import static com.raylib.Jaylib.BLANK;

import gsdk.source.generic.GTexture;

import gsdk.source.vectors.Vector3Df;

import static gsdk.source.generic.Assert.assert_f;

/**
 * Makes any Raylib drawable object turn into billboard.
 */
public class BillboardTexture2D {
    private final Raylib.RenderTexture billboardTex;

    private Runnable _drawFunc;

    private Raylib.Color backgroundCol;

    /**
     * Initialize billboard texture.
     *
     * @param width Texture width.
     * @param height Texture height.
     * @param background Background color.
     */
    public BillboardTexture2D(int width, int height, Raylib.Color background) {
        billboardTex = Raylib.LoadRenderTexture(width, height);

        backgroundCol = background;
    }

    private BillboardTexture2D(int width, int height, Raylib.Color background, Runnable drawFunc) {
        billboardTex = Raylib.LoadRenderTexture(width, height);

        backgroundCol = background;

        _drawFunc = drawFunc;
    }

    /**
     * Initialize billboard texture with blank background.
     *
     * @param width Texture width.
     * @param height Texture height.
     */
    public BillboardTexture2D(int width, int height) {
        billboardTex = Raylib.LoadRenderTexture(width, height);

        backgroundCol = BLANK;
    }

    /**
     * Initialize billboard texture with screen size.
     *
     * @param background Background color.
     */
    public BillboardTexture2D(Raylib.Color background) {
        billboardTex = Raylib.LoadRenderTexture(Raylib.GetScreenWidth(), Raylib.GetScreenHeight());

        backgroundCol = background;
    }

    /**
     * Initialize billboard texture with screen size and blank background.
     */
    public BillboardTexture2D() {
        billboardTex = Raylib.LoadRenderTexture(Raylib.GetScreenWidth(), Raylib.GetScreenHeight());

        backgroundCol = BLANK;
    }

    /**
     * Draw objects into billboard texture.
     *
     * @param drawFunc Drawer function.
     */
    public void draw(Runnable drawFunc) {
        assert_f(Raylib.IsRenderTextureReady(billboardTex), "billboardTex != valid");

        assert_f(_drawFunc == null, "_drawFunc != null: remove billboard shortcuts or use draw(void)!");

        Raylib.BeginTextureMode(billboardTex);

        Raylib.ClearBackground(backgroundCol);

        drawFunc.run();

        Raylib.EndTextureMode();
    }

    /**
     * Draw objects into billboard texture.
     * Use this method if shortcuts like `billboardText` or `billboardTexture` was used.
     */
    public void draw() {
        assert_f(Raylib.IsRenderTextureReady(billboardTex), "billboardTex != valid");

        assert_f(_drawFunc != null, "_drawFunc == null: draw(void) designated only for billboards with shortcuts!");

        Raylib.BeginTextureMode(billboardTex);

        Raylib.ClearBackground(backgroundCol);

        _drawFunc.run();

        Raylib.EndTextureMode();
    }

    /**
     * Render billboard.
     *
     * @param cam 3D Camera.
     * @param pos 3D Billboard position.
     * @param size Billboard size.
     * @param tint Billboard tint.
     */
    public void render(Raylib.Camera3D cam, Vector3Df pos, float size, Raylib.Color tint) {
        assert_f(Raylib.IsRenderTextureReady(billboardTex), "billboardTex != valid");

        Raylib.DrawBillboardRec(
            cam, billboardTex.texture(),
            new Raylib.Rectangle().x(0).y(0)
                .width(billboardTex.texture().width())
                .height(-billboardTex.texture().height()),
            pos.toRlVec(), new Raylib.Vector2().x(size).y(size), tint);
    }

    /**
     * Render billboard with white tint.
     *
     * @param cam 3D Camera.
     * @param pos 3D Billboard position.
     * @param size Billboard size.
     */
    public void render(Raylib.Camera3D cam, Vector3Df pos, float size) {
        render(cam, pos, size, WHITE);
    }

    /**
     * Render billboard with white tint.
     *
     * @param cam 3D Camera.
     * @param pos 3D Billboard position.
     */
    public void render(Raylib.Camera3D cam, Vector3Df pos) {
        render(cam, pos, 1.0f, WHITE);
    }

    /**
     * Get billboard render texture.
     */
    public Raylib.RenderTexture getBillboardTex() {
        return billboardTex;
    }

    /**
     * Set background color.
     *
     * @param color Background color.
     */
    public void setBackgroundCol(Raylib.Color color) {
        backgroundCol = color;
    }

    /**
     * Get background color.
     */
    public Raylib.Color getBackgroundCol() {
        return backgroundCol;
    }

    /**
     * Shortcut for text billboard.
     * Initializes billboard with text size, and other parameters.
     *
     * @param font Font (null if default font).
     * @param text Text.
     * @param size Text size.
     * @param spacing Spacing (-1 if default spacing).
     * @param color Text color.
     * @param background Text background.
     */
    public static BillboardTexture2D billboardText(Raylib.Font font, String text, float size, float spacing, Raylib.Color color, Raylib.Color background) {
        Raylib.Vector2 textSize = Raylib.MeasureTextEx(font == null ? Raylib.GetFontDefault() : font, text, size, spacing == -1 ? size / 10 : spacing);

        return new BillboardTexture2D(
            (int) textSize.x(),
            (int) textSize.y(), background,

            () -> Raylib.DrawTextEx(
                font == null ? Raylib.GetFontDefault() : font, text,
                new Raylib.Vector2().x(0).y(0),
                size, spacing == -1 ? size / 10 : spacing, color
            ));
    }

    /**
     * Shortcut for text billboard.
     * Initializes billboard with text size, and other parameters.
     *
     * @param text Text.
     * @param size Text size.
     * @param color Text color.
     * @param background Text background.
     */
    public static BillboardTexture2D billboardText(String text, float size, Raylib.Color color, Raylib.Color background) {
        return billboardText(null, text, size, -1, color, background);
    }

    /**
     * Shortcut for text billboard.
     * Initializes billboard with text size, and other parameters.
     *
     * @param text Text.
     * @param size Text size.
     * @param color Text color.
     */
    public static BillboardTexture2D billboardText(String text, float size, Raylib.Color color) {
        return billboardText(null, text, size, -1, color, BLANK);
    }

    /**
     * Shortcut for texture billboard.
     * Initialized billboard with texture size, and other parameters.
     *
     * @param tex Texture.
     * @param tint Texture tint.
     * @param background Texture background.
     */
    public static BillboardTexture2D billboardTexture(GTexture tex, Raylib.Color tint, Raylib.Color background) {
        return new BillboardTexture2D(
            tex.getTexWidth(), tex.getTexHeight(), background,

            () -> Raylib.DrawTexture(tex.getTex(), 0, 0, tex.orTexTint(tint)));
    }

    /**
     * Shortcut for texture billboard.
     * Initialized billboard with texture size, and other parameters.
     *
     * @param tex Texture.
     * @param tint Texture tint.
     */
    public static BillboardTexture2D billboardTexture(GTexture tex, Raylib.Color tint) {
        return billboardTexture(tex, tint, BLANK);
    }

    /**
     * Shortcut for texture billboard.
     * Initialized billboard with texture size, and other parameters.
     *
     * @param tex Texture.
     */
    public static BillboardTexture2D billboardTexture(GTexture tex) {
        return billboardTexture(tex, null, BLANK);
    }

    /**
     * Unload render texture.
     */
    public void unload() {
        Raylib.UnloadRenderTexture(billboardTex);
    }
}
