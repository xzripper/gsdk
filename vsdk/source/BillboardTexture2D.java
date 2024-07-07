package vsdk.source;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import static com.raylib.Jaylib.BLANK;

import static vsdk.source.Assert.assert_f;

/**
 * Makes any Raylib drawable object turn into billboard.
 */
public class BillboardTexture2D {
    private final Raylib.RenderTexture billboardTex;

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

        Raylib.BeginTextureMode(billboardTex);

        Raylib.ClearBackground(backgroundCol);

        drawFunc.run();

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
     * Unload render texture.
     */
    public void unload() {
        Raylib.UnloadRenderTexture(billboardTex);
    }
}
