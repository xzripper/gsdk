package gsdk.source.audio.acm;

import static gsdk.source.generic.Assert.assert_t;

/**
 * Context Manager's audio data class.
 */
public class CMAudio {
    private final String audioName;

    private final CMContext[] ctxArray;

    /**
     * Create new CM's audio instance.
     * 
     * @param ctxAudioName The audio's name.
     * @param audioCtxs Possible audio contexts.
     */
    public CMAudio(String ctxAudioName, CMContext... audioCtxs) {
        assert_t(ctxAudioName == null || audioCtxs == null, "ctxAudioName and audioCtxs can't be null");

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
