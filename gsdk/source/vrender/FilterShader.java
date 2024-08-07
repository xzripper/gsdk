package gsdk.source.vrender;

import com.raylib.Raylib;

import static com.raylib.Jaylib.WHITE;

import gsdk.source.utils.VShader;

/**
 * Filter Shader or Post Processing Shader simplified.
 */
public class FilterShader {
    private final Raylib.RenderTexture filterTex;

    private final VShader filterShader;

    /**
     * Initialize filter.
     *
     * @param areaW Filter area width.
     * @param areaH Filter area height.
     * @param filterShader_ Filter shader.
     */
    public FilterShader(int areaW, int areaH, VShader filterShader_) {
        filterTex = Raylib.LoadRenderTexture(areaW, areaH);

        filterShader = filterShader_;
    }

    /**
     * Get filter texture.
     */
    public Raylib.RenderTexture getFilterTex() {
        return filterTex;
    }

    /**
     * Get filter shader.
     */
    public VShader getFilterShader() {
        return filterShader;
    }

    /**
     * Begin filter.
     */
    public void beginFilter() {
        Raylib.BeginTextureMode(filterTex);
    }

    /**
     * End filter.
     */
    public void endFilter() {
        Raylib.EndTextureMode();
    }

    /**
     * Render filter and all content in it.
     */
    public void renderFilter() {
        filterShader.begin();

        Raylib.DrawTextureRec(
            filterTex.texture(), new Raylib.Rectangle()
                .x(0).y(0)
                .width((float) filterTex.texture().width()).height((float) -filterTex.texture().height()),
            new Raylib.Vector2().x(0).y(0), WHITE);

        filterShader.end();
    }

    /**
     * Unload everything.
     */
    public void unloadFilter() {
        Raylib.UnloadRenderTexture(filterTex);

        filterShader.unload();
    }
}
