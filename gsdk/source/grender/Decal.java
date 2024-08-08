package gsdk.source.grender;

import com.raylib.Raylib;

import gsdk.source.vectors.Vector3Df;

import static gsdk.source.generic.GMath.PI;

import static gsdk.source.generic.Assert.assert_f;

/**
 * 3D Plane Decal.
 */
public class Decal {
    private final Raylib.Model decalPlane;

    private Texture decalTex;

    /**
     * Create new decal.
     *
     * @param decalTex_ Decal texture.
     * @param decalWidth Decal width.
     * @param decalHeight Decal height.
     * @param segments Decal segments (can be 1 by default).
     */
    public Decal(Texture decalTex_, float decalWidth, float decalHeight, int segments) {
        decalPlane = Raylib.LoadModelFromMesh(Raylib.GenMeshPlane(decalWidth, decalHeight, segments, segments));

        decalPlane.transform(Raylib.MatrixRotateX((float) -PI / 2));

        decalTex = decalTex_;

        assert_f(decalTex.valid(), "decalTex != valid");

        decalTex.setMapTex(decalPlane, Raylib.MATERIAL_MAP_DIFFUSE);
    }

    /**
     * Render decal.
     *
     * @param dPos Decal position.
     * @param dSize Decal size (can be 1.0f by default).
     * @param dTint Decal tint.
     */
    public void render(Vector3Df dPos, float dSize, Raylib.Color dTint) {
        Raylib.DrawModel(decalPlane, dPos.toRlVec(), dSize, dTint);
    }

    /**
     * Get decal plane.
     */
    public Raylib.Model getDecal() {
        return decalPlane;
    }

    /**
     * Set decal texture.
     *
     * @param dTex Decal texture.
     */
    public void setDecalTex(Texture dTex) {
        assert_f(dTex.valid(), "dTex != valid");

        assert_f(Raylib.IsModelReady(decalPlane), "decalPlane != valid");

        decalTex = dTex;

        decalTex.setMapTex(decalPlane, Raylib.MATERIAL_MAP_DIFFUSE);
    }

    /**
     * Get decal texture.
     */
    public Texture getDecalTex() {
        return decalTex;
    }

    /**
     * Unload decal.
     */
    public void unloadDecal() {
        Raylib.UnloadModel(decalPlane);

        decalTex.unload();
    }
}
