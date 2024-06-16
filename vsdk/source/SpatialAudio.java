package vsdk.source;

import com.raylib.Raylib;

import static vsdk.source.Assert.assert_f;

/**
 * Spatial audio implementation (ALPHA): https://github.com/orgs/violent-studio/projects/1/views/1?pane=issue&itemId=63421011 (Reworked some things audio).
 */
public class SpatialAudio {
    private final SpatialAudioData spAudioData;

    /**
     * Create new spatial audio.
     *
     * @param spAudioData_ Spatial audio data.
     */
    public SpatialAudio(SpatialAudioData spAudioData_) {
        assert_f(Raylib.IsAudioDeviceReady(), "audio_device != initialized; maybe use InitAudioDevice?");

        spAudioData = spAudioData_;

        assert_f(Raylib.IsSoundReady(spAudioData.getAudio()), "sp_audio_data->audio != valid");
    }

    /**
     * Handle spatial sound.
     *
     * @param camPos Camera position.
     * @param camTargetZ Camera target Z.
     * @param invPanEff Inverse pan effect?
     */
    public void handleSpatiality(Vector3Df camPos, float camTargetZ, boolean invPanEff) {
        spAudioData.setAudioCurrVol(calcDistVol(camPos));
        spAudioData.setAudioCurrPan(calcDistPan(camPos.z(), camTargetZ, invPanEff));

        Raylib.SetSoundVolume(spAudioData.getAudio(), spAudioData.getAudioCurrVol());
        Raylib.SetSoundPan(spAudioData.getAudio(), spAudioData.getAudioCurrPan());
    }

    /**
     * Calculate distanced volume between camera and audio.
     *
     * @param camPos Camera position.
     */
    public float calcDistVol(Vector3Df camPos) {
        float dist = 0.0f;

        for(int posIndex=0; posIndex < 3; posIndex++) {
            dist += VMath.abs((spAudioData.getAudioPos().toArray()[posIndex] * 0.1f) - (camPos.toArray()[posIndex] * 0.1f));
        }

        return (float) VMath.clamp(SpatialAudioData.MIN_VOL, SpatialAudioData.MAX_VOL,  (SpatialAudioData.MAX_VOL + spAudioData.getAudioLoudness()) - dist);
    }

    /**
     * Calculate audio pan based on camera position/target Z.
     *
     * @param camPositionZ Camera position X.
     * @param camTargetZ Camera position Z.
     * @param inverse Inverse effect?
     */
    public float calcDistPan(float camPositionZ, float camTargetZ, boolean inverse) {
        if(inverse)
            return (float) VMath.clamp(SpatialAudioData.LEFT_PAN, SpatialAudioData.RIGHT_PAN, SpatialAudioData.CENTER_PAN - (spAudioData.getAudioPos().x() - (camPositionZ + camTargetZ)) * 0.1f);
        else
            return (float) VMath.clamp(SpatialAudioData.LEFT_PAN, SpatialAudioData.RIGHT_PAN, SpatialAudioData.CENTER_PAN + (spAudioData.getAudioPos().x() - (camPositionZ + camTargetZ)) * 0.1f);
    }

    /**
     * Get audio.
     */
    public Raylib.Sound getAudio() {
        return spAudioData.getAudio();
    }

    /**
     * Set audio position.
     *
     * @param audioPos Audio position in 3D world.
     */
    public void setAudioPos(Vector3Df audioPos) {
        spAudioData.setAudioPos(audioPos);
    }

    /**
     * Get audio position.
     */
    public Vector3Df getAudioPos() {
        return spAudioData.getAudioPos();
    }

    /**
     * Get audio start volume.
     */
    public float getAudioStartVol() {
        return spAudioData.getAudioCurrVol();
    }

    /**
     * Get audio current volume.
     */
    public float getAudioCurrVol() {
        return spAudioData.getAudioCurrVol();
    }

    /**
     * Get audio current pan.
     */
    public float getAudioCurrPan() {
        return spAudioData.getAudioCurrPan();
    }

    /**
     * Set audio loudness.
     *
     * @param loudness Loudness.
     */
    public void setAudioLoudness(float loudness) {
        spAudioData.setAudioLoudness(loudness);
    }

    /**
     * Get audio loudness.
     */
    public float getAudioLoudness() {
        return spAudioData.getAudioLoudness();
    }

    /**
     * Set audio pan sensitivity.
     *
     * @param sens Sensitivity..
     */
    public void setAudioPSens(float sens) {
        spAudioData.setAudioPSens(sens);
    }

    /**
     * Get audio pan sensitivity.
     */
    public float getAudioPSens() {
        return spAudioData.getAudioPSens();
    }

    /**
     * Unload sound.
     */
    public void unloadSound() {
        Raylib.UnloadSound(spAudioData.getAudio());
    }

    /**
     * Get camera position as Vector3Df.
     *
     * @param cam 3D Camera.
     */
    public static Vector3Df getCamPos(Raylib.Camera3D cam) {
        return new Vector3Df(cam._position().x(), cam._position().y(), cam._position().z());
    }
}
