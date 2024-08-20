package gsdk.source.grender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import static gsdk.source.generic.GLogger.info;

import static gsdk.r_utilities.PathResolver.resolvePath;

import static gsdk.source.generic.Assert.assert_f;

/**
 * GSDK Implementation of billboards; this implementation is superiour to Raylib implementation of billboard,
 * its main advantages are optional billboards frustum culling, distance scaling, builtin alpha discard shader,
 * billboard responses to vertical camera movements, simpler implementation and usage.
 */
public class GBillboard {
    private static boolean cullState = false;

    public static final Raylib.Shader DISCARD_ALPHA_SHADER;

    static {
        DISCARD_ALPHA_SHADER = Raylib.LoadShader(null, resolvePath("gsdk/shaders/discard_alpha.fs"));

        info("Loaded DISCARD_ALPHA_SHADER shader; do not forget to unload it with GBillboard::unloadDiscardAlphaShader.");
    }

    // Those values aren't random; they are most the average for plane with billboard texture
    // and work for all possible textures. Following values aren't accurate,
    // but with current technique accurate visibility detection is not possible.
    // But at least this technique has huge advantages like easy implementation and fast visibility check.
    private static final int TEX_SIZE_XMSC_CNST = 500, TEX_SIZE_DMVL_CNST = 12, DIST_SCL_CMP_VAL_CNST = 45;

    // Quite experimental, I've failed to implement frustum culling, but I guess for first time this will work.
    private static boolean billboardVisible(Raylib.Camera3D cam, Raylib.Vector3 bPos, Raylib.Vector2 bSize, float bScale, float bDist) {
        Raylib.Vector2 bSizeDAdj = new Raylib.Vector2()
                                .x((TEX_SIZE_XMSC_CNST * bScale) / bDist * TEX_SIZE_DMVL_CNST)
                                .y((TEX_SIZE_XMSC_CNST * bScale) / bDist * TEX_SIZE_DMVL_CNST);

        Raylib.Vector2 bPos2D = Raylib.GetWorldToScreen(bPos, cam);

        bPos2D.x(bPos2D.x() - bSizeDAdj.x() / 2);
        bPos2D.y(bPos2D.y() - bSizeDAdj.y() / 2);

        // Disable culling if camera is too close to a billboard.
        if((bDist * bScale) < (DIST_SCL_CMP_VAL_CNST * bScale)) return true;

        return Raylib.CheckCollisionRecs(
            new Raylib.Rectangle()
                            .x(bPos2D.x()).y(bPos2D.y())
                            .width(bSizeDAdj.x()).height(bSizeDAdj.y()),

            new Raylib.Rectangle()
                            .x(0).y(0)
                            .width(Raylib.GetScreenWidth())
                            .height(Raylib.GetScreenHeight()));
    }

    /**
     * Begin culling for next billboards.
     */
    public static void beginCulling() {
        cullState = true;
    }

    /**
     * End culling for next billboards.
     */
    public static void endCulling() {
        cullState = false;
    }

    /**
     * Is culling enabled?
     */
    public static boolean isCulling() {
        return cullState;
    }

    /**
     * Render advanced billboard.
     * 
     * @param cam3D 3D Camera.
     * @param tex Billboard texture.
     * @param bPos Billboard position.
     * @param bScale Billboard scale.
     * @param bTint Billboard tint.
     * @param bShader Billboard shader.
     * @param bDistScaling Billboard distance scaling.
     * @param bDistScalingDiv Billboard distance scaling division.
     */
    public static void drawBillboard(
        Raylib.Camera3D cam3D, Raylib.Texture tex,
        Raylib.Vector3 bPos, float bScale,
        Raylib.Color bTint, Raylib.Shader bShader,
        boolean bDistScaling, float bDistScalingDiv
    ) {
        assert_f(Raylib.IsTextureReady(tex), "tex != valid (billboard)");

        float bDist = Raylib.Vector3Distance(cam3D._position(), bPos) / bDistScalingDiv;

        if(cullState) {
            if(!billboardVisible(
                cam3D, bPos, new Raylib.Vector2()
                .x(tex.width()).y(tex.height()), bScale, bDist)) { return; }
        }

        bScale *= bDistScaling ? bDist : 1;

        Raylib.Vector3 direction = Raylib.Vector3Normalize(Raylib.Vector3Subtract(cam3D._position(), bPos));

        Raylib.Vector3 right = Raylib.
                Vector3Scale(Raylib.
                    Vector3Normalize(Raylib.
                        Vector3CrossProduct(cam3D.up(), direction)), bScale);

        Raylib.Vector3 up = Raylib.
                Vector3Scale(Raylib.
                    Vector3Normalize(Raylib.
                        Vector3CrossProduct(direction, right)), bScale);

        Raylib.Vector3 point1 = Raylib.Vector3Subtract(Raylib.Vector3Subtract(bPos, right), up);
        Raylib.Vector3 point2 = Raylib.Vector3Subtract(Raylib.Vector3Add(bPos, right), up);
        Raylib.Vector3 point3 = Raylib.Vector3Add(Raylib.Vector3Add(bPos, right), up);
        Raylib.Vector3 point4 = Raylib.Vector3Add(Raylib.Vector3Subtract(bPos, right), up);

        if(bShader != null) Raylib.BeginShaderMode(bShader);

        Raylib.rlSetTexture(tex.id());

        Raylib.rlBegin(Raylib.RL_QUADS);

        Raylib.rlColor4ub(bTint.r(), bTint.g(), bTint.b(), bTint.a());

        Raylib.rlTexCoord2f(0.0f, 1.0f);
        Raylib.rlVertex3f(point1.x(), point1.y(), point1.z());

        Raylib.rlTexCoord2f(1.0f, 1.0f);
        Raylib.rlVertex3f(point2.x(), point2.y(), point2.z());

        Raylib.rlTexCoord2f(1.0f, 0.0f);
        Raylib.rlVertex3f(point3.x(), point3.y(), point3.z());

        Raylib.rlTexCoord2f(0.0f, 0.0f);
        Raylib.rlVertex3f(point4.x(), point4.y(), point4.z());

        Raylib.rlEnd();

        Raylib.rlSetTexture(0);

        if(bShader != null) Raylib.EndShaderMode();
    }

