package vsdk.source.audio;

import com.raylib.Raylib;

import vsdk.source.vectors.Vector3Df;

import static vsdk.source.utils.Range.inRange;

import static vsdk.source.utils.Assert.assert_t;

/**
 * Spatial Audio Data Class.
 */
public class SpatialAudioData {
    private final Raylib.Sound audio;

    private Vector3Df audioPos;

    private float currVol;

    private float currPan;

    private float aLoudness;

    private float aPanSens;

    /**
     * Start volume.
     */
    public static final float START_VOL = 1.0f;

    /**
     * Minimal audio volume.
     */
    public static final float MIN_VOL = 0.0f;

    /**
     * Maximal audio volume.
     */
    public static final float MAX_VOL = 1.0f;

    /**
     * Start audio pan.
     */
    public static final float START_PAN = 0.5f;

    /**
     * Right pan constant.
     */
    public static final float RIGHT_PAN = 1.0f;

    /**
     * Center pan constant.
     */
    public static final float CENTER_PAN = 0.5f;

    /**
     * Left pan constant.
     */
    public static final float LEFT_PAN = 0.0f;

    /**
     * Default audio loudness.
     */
    public static final float DEFAULT_LOUDNESS = 0.1f;

    /**
     * Default audio pan.
     */
    public static final float DEFAULT_PSENS = 0.1f;

    /**
     * Initialize Spatial Audio Data.
     *
     * @param audioP Path to audio.
     * @param audioPos_ Audio position in 3D world.
     * @param startVol Start volume.
     * @param startPan Start pan.
     * @param loudness Audio loudness (can be 0.1 by default).
     * @param pSens Pan sensitivity (can be 0.1 by default).
     */
    public SpatialAudioData(String audioP, Vector3Df audioPos_, float startVol, float startPan, float loudness, float pSens) {
        assert_t(inRange(startVol, MIN_VOL, MAX_VOL), "startVol is not in expected range: 0.0f->1.0f");
        assert_t(inRange(startPan, LEFT_PAN, RIGHT_PAN), "startPan is not in expected range: 0.0f->1.0f");

        audio = Raylib.LoadSound(audioP);

        audioPos = audioPos_;

        currVol = startVol;

        currPan = startPan;

        aLoudness = loudness;

        aPanSens = pSens;
    }

    /**
     * Get audio.
     */
    public Raylib.Sound getAudio() {
        return audio;
    }

    /**
     * Set audio position in 3D world.
     */
    public void setAudioPos(Vector3Df audioPos_) {
        audioPos = audioPos_;
    }

    /**
     * Get audio position in 3D world.
     */
    public Vector3Df getAudioPos() {
        return audioPos;
    }

    /**
     * Get audio current volume.
     *
     * @param newVal New volume.
     */
    public void setAudioCurrVol(float newVal) {
        currVol = newVal;
    }

    /**
     * Get audio current volume.
     */
    public float getAudioCurrVol() {
        return currVol;
    }

    /**
     * Set audio current pan.
     *
     * @param newPan Pan.
     */
    public void setAudioCurrPan(float newPan) {
        currPan = newPan;
    }

    /**
     * Get audio current pan.
     */
    public float getAudioCurrPan() {
        return currPan;
    }

    /**
     * Set audio loudness.
     */
    public void setAudioLoudness(float loudness) {
        aLoudness = loudness;
    }

    /**
     * Get audio loudness.
     */
    public float getAudioLoudness() {
        return aLoudness;
    }

    /**
     * Set audio pan sensitivity.
     */
    public void setAudioPSens(float pSens) {
        aPanSens = pSens;
    }

    /**
     * Get audio pan sensitivity.
     */
    public float getAudioPSens() {
        return aPanSens;
    }
}
