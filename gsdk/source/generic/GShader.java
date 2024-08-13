package gsdk.source.generic;

import com.raylib.Raylib;

import java.util.HashMap;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.FloatPointer;

import static gsdk.glib.futils.FReadUtil.read;

import static gsdk.source.generic.Assert.assert_t;
import static gsdk.source.generic.Assert.assert_f;

/**
 * GShader is a high-level abstraction level for shaders in Raylib.
 * Key features of GShader are: Unparalleled feature for merging shader with another shaders (vertex and fragment),
 * preprocessor definitions implementation, loading shaders from file or memory just by changing flag,
 * simple loop-safe automatic shader uniform locations handler, pointer-free and flag-free shader uniform updater (+ advanced types supported: matrix, texture),
 * utility functions like reload, unload, etc.
 */
public class GShader {
    private Raylib.Shader shader = null;

    private HashMap<String, Integer> locations;

    private String vertexSrc, fragmentSrc;

    private String vertPreProcDefs, fragPreProcDefs;

    private int loadType;

    public static final int FILE = 1;
    public static final int MEMORY = 2;

    /**
     * Load shader.
     * 
     * @param vertex Vertex shader.
     * @param fragment Fragment shader.
     * @param loadFlag Load type (file/memory).
     * @param load Load shader (can be set to false if shader is designed to be combined with others).
     */
    public GShader(String vertex, String fragment, int loadFlag, boolean load) {
        assert_f(vertex != null || fragment != null, "expected vertex or fragment shader");

        assert_f(loadFlag == FILE || loadFlag == MEMORY, "expected file or memory shader");

        vertexSrc = vertex != null ? read(vertex) : null;
        fragmentSrc = fragment != null ? read(fragment) : null;

        loadType = loadFlag;

        if(load) {
            shader = loadFlag == FILE ? Raylib.LoadShader(vertex, fragment) : Raylib.LoadShaderFromMemory(vertex, fragment);
        }

        locations = new HashMap<>();
    }

    /**
     * Load shader.
     * 
     * @param vertex Vertex shader.
     * @param fragment Fragment shader.
     * @param loadFlag Load type (file/memory).
     */
    public GShader(String vertex, String fragment, int loadFlag) {
        this(vertex, fragment, loadFlag, true);
    }

    /**
     * Load shader with preprocessor definitions.
     * 
     * @param vertex Vertex shader.
     * @param fragment Fragment shader.
     * @param loadFlag Load type (file/memory).
     * @param load Load shader (can be set to false if shader is designed to be combined with others).
     * @param glslVersion Shader (GLSL) version.
     * @param ppDefsVert Preprocessor definitions for vertex shader.
     * @param ppDefsFrag Preprocessor definitions for fragment shader.
     */
    public GShader(String vertex, String fragment, int loadFlag, boolean load, int glslVersion, String[] ppDefsVert, String[] ppDefsFrag) {
        assert_f(vertex != null || fragment != null, "expected vertex or fragment shader");

        assert_f(loadFlag == FILE || loadFlag == MEMORY, "expected file or memory shader");

        String vertSrcStr = loadFlag == FILE ? (vertex != null ? read(vertex) : null) : vertex;
        String fragSrcStr = loadFlag == FILE ? (fragment != null ? read(fragment) : null) : fragment;

        vertexSrc = vertSrcStr != null ? applyPreProcDefs(vertSrcStr, glslVersion, ppDefsVert, true) : null;
        fragmentSrc = fragSrcStr != null ? applyPreProcDefs(fragSrcStr, glslVersion, ppDefsFrag, false) : null;

        loadType = loadFlag;

        if(load) {
            shader = Raylib.LoadShaderFromMemory(
                vertexSrc != null && vertexSrc.isEmpty() ? null : vertexSrc,
                fragmentSrc != null && fragmentSrc.isEmpty() ? null : fragmentSrc
            );
        }

        locations = new HashMap<>();
    }

