package gsdk.source.vrender;

import com.raylib.Raylib;

import org.bytedeco.javacpp.FloatPointer;

import static gsdk.source.generic.Assert.assert_f;

import static gsdk.r_utilities.PathResolver.resolvePath;

/**
 * Raylib Bump Map (ALPHA).
 */
public class BumpMap {
    private final Raylib.Texture albedoTex;
    private final Raylib.Texture bumpMapTex;

    private final Raylib.Material bumpMaterial = Raylib.LoadMaterialDefault();

    private final Raylib.Shader bumpShader = Raylib.LoadShader(resolvePath("gsdk/shaders/vbump.vs"), resolvePath("gsdk/shaders/fbump.fs"));

    private int bumpShaderLightPosLoc;
    private int bumpShaderViewPosLoc;

    private int bumpShaderSpecularShininessLoc;
    private int bumpShaderAmbientValueLoc;

    /**
     * Initialize bump map.
     *
     * @param albedoTexP Path to albedo texture.
     * @param bumpMapTexP Path to normal (bump map) texture.
     * @param lightPos Light position (3D).
     * @param viewPos View position (3D).
     * @param specularShininess Specular shininess.
     * @param ambientValue Ambient color/value?
     * @param anisotropicFiltering Enable anisotropic filtering for textures.
     */
    public BumpMap(String albedoTexP, String bumpMapTexP, float[] lightPos, float[] viewPos, float specularShininess, float ambientValue, boolean anisotropicFiltering) {
        albedoTex = Raylib.LoadTexture(albedoTexP);
        bumpMapTex = Raylib.LoadTexture(bumpMapTexP);

        assert_f(Raylib.IsTextureReady(albedoTex), "albedoTex != valid");
        assert_f(Raylib.IsTextureReady(bumpMapTex), "bumpMapTex != valid");

        if(anisotropicFiltering) {
            Raylib.SetTextureFilter(albedoTex, Raylib.TEXTURE_FILTER_ANISOTROPIC_16X);
            Raylib.SetTextureFilter(bumpMapTex, Raylib.TEXTURE_FILTER_ANISOTROPIC_16X);
        }

        bumpMaterial.maps().position(Raylib.MATERIAL_MAP_ALBEDO).texture(albedoTex);
        bumpMaterial.maps().position(Raylib.MATERIAL_MAP_NORMAL).texture(bumpMapTex);

        int albedoTexLoc = Raylib.GetShaderLocation(bumpShader, "texture0");
        int bumpTexLoc = Raylib.GetShaderLocation(bumpShader, "bumpMap");

        assert_f(albedoTexLoc != -1, "albedoTexLoc == -1");
        assert_f(bumpTexLoc != -1, "bumpTexLoc == -1");

        Raylib.SetShaderValueTexture(bumpShader, albedoTexLoc, albedoTex);
        Raylib.SetShaderValueTexture(bumpShader, bumpTexLoc, bumpMapTex);

        bumpShaderLightPosLoc = Raylib.GetShaderLocation(bumpShader, "lightPos");
        bumpShaderViewPosLoc = Raylib.GetShaderLocation(bumpShader, "viewPos");

        assert_f(bumpShaderLightPosLoc != -1, "bumpShaderLightPosLoc == -1");
        assert_f(bumpShaderViewPosLoc != -1, "bumpShaderViewPosLoc == -1");

        Raylib.SetShaderValue(bumpShader, bumpShaderLightPosLoc, new FloatPointer(lightPos), Raylib.SHADER_UNIFORM_VEC3);
        Raylib.SetShaderValue(bumpShader, bumpShaderViewPosLoc, new FloatPointer(viewPos), Raylib.SHADER_UNIFORM_VEC3);

        bumpShaderSpecularShininessLoc = Raylib.GetShaderLocation(bumpShader, "specularShininess");
        bumpShaderAmbientValueLoc = Raylib.GetShaderLocation(bumpShader, "ambientValue");

        assert_f(bumpShaderSpecularShininessLoc != -1, "bumpShaderSpecularShininessLoc == -1");
        assert_f(bumpShaderAmbientValueLoc != -1, "bumpShaderAmbientValueLoc == -1");

        Raylib.SetShaderValue(bumpShader, bumpShaderSpecularShininessLoc, new FloatPointer(specularShininess), Raylib.SHADER_UNIFORM_FLOAT);
        Raylib.SetShaderValue(bumpShader, bumpShaderAmbientValueLoc, new FloatPointer(ambientValue), Raylib.SHADER_UNIFORM_FLOAT);

        bumpMaterial.shader(bumpShader);
    }

