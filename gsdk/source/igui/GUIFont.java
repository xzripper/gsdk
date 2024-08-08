package gsdk.source.igui;

import com.raylib.Raylib;

import static gsdk.source.generic.Assert.assert_f;

/**
 * GUI Font.
 */
public class GUIFont {
    private final Raylib.Font rlFont;

    /**
     * Load GUI font.
     *
     * @param fontPath Font path.
     * @param genMipmaps Generate mipmaps for texture?
     * @param bilinearFiltering Enable bilinear filtering for font?
     */
    public GUIFont(String fontPath, boolean genMipmaps, boolean bilinearFiltering) {
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