    /**
     * Load shader with preprocessor definitions.
     * 
     * @param vertex Vertex shader.
     * @param fragment Fragment shader.
     * @param loadFlag Load type (file/memory).
     * @param glslVersion Shader (GLSL) version.
     * @param ppDefsVert Preprocessor definitions for vertex shader.
     * @param ppDefsFrag Preprocessor definitions for fragment shader.
     */
    public GShader(String vertex, String fragment, int loadFlag, int glslVersion, String[] ppDefsVert, String[] ppDefsFrag) {
        this(vertex, fragment, loadFlag, true, glslVersion, ppDefsVert, ppDefsFrag);
    }

    /**
     * Get vertex source.
     */
    public String getVertSrc() {
        return vertexSrc;
    }

    /**
     * Get fragment source.
     */
    public String getFragSrc() {
        return fragmentSrc;
    }

    /**
     * Get shader load type.
     */
    public int getLoadType() {
        return loadType;
    }

    /**
     * Get Raylib shader.
     */
    public Raylib.Shader getShader() {
        return shader;
    }

    /**
     * Get shader ID.
     */
    public int getID() {
        return shader.id();
    }

    /**
     * Begin shader mode.
     */
    public void begin() {
        Raylib.BeginShaderMode(shader);
    }

    /**
     * End shader mode.
     */
    public void end() {
        Raylib.EndShaderMode();
    }

    /**
     * Render objects with shader.
     * 
     * @param draw Objects drawing function.
     */
    public void shaded(Runnable draw) {
        begin(); draw.run(); end();
    }

    /**
     * Reload shader.
     * 
     * @param loadFlag Load type.
     */
    public void reload(int loadFlag) {
        shader = loadFlag == FILE ?
                Raylib.LoadShader(vertexSrc, fragmentSrc) :
                Raylib.LoadShaderFromMemory(
                    vertexSrc != null && vertexSrc.isEmpty() ? null : vertexSrc,
                    fragmentSrc != null && fragmentSrc.isEmpty() ? null : fragmentSrc
                );

        refreshLocations();
    }

    /**
     * Remove LAST preprocessor definitions from vertex.
     */
    public void rmVertPreProcDefs() {
        if(vertPreProcDefs != null && vertexSrc != null) vertexSrc = vertexSrc.replace(vertPreProcDefs, "");
    }

    /**
     * Remove LAST preprocessor definitions from fragment.
     */
    public void rmFragPreProcDefs() {
        if(fragPreProcDefs != null && fragmentSrc != null) fragmentSrc = fragmentSrc.replace(fragPreProcDefs, "");
    }

    /**
     * Apply preprocessor definitions on vertex shader.
     * 
     * @param glslVersion GLSL Version.
     * @param vertPreProcDefs Vertex preprocessor definitions.
     */
    public void preProcDefsVert(int glslVersion, String ...vertPreProcDefs) {
        assert_t(vertexSrc == null, "can't apply pre-proc definitions on null vertex shader");

        vertexSrc = applyPreProcDefs(vertexSrc, glslVersion, vertPreProcDefs, true);

        reload(MEMORY);
    }

    /**
     * Apply preprocessor definitions on fragment shader.
     * 
     * @param glslVersion GLSL Version.
     * @param fragPreProcDefs Fragment preprocessor definitions.
     */
    public void preProcDefsFrag(int glslVersion, String ...fragPreProcDefs) {
        assert_t(fragmentSrc == null, "can't apply pre-proc definitions on null fragment shader");

        fragmentSrc = applyPreProcDefs(fragmentSrc, glslVersion, fragPreProcDefs, false);

        reload(MEMORY);
    }


    /**
     * Combine current vertex shader with specified vertex shader(s).
     * 
     * @param loadFlag Target vertex shader(s) load type (file/memory).
     * @param vertex Vertex shader(s).
     */
    public void combineVertex(int loadFlag, String ...vertex) {
        assert_f(vertexSrc != null, "can't combine vertex shader: no vertex shaders loaded");

        assert_f(loadFlag == FILE || loadFlag == MEMORY, "expected file or memory shader");

        for(String vertShdr : vertex) {
            vertexSrc = combineShaders(vertexSrc, loadFlag == FILE ? read(vertShdr) : vertShdr);
        }

        reload(MEMORY);
    }