    /**
     * Update light position.
     *
     * @param lightPos Light position (3D).
     */
    public void updLightPos(float[] lightPos) {
        assert_f(bumpShaderLightPosLoc != -1, "bumpShaderLightPosLoc == -1");

        Raylib.SetShaderValue(bumpShader, bumpShaderLightPosLoc, new FloatPointer(lightPos), Raylib.SHADER_UNIFORM_VEC3);
    }

    /**
     * Update view position.
     *
     * @param viewPos View position (3D).
     */
    public void updViewPos(float[] viewPos) {
        assert_f(bumpShaderViewPosLoc != -1, "bumpShaderViewPosLoc == -1");

        Raylib.SetShaderValue(bumpShader, bumpShaderViewPosLoc, new FloatPointer(viewPos), Raylib.SHADER_UNIFORM_VEC3);
    }

    /**
     * Update specular shininess.
     *
     * @param specularShininess Specular shininess.
     */
    public void updSpecularShininess(float specularShininess) {
        assert_f(bumpShaderSpecularShininessLoc != -1, "bumpShaderSpecularShininessLoc == -1");

        Raylib.SetShaderValue(bumpShader, bumpShaderSpecularShininessLoc, new FloatPointer(specularShininess), Raylib.SHADER_UNIFORM_VEC3);
    }

    /**
     * Update ambient value.
     *
     * @param ambientValue Ambient value.
     */
    public void updAmbientValue(float ambientValue) {
        assert_f(bumpShaderAmbientValueLoc != -1, "bumpShaderAmbientValueLoc == -1");

        Raylib.SetShaderValue(bumpShader, bumpShaderAmbientValueLoc, new FloatPointer(ambientValue), Raylib.SHADER_UNIFORM_VEC3);
    }

    /**
     * Get albedo texture.
     */
    public Raylib.Texture getAlbedoTex() {
        return albedoTex;
    }

    /**
     * Get bump map texture.
     */
    public Raylib.Texture getBumpMapTex() {
        return bumpMapTex;
    }

    /**
     * Get bump map material.
     */
    public Raylib.Material getBumpMaterial() {
        return bumpMaterial;
    }

    /**
     * Get bump map shader.
     */
    public Raylib.Shader getBumpShader() {
        return bumpShader;
    }

    /**
     * Get bump map shader light position uniform location.
     */
    public int getBumpShaderLightPosLoc() {
        return bumpShaderLightPosLoc;
    }

    /**
     * Get bump map shader view position uniform location.
     */
    public int getBumpShaderViewPosLoc() {
        return bumpShaderViewPosLoc;
    }

    /**
     * Get bump map shader specular shininess uniform location.
     */
    public int getBumpShaderSpecularShininessLoc() {
        return bumpShaderSpecularShininessLoc;
    }

    /**
     * Get bump map shader ambient value uniform location.
     */
    public int getBumpShaderAmbientValueLoc() {
        return bumpShaderAmbientValueLoc;
    }

    /**
     * Unload everything.
     */
    public void unloadBumpMap() {
        Raylib.UnloadTexture(albedoTex);
        Raylib.UnloadTexture(bumpMapTex);

        Raylib.UnloadMaterial(bumpMaterial);

        Raylib.UnloadShader(bumpShader);

        bumpShaderLightPosLoc = -1;
        bumpShaderViewPosLoc = -1;

        bumpShaderSpecularShininessLoc = -1;
        bumpShaderAmbientValueLoc = -1;
    }
}
