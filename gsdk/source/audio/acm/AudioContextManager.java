package gsdk.source.audio.acm;

import static com.raylib.Raylib.Sound;

import static gsdk.source.generic.Assert.assert_t;

/**
 * Utility for separating audio by contexts. Useful for creating variations for single audio (example: separate footsteps' sounds by a ground material: sand, water, etc).
 */
public class AudioContextManager {
    private final CMAudio[] contextAudioArray;

    private String currentContext;

    /**
     * Initialize audio context manager.
     * 
     * @param defaultCtx Context used by default.
     * @param ctxAudio A list of sounds' contexts.
     */
    public AudioContextManager(String defaultCtx, CMAudio... ctxAudio) {
        assert_t(defaultCtx == null || ctxAudio == null, "defaultCtx and ctxAudio can't be null");

        currentContext = defaultCtx;

        contextAudioArray = ctxAudio;
    }

    /**
     * Set current context.
     * 
     * @param context Current context.
     */
    public void setContext(String context) {
        currentContext = context;
    }

    /**
     * Get current context.
     */
    public String getContext() {
        return currentContext;
    }

    /**
     * Get audio in its context. Returns NULL if audio can not be found. 
     * 
     * @param audioName Audio name to look for.
     */
    public Sound getContextAudio(String audioName) {
        for(CMAudio ctxAudio : contextAudioArray) {
            if(ctxAudio.getName().equals(audioName)) {
                for(CMContext audioCtx : ctxAudio.getContextArray()) {
                    if(audioCtx.getContext().equals(currentContext)) {
                        return audioCtx.getContextSoundObject();
                    }
                }
            }
        }

        return null;
    }
}
