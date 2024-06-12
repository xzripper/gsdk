package vsdk.source;

import com.raylib.Raylib;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.FloatPointer;

import java.util.ArrayList;

/**
 * Raylib GPU Objects (like model, shader, etc) manager.
 */
public class GPU_RLObjManager {
    public static final int VR_STEREO_CONFIG = 0;
    public static final int SHADER = 1;
    public static final int RANDOM_SEQUENCE = 2;
    public static final int FILE_DATA = 3;
    public static final int FILE_TEXT = 4;
    public static final int DIRECTORY_FILES = 5;
    public static final int DROPPED_FILES = 6;
    public static final int AUTOMATION_EVENT_LIST = 7;
    public static final int IMAGE = 8;
    public static final int IMAGE_COLORS = 9;
    public static final int IMAGE_PALETTE = 10;
    public static final int TEXTURE = 11;
    public static final int RENDER_TEXTURE = 12;
    public static final int FONT_DATA = 13;
    public static final int FONT = 14;
    public static final int UTF8 = 15;
    public static final int CODEPOINTS = 16;
    public static final int MODEL = 17;
    public static final int MESH = 18;
    public static final int MATERIAL = 19;
    public static final int MODEL_ANIMATION = 20;
    public static final int MODEL_ANIMATIONS = 21;
    public static final int WAVE = 22;
    public static final int SOUND = 23;
    public static final int SOUND_ALIAS = 24;
    public static final int WAVE_SAMPLES = 25;
    public static final int MUSIC_STREAM = 26;
    public static final int AUDIO_STREAM = 27;
    public static final int RENDER_BATCH = 28;
    public static final int VERTEX_ARRAY = 29;
    public static final int VERTEX_BUFFER = 30;
    public static final int FRAMEBUFFER = 31;
    public static final int SHADER_PROGRAM = 32;
    public static final int SHADER_BUFFER = 33;

    private final ArrayList<GPU_RLObj<Object>> objects = new ArrayList<>();

    /**
     * Add Raylib object.
     *
     * @param rlObj Raylib object.
     * @param gpuObjType Object type.
     */
    public void add(Object rlObj, int gpuObjType) {
        objects.add(new GPU_RLObj<>(rlObj, gpuObjType));
    }

    /**
     * Remove Raylib object.
     *
     * @param rlObj Object.
     */
    public void remove(Object rlObj) {
        objects.removeIf((gpuRlObj) -> gpuRlObj.rlObj.equals(rlObj));
    }

    /**
     * Unload all objects.
     *
     * @param object Specific object.
     */
    public void unload(Object object) {
        for(GPU_RLObj<Object> gpuRlObj : objects) {
            if(object != null) {
                if(gpuRlObj.rlObj != object) {
                    continue;
                }
            }

            switch(gpuRlObj.gpuObjType) {
                case VR_STEREO_CONFIG -> Raylib.UnloadVrStereoConfig((Raylib.VrStereoConfig) gpuRlObj.rlObj);
                case SHADER -> Raylib.UnloadShader((Raylib.Shader) gpuRlObj.rlObj);
                case RANDOM_SEQUENCE -> Raylib.UnloadRandomSequence(new IntPointer((int[]) gpuRlObj.rlObj));
                case FILE_DATA -> Raylib.UnloadFileData(new BytePointer((byte[]) gpuRlObj.rlObj));
                case FILE_TEXT -> Raylib.UnloadFileText(new BytePointer((byte[]) gpuRlObj.rlObj));
                case DIRECTORY_FILES -> Raylib.UnloadDirectoryFiles((Raylib.FilePathList) gpuRlObj.rlObj);
                case DROPPED_FILES -> Raylib.UnloadDroppedFiles((Raylib.FilePathList) gpuRlObj.rlObj);
                case AUTOMATION_EVENT_LIST -> Raylib.UnloadAutomationEventList((Raylib.AutomationEventList) gpuRlObj.rlObj);
                case IMAGE -> Raylib.UnloadImage((Raylib.Image) (gpuRlObj.rlObj));
                case IMAGE_COLORS -> Raylib.UnloadImageColors((Raylib.Color) gpuRlObj.rlObj);
                case IMAGE_PALETTE -> Raylib.UnloadImagePalette((Raylib.Color) gpuRlObj.rlObj);
                case TEXTURE -> Raylib.UnloadTexture((Raylib.Texture) gpuRlObj.rlObj);
                case RENDER_TEXTURE -> Raylib.UnloadRenderTexture((Raylib.RenderTexture) gpuRlObj.rlObj);
                case FONT_DATA -> Raylib.UnloadFontData(((Raylib.Font) gpuRlObj.rlObj).glyphs(), ((Raylib.Font) gpuRlObj.rlObj).glyphCount());
                case FONT -> Raylib.UnloadFont((Raylib.Font) gpuRlObj.rlObj);
                case UTF8 -> Raylib.UnloadUTF8(new BytePointer((byte[]) gpuRlObj.rlObj));
                case CODEPOINTS -> Raylib.UnloadCodepoints(new IntPointer((int[]) gpuRlObj.rlObj));
                case MODEL -> Raylib.UnloadModel((Raylib.Model) gpuRlObj.rlObj);
                case MESH -> Raylib.UnloadMesh((Raylib.Mesh) gpuRlObj.rlObj);
                case MATERIAL -> Raylib.UnloadMaterial((Raylib.Material) gpuRlObj.rlObj);
                case MODEL_ANIMATION -> Raylib.UnloadModelAnimation((Raylib.ModelAnimation) gpuRlObj.rlObj);
                case MODEL_ANIMATIONS -> {}
                case WAVE -> Raylib.UnloadWave((Raylib.Wave) gpuRlObj.rlObj);
                case SOUND -> Raylib.UnloadSound((Raylib.Sound) gpuRlObj.rlObj);
                case SOUND_ALIAS -> Raylib.UnloadSoundAlias((Raylib.Sound) gpuRlObj.rlObj);
                case WAVE_SAMPLES -> Raylib.UnloadWaveSamples(new FloatPointer((float[]) gpuRlObj.rlObj));
                case MUSIC_STREAM -> Raylib.UnloadMusicStream((Raylib.Music) gpuRlObj.rlObj);
                case AUDIO_STREAM -> Raylib.UnloadAudioStream((Raylib.AudioStream) gpuRlObj.rlObj);
                case RENDER_BATCH -> Raylib.rlUnloadRenderBatch((Raylib.rlRenderBatch) gpuRlObj.rlObj);
                case VERTEX_ARRAY -> Raylib.rlUnloadVertexArray((int) gpuRlObj.rlObj);
                case VERTEX_BUFFER -> Raylib.rlUnloadVertexBuffer((int) gpuRlObj.rlObj);
                case FRAMEBUFFER -> Raylib.rlUnloadFramebuffer((int) gpuRlObj.rlObj);
                case SHADER_PROGRAM -> Raylib.rlUnloadShaderProgram((int) gpuRlObj.rlObj);
                case SHADER_BUFFER -> Raylib.rlUnloadShaderBuffer((int) gpuRlObj.rlObj);
            }
        }

        objects.clear();
    }

    /**
     * Unload all objects.
     */
    public void unload() {
        unload(null);
    }

    /**
     * Get Raylib GPU objects.
     */
    public ArrayList<GPU_RLObj<Object>> getRlObjs() {
        return objects;
    }
}
