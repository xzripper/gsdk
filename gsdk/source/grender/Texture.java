package gsdk.source.grender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import gsdk.source.vectors.Vector2Di;

import static gsdk.source.generic.Range.inRange;

import static gsdk.source.generic.Assert.assert_f;

/**
 * Helper class for Raylib.Texture.
 */
public class Texture {
    private final Raylib.Texture tex;

    private int texFilter;
    private int texWrap;

    public static final int TEX_FILTER_POINT = 0;
    public static final int TEX_FILTER_BILINEAR = 1;
    public static final int TEX_FILTER_TRILINEAR = 2;
    public static final int TEX_FILTER_ANISOTROPIC_4X = 3;
    public static final int TEX_FILTER_ANISOTROPIC_8X = 4;
    public static final int TEX_FILTER_ANISOTROPIC_16X = 5;

    public static final int TEX_WRAP_REPEAT = 0;
    public static final int TEX_WRAP_CLAMP = 1;
    public static final int TEX_WRAP_MIRROR_REPEAT = 2;
    public static final int TEX_WRAP_MIRROR_CLAMP = 3;

    private Raylib.Color tint = WHITE;

    /**
     * Load texture.
     *
     * @param texPath Texture path.
     */
    public Texture(String texPath) {
        tex = Raylib.LoadTexture(texPath);
    }

    /**
     * Load texture.
     *
     * @param texPath Texture path.
     * @param texFilter_ Texture filter.
     */
    public Texture(String texPath, int texFilter_) {
        tex = Raylib.LoadTexture(texPath);

        assert_f(validTexFilter(texFilter_), "invalid texture filter");

        Raylib.SetTextureFilter(tex, texFilter_);

        texFilter = texFilter_;
    }

    /**
     * Load texture.
     *
     * @param texPath Texture path.
     * @param texFilter_ Texture filter.
     * @param texWrap_ Texture wrap.
     */
    public Texture(String texPath, int texFilter_, int texWrap_) {
        tex = Raylib.LoadTexture(texPath);

        assert_f(validTexFilter(texFilter_), "invalid texture filter");
        assert_f(validTexWrap(texWrap_), "invalid texture wrap");

        Raylib.SetTextureFilter(tex, texFilter_);
        Raylib.SetTextureWrap(tex, texWrap_);

        texFilter = texFilter_;
        texWrap = texWrap_;
    }

    /**
     * Load texture from image.
     *
     * @param texImg Image.
     */
    public Texture(Raylib.Image texImg) {
        tex = Raylib.LoadTextureFromImage(texImg);
    }

    /**
     * Load texture from image.
     *
     * @param texImg Image.
     * @param texFilter_ Texture filter.
     */
    public Texture(Raylib.Image texImg, int texFilter_) {
        tex = Raylib.LoadTextureFromImage(texImg);

        assert_f(validTexFilter(texFilter_), "invalid texture filter");

        Raylib.SetTextureFilter(tex, texFilter_);

        texFilter = texFilter_;
    }

    /**
     * Load texture from image.
     *
     * @param texImg Image.
     * @param texFilter_ Texture filter.
     * @param texWrap_ Texture wrap.
     */
    public Texture(Raylib.Image texImg, int texFilter_, int texWrap_) {
        tex = Raylib.LoadTextureFromImage(texImg);

        assert_f(validTexFilter(texFilter_), "invalid texture filter");
        assert_f(validTexWrap(texWrap_), "invalid texture wrap");

        Raylib.SetTextureFilter(tex, texFilter_);
        Raylib.SetTextureWrap(tex, texWrap_);

        texFilter = texFilter_;
        texWrap = texWrap_;
    }

    /**
     * Set texture filter.
     *
     * @param filter Filter.
     */
    public void setTexFilter(int filter) {
        assert_f(valid(), "texture != valid");

        assert_f(validTexFilter(filter), "invalid texture filter");

        Raylib.SetTextureFilter(tex, filter);

        texFilter = filter;
    }

    /**
     * Get texture filter.
     */
    public int getTexFilter() {
        return texFilter;
    }

    /**
     * Set texture wrap.
     *
     * @param wrap Wrap.
     */
    public void setTexWrap(int wrap) {
        assert_f(valid(), "texture != valid");

        assert_f(validTexWrap(wrap), "invalid texture wrap");

        Raylib.SetTextureWrap(tex, wrap);

        texWrap = wrap;
    }

    /**
     * Get texture wrap.
     */
    public int getTexWrap() {
        return texWrap;
    }

    /**
     * Set model texture.
     *
     * @param model Model.
     * @param map Material map.
     */
    public void setMapTex(Raylib.Model model, int map) {
        assert_f(validMap(map), "invalid material map");

        model.materials().maps().position(map).texture(tex);
    }

    /**
     * Set texture tint.
     *
     * @param tint_ Tint color.
     */
    public void setTexTint(Raylib.Color tint_) {
        tint = tint_;
    }

    /**
     * Get texture tint.
     */
    public Raylib.Color getTexTint() {
        return tint;
    }

    /**
     * Use user tint or texture tint.
     *
     * @param uTint User tint.
     */
    public Raylib.Color orTexTint(Raylib.Color uTint) {
        return uTint == null ? tint : uTint;
    }

    /**
     * Get texture width.
     */
    public int getTexWidth() {
        return tex.width();
    }

    /**
     * Get texture height.
     */
    public int getTexHeight() {
        return tex.height();
    }

    /**
     * Get texture size.
     */
    public Vector2Di getTexSize() {
        return new Vector2Di(getTexWidth(), getTexHeight());
    }

    /**
     * Get texture.
     */
    public Raylib.Texture getTex() {
        return tex;
    }

    /**
     * Is texture valid.
     */
    public boolean valid() {
        return Raylib.IsTextureReady(tex);
    }

    /**
     * Unload texture.
     */
    public void unload() {
        Raylib.UnloadTexture(tex);
    }

    /**
     * Is given texture filter valid.

     * @param texFilter Texture filter.
     */
    public static boolean validTexFilter(int texFilter) {
        return inRange(texFilter, TEX_FILTER_POINT, TEX_FILTER_ANISOTROPIC_16X);
    }

    /**
     * Is given texture wrap valid.
     *
     * @param texWrap Texture wrap.
     */
    public static boolean validTexWrap(int texWrap) {
        return inRange(texWrap, TEX_WRAP_REPEAT, TEX_WRAP_MIRROR_CLAMP);
    }

    /**
     * Is given material map valid.
     *
     * @param map Material map.
     */
    public static boolean validMap(int map) {
        return inRange(map, Raylib.MATERIAL_MAP_DIFFUSE, Raylib.MATERIAL_MAP_BRDF);
    }
}
