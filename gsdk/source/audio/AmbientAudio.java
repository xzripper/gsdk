package vsdk.source.audio;

import com.raylib.Raylib;

import static vsdk.source.utils.Assert.assert_f;

/**
 * Ambient audio implementation.
 */
public class AmbientAudio {
    private final Raylib.Music rlMusic;

    /**
     * Initialize ambient audio.
     *
     * @param audioP Audio path.
     */
    public AmbientAudio(String audioP) {
        assert_f(Raylib.IsAudioDeviceReady(), "audio_device != initialized; maybe use InitAudioDevice?");

        rlMusic = Raylib.LoadMusicStream(audioP);

        assert_f(Raylib.IsMusicReady(rlMusic), "rlMusic != valid");
    }

    /**
     * Handle ambient audio.
     *
     * @param fadeInEnd Fade end (-1 to disable).
     * @param fadeInStartVol Fade in start volume (can be 0.0f by default, or -1 if fade is disabled).
     * @param fadeOutStart Begin fade out before end (-1 to disable).
     * @param fadeOutMinVol Fade out minimal value (can be 0.0f by default, or -1 if fade is disabled).
     */
    public void handleAmbient(float fadeInEnd, float fadeInStartVol, float fadeOutStart, float fadeOutMinVol) {
        if(!Raylib.IsMusicStreamPlaying(rlMusic)) return;

        Raylib.UpdateMusicStream(rlMusic);

        float playedLen = Raylib.GetMusicTimePlayed(rlMusic);
        float totalLen = Raylib.GetMusicTimeLength(rlMusic);

        if(fadeInEnd != -1 && playedLen < fadeInEnd) {
            Raylib.SetMusicVolume(rlMusic, fadeInStartVol + ((1.0f - fadeInStartVol) * (playedLen / fadeInEnd)));
        }

        if(fadeOutStart != -1 && playedLen > (totalLen - fadeOutStart)) {
            Raylib.SetMusicVolume(rlMusic, Math.max(fadeOutMinVol + (((totalLen - playedLen) / fadeOutStart) * (1.0f - fadeOutMinVol)), fadeOutMinVol));
        }
    }

    /**
     * Handle ambient audio.
     *
     * @param fadeInEnd Fade end (-1 to disable).
     * @param fadeOutStart Begin fade out before end (-1 to disable).
     */
    public void handleAmbient(float fadeInEnd, float fadeOutStart) {
        handleAmbient(fadeInEnd, 0.0f, fadeOutStart, 0.0f);
    }

    /**
     * Handle ambient audio (no fade effects).
     */
    public void handleAmbient() {
        handleAmbient(-1, -1, -1, -1);
    }

    /**
     * Set audio looping.
     *
     * @param loop Loop?
     */
    public void setLoop(boolean loop) {
        rlMusic.looping(loop);
    }

    /**
     * Play ambient audio.
     */
    public void play() {
        Raylib.PlayMusicStream(rlMusic);
    }

    /**
     * Stop ambient audio.
     */
    public void stop() {
        Raylib.StopMusicStream(rlMusic);
    }

    /**
     * Get Raylib.Music object.
     */
    public Raylib.Music getRlMusic() {
        return rlMusic;
    }

    /**
     * Unload ambient audio.
     */
    public void unloadAmbient() {
        Raylib.UnloadMusicStream(rlMusic);
    }
}
