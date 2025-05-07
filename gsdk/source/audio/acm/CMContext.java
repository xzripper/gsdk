package gsdk.source.audio.acm;

import static com.raylib.Raylib.Sound;

/**
 * Context Manager's context data class.
 */
public class CMContext {
    private String context;

    private Sound contextSoundObject;

    /**
     * Create new CM's context instance.
     * 
     * @param audioCtx Audio context (string).
     * @param audioCtxObject Raylib's audio object.
     */
    public CMContext(String audioCtx, Sound audioCtxObject) {
        context = audioCtx;

        contextSoundObject = audioCtxObject;
    }

    /**
     * Get audio's context.
     */
    public String getContext() {
        return context;
    }

    /**
     * Get context's sound object.
     */
    public Sound getContextSoundObject() {
        return contextSoundObject;
    }
}
