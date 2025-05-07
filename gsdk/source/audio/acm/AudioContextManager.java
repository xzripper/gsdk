package gsdk.source.audio.acm;

import static com.raylib.Raylib.Sound;

/**
 * Utility for separating audio by contexts. Useful for creating variations for single audio (example: separate footsteps' sounds by a ground material: sand, water, etc).
 */
public class AudioContextManager {
    private CMAudio[] contextAudioArray;

    private String currentContext;

    /**
     * Initialize audio context manager.
     * 
     * @param defaultCtx Context used by default.
     * @param ctxAudio A list of sounds' contexts.
     */
    public AudioContextManager(String defaultCtx, CMAudio... ctxAudio) {
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
            if(ctxAudio.getName() == audioName) {
                for(CMContext audioCtx : ctxAudio.getContextArray()) {
                    if(audioCtx.getContext() == currentContext) {
                        return audioCtx.getContextSoundObject();
                    }
                }
            }
        }

        return null;
    }
}