    /**
     * Combine current fragment shader with specified fragment shader(s).
     * 
     * @param loadFlag Target fragment shader(s) load type (file/memory).
     * @param fragment Fragment shader(s).
     */
    public void combineFragment(int loadFlag, String ...fragment) {
        assert_f(fragmentSrc != null, "can't combine fragment shader: no fragment shaders loaded");

        assert_f(loadFlag == FILE || loadFlag == MEMORY, "expected file or memory shader");

        for(String fragShdr : fragment) {
            fragmentSrc = combineShaders(fragmentSrc, loadFlag == FILE ? read(fragShdr) : fragShdr);
        }

        reload(MEMORY);
    }

    /**
     * Combine current vertex and fragment shader with specified shader(s).
     * 
     * @param loadFlag Target shader(s) load type (file/memory).
     * @param vertex Vertex shader(s).
     * @param fragment Fragment shader(s).
     */
    public void combine(int loadFlag, String[] vertex, String[] fragment) {
        combineVertex(loadFlag, vertex);
        combineFragment(loadFlag, fragment);
    }

    /**
     * Combine current vertex and fragment shader with specified shaders.
     * 
     * @param loadFlag Target shader load type (file/memory).
     * @param vertex Vertex shader.
     * @param fragment Fragment shader.
     */
    public void combineSingle(int loadFlag, String vertex, String fragment) {
        combineVertex(loadFlag, vertex);
        combineFragment(loadFlag, fragment);
    }

    /**
     * Get is location already stored in the map.
     * 
     * @param name Uniform name.
     */
    public boolean locationKnown(String name) {
        return locations.containsKey(name);
    }

    /**
     * Get shader uniform location.
     * Calls GetShaderLocation only if uniform wasn't gathered earlier (ie. it's OK to call this function in loop).
     * 
     * @param name Uniform name.
     */
    public int getLocation(String name) {
        if(!locationKnown(name)) {
            locations.put(name, Raylib.GetShaderLocation(shader, name));
        }

        return locations.get(name);
    }

    /**
     * Refresh all shader uniforms locations.
     */
    public void refreshLocations() {
        for(String key : locations.keySet()) {
            locations.put(key, Raylib.GetShaderLocation(shader, key));
        }
    }

    /**
     * Is uniform location valid.
     * 
     * @param location Location.
     */
    public boolean locationValid(int location) {
        return location != -1;
    }

    /**
     * Update shader uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     * @param type Uniform name.
     */
    public void setUniform(String name, Pointer value, int type) {
        Raylib.SetShaderValue(shader, getLocation(name), value, type);
    }

    /**
     * Update shader integer uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformInt(String name, int value) { setUniform(name, new IntPointer(value), Raylib.SHADER_UNIFORM_INT); }

    /**
     * Update shader float uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformFloat(String name, float value) { setUniform(name, new FloatPointer(value), Raylib.SHADER_UNIFORM_FLOAT); }

    /**
     * Update shader 2D vector uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformVec2(String name, float ...value) { setUniform(name, new FloatPointer(value), Raylib.SHADER_UNIFORM_VEC2); }

    /**
     * Update shader 3D vector uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformVec3(String name, float ...value) { setUniform(name, new FloatPointer(value), Raylib.SHADER_UNIFORM_VEC3); }

    /**
     * Update shader 4D vector uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformVec4(String name, float ...value) { setUniform(name, new FloatPointer(value), Raylib.SHADER_UNIFORM_VEC4); }

    /**
     * Update shader 2D integer vector uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformIVec2(String name, int ...value) { setUniform(name, new IntPointer(value), Raylib.SHADER_UNIFORM_IVEC2); }

    /**
     * Update shader 3D integer vector uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformIVec3(String name, int ...value) { setUniform(name, new IntPointer(value), Raylib.SHADER_UNIFORM_IVEC3); }

    /**
     * Update shader 4D integer vector uniform.
     * 
     * @param name Uniform name.
     * @param value Value.
     */
    public void setUniformIVec4(String name, int ...value) { setUniform(name, new IntPointer(value), Raylib.SHADER_UNIFORM_IVEC4); }