    /**
     * Render advanced billboard with distance scaling division value set to 1.
     * 
     * @param cam3D 3D Camera.
     * @param tex Billboard texture.
     * @param bPos Billboard position.
     * @param bScale Billboard scale.
     * @param bTint Billboard tint.
     * @param bShader Billboard shader.
     * @param bDistScaling Billboard distance scaling.
     */
    public static void drawBillboard(
        Raylib.Camera3D cam3D, Raylib.Texture tex,
        Raylib.Vector3 bPos, float bScale,
        Raylib.Color bTint, Raylib.Shader bShader,
        boolean bDistScaling
    ) {
        drawBillboard(cam3D, tex, bPos, bScale, bTint, bShader, bDistScaling, 1);
    }

    /**
     * Render advanced billboard with distance scaling division value set to 1 and distance scaling disabled.
     * 
     * @param cam3D 3D Camera.
     * @param tex Billboard texture.
     * @param bPos Billboard position.
     * @param bScale Billboard scale.
     * @param bTint Billboard tint.
     * @param bShader Billboard shader.
     */
    public static void drawBillboard(
        Raylib.Camera3D cam3D, Raylib.Texture tex,
        Raylib.Vector3 bPos, float bScale,
        Raylib.Color bTint, Raylib.Shader bShader
    ) {
        drawBillboard(cam3D, tex, bPos, bScale, bTint, bShader, false);
    }

    /**
     * Render advanced billboard with distance scaling division value set to 1 and distance scaling disabled and shader disabled.
     * 
     * @param cam3D 3D Camera.
     * @param tex Billboard texture.
     * @param bPos Billboard position.
     * @param bScale Billboard scale.
     * @param bTint Billboard tint.
     */
    public static void drawBillboard(
        Raylib.Camera3D cam3D, Raylib.Texture tex,
        Raylib.Vector3 bPos, float bScale,
        Raylib.Color bTint
    ) {
        drawBillboard(cam3D, tex, bPos, bScale, bTint, DISCARD_ALPHA_SHADER);
    }

    /**
     * Render advanced billboard with distance scaling division value set to 1 and distance scaling disabled and shader disabled and white tint.
     * 
     * @param cam3D 3D Camera.
     * @param tex Billboard texture.
     * @param bPos Billboard position.
     * @param bScale Billboard scale.
     */
    public static void drawBillboard(
        Raylib.Camera3D cam3D, Raylib.Texture tex,
        Raylib.Vector3 bPos, float bScale
    ) {
        drawBillboard(cam3D, tex, bPos, bScale, WHITE);
    }

    /**
     * Render advanced billboard with distance scaling division value set to 1 and distance scaling disabled,
     * and shader disabled and white tint, and default 1 scale.
     * 
     * @param cam3D 3D Camera.
     * @param tex Billboard texture.
     * @param bPos Billboard position.
     */
    public static void drawBillboard(
        Raylib.Camera3D cam3D, Raylib.Texture tex, Raylib.Vector3 bPos
    ) {
        drawBillboard(cam3D, tex, bPos, 1.0f);
    }

    /**
     * Unload discard alpha shader.
     */
    public static void unloadDiscardAlphaShader() {
        Raylib.UnloadShader(DISCARD_ALPHA_SHADER);
    }
}
