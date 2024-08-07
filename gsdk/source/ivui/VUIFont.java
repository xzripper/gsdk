package gsdk.source.ivui;

import com.raylib.Raylib;

import static gsdk.source.generic.Assert.assert_f;

/**
 * VUI Font.
 */
public class VUIFont {
    private final Raylib.Font rlFont;

    /**
     * Load VUI font.
     *
     * @param fontPath Font path.
     * @param genMipmaps Generate mipmaps for texture?
     * @param bilinearFiltering Enable bilinear filtering for font?
     */
    public VUIFont(String fontPath, boolean genMipmaps, boolean bilinearFiltering) {
        rlFont = Raylib.LoadFont(fontPath);

        assert_f(Raylib.IsFontReady(rlFont), "rlFont != valid");

        if(genMipmaps) {
            Raylib.GenTextureMipmaps(rlFont.texture());
        }

        if(bilinearFiltering) {
            Raylib.SetTextureFilter(rlFont.texture(), Raylib.TEXTURE_FILTER_BILINEAR);
        }
    }

    /**
     * Get Raylib font.
     */
    public Raylib.Font getFont() {
        return rlFont;
    }
}
