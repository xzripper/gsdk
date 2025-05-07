package gsdk.source.audio.acm;

/**
 * Context Manager's audio data class.
 */
public class CMAudio {
    private String audioName;

    private CMContext[] ctxArray;

    /**
     * Create new CM's audio instance.
     * 
     * @param ctxAudioName The audio's name.
     * @param audioCtxs Possible audio contexts.
     */
    public CMAudio(String ctxAudioName, CMContext... audioCtxs) {
        audioName = ctxAudioName;

        ctxArray = audioCtxs;
    }

    /**
     * Get audio name.
     */
    public String getName() {
        return audioName;
    }

    /**
     * Get audio context list.
     */
    public CMContext[] getContextArray() {
        return ctxArray;
    }
}