    /**
     * Update shader texture (sampler2d) uniform.
     * 
     * @param name Uniform name.
     * @param tex Texture.
     */
    public void setUniformTex(String name, Raylib.Texture tex) {
        Raylib.SetShaderValueTexture(shader, getLocation(name), tex);
    }

    /**
     * Update shader texture (sampler2d) uniform.
     * 
     * @param name Uniform name.
     * @param matrix Matrix.
     */
    public void setUniformMatrix(String name, Raylib.Matrix matrix) {
        Raylib.SetShaderValueMatrix(shader, getLocation(name), matrix);
    }

    /**
     * Is shader valid.
     */
    public boolean valid() {
        return Raylib.IsShaderReady(shader);
    }

    /**
     * Unload shader.
     */
    public void unload() {
        Raylib.UnloadShader(shader);
    }

    private String applyPreProcDefs(String shader, int glslVersion, String[] shaderPreProcDefs, boolean vert) {
        StringBuilder appliedPreProc = new StringBuilder();

        String version = "#version %d\n".formatted(glslVersion);

        appliedPreProc.append(version + '\n');

        for(String ppName : shaderPreProcDefs) {
            appliedPreProc.append("#define %s\n".formatted(ppName));
        }

        String append = shader.replace(version, "");

        if(vert) vertPreProcDefs = appliedPreProc.toString();
        else fragPreProcDefs = appliedPreProc.toString();
 
        appliedPreProc.append(append);

        return appliedPreProc.toString().replace("\n\n\n", "\n\n");
    }

    private static String combineShaders(String original, String target) {
        assert_t(original == null || target == null || original.isEmpty() || target.isEmpty(), "original and target shader must not be null or empty");

        String[] targetLines = target.split("\n");

        String tVersion = null;

        StringBuilder tIo = new StringBuilder();
        StringBuilder tUni = new StringBuilder();

        HashMap<String, StringBuilder> tShdr = new HashMap<>();

        boolean openIo = false, openUni = false, openShdr = false;

        String shdrId = null;

        for(int l=0; l < targetLines.length; l++) {
            String line = targetLines[l].strip();

            if(line.startsWith("//:version")) { tVersion = targetLines[l + 1];
            } else if(line.startsWith("//:io")) { openIo = true;
            } else if(line.startsWith("//:eio")) { openIo = false;
            } else if(line.startsWith("//:uni")) { openUni = true;
            } else if(line.startsWith("//:euni")) { openUni = false;
            } else if(line.startsWith("//:shdr")) {
                openShdr = true;

                String[] idSplit = line.split("#");

                assert_t(idSplit.length <= 1, "token #: expected shader id");

                shdrId = idSplit[1];

                tShdr.put(shdrId, new StringBuilder());
            } else if(line.startsWith("//:eshdr")) {
                openShdr = false;

                shdrId = null;
            }

            if(!line.startsWith("//")) {
                if(openIo) tIo.append(line + '\n');
                if(openUni) tUni.append(line + '\n');
                if(openShdr) tShdr.get(shdrId).append(line + '\n');
            }
        }

        assert_t(openIo || openUni || openShdr, "shader section is not closed");

        StringBuilder combined = new StringBuilder();

        String[] originalLines = original.split("\n");

        for(int l=0; l < originalLines.length; l++) {
            String line = originalLines[l].strip();

            if(line.startsWith("//:put-version")) {
                assert_t(tVersion == null, "tried to combine shader versions but version is not specified by target");

                if(!combined.toString().contains(tVersion)) combined.append(tVersion + '\n');
            } else if(line.startsWith("//:put-io")) {
                combined.append(tIo.toString() + '\n');
            } else if(line.startsWith("//:put-uni")) {
                combined.append(tUni.toString() + '\n');
            } else if(line.startsWith("//:pshdr")) {
                String[] idSplit = line.split("#");

                assert_t(idSplit.length <= 1, "token #: expected shader id");

                assert_f(tShdr.containsKey(idSplit[1]), "no shader with id %s found".formatted(idSplit[1].toString()));

                combined.append(tShdr.get(idSplit[1]).toString() + '\n');
            }

            if(!line.startsWith("//")) combined.append(line + '\n');
        }

        return combined.toString();
    }
}
