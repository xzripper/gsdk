package gsdk.r_utilities.shader_playground;

import imgui.type.ImInt;
import imgui.type.ImFloat;

import java.util.ArrayList;

public class ShaderUniforms {
    private ArrayList<ShaderUniform<ImInt>> iUniforms = new ArrayList<>();

    private ArrayList<ShaderUniform<ImFloat>> fUniforms = new ArrayList<>();

    private ArrayList<ShaderUniform<float[]>> fVec2Uniforms = new ArrayList<>();
    private ArrayList<ShaderUniform<float[]>> fVec3Uniforms = new ArrayList<>();
    private ArrayList<ShaderUniform<float[]>> fVec4Uniforms = new ArrayList<>();

    private ArrayList<ShaderUniform<int[]>> iVec2Uniforms = new ArrayList<>();
    private ArrayList<ShaderUniform<int[]>> iVec3Uniforms = new ArrayList<>();
    private ArrayList<ShaderUniform<int[]>> iVec4Uniforms = new ArrayList<>();

    public void pushInt(String uName, int uValue) {
        iUniforms.add(new ShaderUniform<>(uName, new ImInt(uValue), UniformsParser.UNIFORM_TYPE_INT));
    }

    public void pushFloat(String uName, float uValue) {
        fUniforms.add(new ShaderUniform<>(uName, new ImFloat(uValue), UniformsParser.UNIFORM_TYPE_FLOAT));
    }

    public void pushFVec2(String uName, float[] uValue) {
        fVec2Uniforms.add(new ShaderUniform<>(uName, uValue, UniformsParser.UNIFORM_TYPE_VEC2));
    }

    public void pushFVec3(String uName, float[] uValue) {
        fVec3Uniforms.add(new ShaderUniform<>(uName, uValue, UniformsParser.UNIFORM_TYPE_VEC3));
    }

    public void pushFVec4(String uName, float[] uValue) {
        fVec4Uniforms.add(new ShaderUniform<>(uName, uValue, UniformsParser.UNIFORM_TYPE_VEC4));
    }

    public void pushIVec2(String uName, int[] uValue) {
        iVec2Uniforms.add(new ShaderUniform<>(uName, uValue, UniformsParser.UNIFORM_TYPE_IVEC2));
    }

    public void pushIVec3(String uName, int[] uValue) {
        iVec3Uniforms.add(new ShaderUniform<>(uName, uValue, UniformsParser.UNIFORM_TYPE_IVEC3));
    }

    public void pushIVec4(String uName, int[] uValue) {
        iVec4Uniforms.add(new ShaderUniform<>(uName, uValue, UniformsParser.UNIFORM_TYPE_IVEC4));
    }

    public ArrayList<ShaderUniform<ImInt>> getIUniforms() {
        return iUniforms;
    }

    public ArrayList<ShaderUniform<ImFloat>> getFUniforms() {
        return fUniforms;
    }

    public ArrayList<ShaderUniform<float[]>> getFVec2Uniforms() {
        return fVec2Uniforms;
    }

    public ArrayList<ShaderUniform<float[]>> getFVec3Uniforms() {
        return fVec3Uniforms;
    }

    public ArrayList<ShaderUniform<float[]>> getFVec4Uniforms() {
        return fVec4Uniforms;
    }

    public ArrayList<ShaderUniform<int[]>> getIVec2Uniforms() {
        return iVec2Uniforms;
    }

    public ArrayList<ShaderUniform<int[]>> getIVec3Uniforms() {
        return iVec3Uniforms;
    }

    public ArrayList<ShaderUniform<int[]>> getIVec4Uniforms() {
        return iVec4Uniforms;
    }

    public boolean isEmpty() {
        return getIUniforms().isEmpty() &&
                getFUniforms().isEmpty() &&
                getFVec2Uniforms().isEmpty() && getFVec3Uniforms().isEmpty() && getFVec4Uniforms().isEmpty() &&
                getIVec2Uniforms().isEmpty() && getIVec3Uniforms().isEmpty() && getIVec4Uniforms().isEmpty();
    }

    public void noDefValSet() {
        iUniforms.forEach(ShaderUniform::setP_NoDefValSet);

        fUniforms.forEach(ShaderUniform::setP_NoDefValSet);

        fVec2Uniforms.forEach(ShaderUniform::setP_NoDefValSet);
        fVec3Uniforms.forEach(ShaderUniform::setP_NoDefValSet);
        fVec4Uniforms.forEach(ShaderUniform::setP_NoDefValSet);

        iVec2Uniforms.forEach(ShaderUniform::setP_NoDefValSet);
        iVec3Uniforms.forEach(ShaderUniform::setP_NoDefValSet);
        iVec4Uniforms.forEach(ShaderUniform::setP_NoDefValSet);
    }
}
