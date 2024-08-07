package gsdk.r_utilities.shader_playground;

import java.util.ArrayList;

import java.io.BufferedReader;

import java.io.FileReader;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class UniformsParser {
    private static final String TOKEN_VOID = "void";

    private static final String TOKEN_UNIFORM = "uniform";

    private static final String TOKEN_SPACE = " ";

    private static final String TOKEN_DEFVAL = "//p_DefVal=";

    private static final String TOKEN_TYPE_SPLIT = ":";

    private static final String TOKEN_NEXT_ELEMENT = ",";

    public static final String UNIFORM_TYPE_INT = "int";

    public static final String UNIFORM_TYPE_FLOAT = "float";

    public static final String UNIFORM_TYPE_VEC2 = "vec2";
    public static final String UNIFORM_TYPE_VEC3 = "vec3";
    public static final String UNIFORM_TYPE_VEC4 = "vec4";

    public static final String UNIFORM_TYPE_IVEC2 = "ivec2";
    public static final String UNIFORM_TYPE_IVEC3 = "ivec3";
    public static final String UNIFORM_TYPE_IVEC4 = "ivec4";

    public static final int DEFAULT_INT = 0;

    public static final float DEFAULT_FLOAT = 0.0f;

    public static final float[] DEFAULT_VEC2 = new float[] {DEFAULT_FLOAT, DEFAULT_FLOAT};
    public static final float[] DEFAULT_VEC3 = new float[] {DEFAULT_FLOAT, DEFAULT_FLOAT, DEFAULT_FLOAT};
    public static final float[] DEFAULT_VEC4 = new float[] {DEFAULT_FLOAT, DEFAULT_FLOAT, DEFAULT_FLOAT, DEFAULT_FLOAT};

    public static final int[] DEFAULT_IVEC2 = new int[] {DEFAULT_INT, DEFAULT_INT};
    public static final int[] DEFAULT_IVEC3 = new int[] {DEFAULT_INT, DEFAULT_INT, DEFAULT_INT};
    public static final int[] DEFAULT_IVEC4 = new int[] {DEFAULT_INT, DEFAULT_INT, DEFAULT_INT, DEFAULT_INT};

    private static String[] parseLines(String shader) {
        ArrayList<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(shader))) {
            for(String line; (line = reader.readLine()) != null;) lines.add(line);
        } catch (IOException e) {
            return null;
        }

        lines.removeIf(String::isBlank);

        return lines.toArray(new String[0]);
    }

    public static ShaderUniforms parseShaderUniforms(String shader) {
        ShaderUniforms uniforms = new ShaderUniforms();

        String nUniformDefVal = null;
        String nUniformDefValType = null;

        int defInt = DEFAULT_INT;

        float defFloat = DEFAULT_FLOAT;

        float[] defVec2 = DEFAULT_VEC2;
        float[] defVec3 = DEFAULT_VEC3;
        float[] defVec4 = DEFAULT_VEC4;

        int[] defIVec2 = DEFAULT_IVEC2;
        int[] defIVec3 = DEFAULT_IVEC3;
        int[] defIVec4 = DEFAULT_IVEC4;

        for(String line : requireNonNull(parseLines(shader))) {
            line = line.strip();

            if(line.startsWith(TOKEN_VOID)) {
                break;
            }

            if(line.startsWith(TOKEN_DEFVAL)) {
                String[] uDefUniformVal = line.split(TOKEN_DEFVAL);

                if(uDefUniformVal.length > 1) {
                    String[] uDefUniformValTypeSplit = uDefUniformVal[1].split(TOKEN_TYPE_SPLIT);

                    if(uDefUniformValTypeSplit.length > 1) {
                        nUniformDefVal = uDefUniformValTypeSplit[1].strip();

                        nUniformDefValType = uDefUniformValTypeSplit[0].strip();
                    }
                }
            }

            if(line.startsWith(TOKEN_UNIFORM)) {
                String[] uTokens = line.split(TOKEN_SPACE);

                if(uTokens.length < 3) {
                    continue;
                }

                String uType = uTokens[1];

                String uName = uTokens[2].substring(0, uTokens[2].length() - 1);

                if(nUniformDefValType != null & nUniformDefVal != null) {
                    switch (nUniformDefValType) {
                        case UNIFORM_TYPE_INT -> {
                            defInt = Integer.parseInt(nUniformDefVal);

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_FLOAT -> {
                            defFloat = Float.parseFloat(nUniformDefVal);

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_VEC2 -> {
                            String[] vecElements = nUniformDefVal.split(TOKEN_NEXT_ELEMENT);

                            if (vecElements.length < 2) {
                                defVec2 = DEFAULT_VEC2;
                            } else {
                                defVec2 = new float[] {
                                    Float.parseFloat(vecElements[0]),
                                    Float.parseFloat(vecElements[1])
                                };
                            }

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_VEC3 -> {
                            String[] vecElements = nUniformDefVal.split(TOKEN_NEXT_ELEMENT);

                            if (vecElements.length < 3) {
                                defVec3 = DEFAULT_VEC3;
                            } else {
                                defVec3 = new float[] {
                                    Float.parseFloat(vecElements[0]),
                                    Float.parseFloat(vecElements[1]),
                                    Float.parseFloat(vecElements[2])
                                };
                            }

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_VEC4 -> {
                            String[] vecElements = nUniformDefVal.split(TOKEN_NEXT_ELEMENT);

                            if (vecElements.length < 4) {
                                defVec4 = DEFAULT_VEC4;
                            } else {
                                defVec4 = new float[] {
                                    Float.parseFloat(vecElements[0]),
                                    Float.parseFloat(vecElements[1]),
                                    Float.parseFloat(vecElements[2]),
                                    Float.parseFloat(vecElements[3])
                                };
                            }

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_IVEC2 -> {
                            String[] vecElements = nUniformDefVal.split(TOKEN_NEXT_ELEMENT);

                            if (vecElements.length < 2) {
                                defIVec2 = DEFAULT_IVEC2;
                            } else {
                                defIVec2 = new int[] {
                                    Integer.parseInt(vecElements[0]),
                                    Integer.parseInt(vecElements[1])
                                };
                            }

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_IVEC3 -> {
                            String[] vecElements = nUniformDefVal.split(TOKEN_NEXT_ELEMENT);

                            if (vecElements.length < 3) {
                                defIVec3 = DEFAULT_IVEC3;
                            } else {
                                defIVec3 = new int[] {
                                    Integer.parseInt(vecElements[0]),
                                    Integer.parseInt(vecElements[1]),
                                    Integer.parseInt(vecElements[2])
                                };
                            }

                            nUniformDefVal = null;
                        }

                        case UNIFORM_TYPE_IVEC4 -> {
                            String[] vecElements = nUniformDefVal.split(TOKEN_NEXT_ELEMENT);

                            if (vecElements.length < 4) {
                                defIVec4 = DEFAULT_IVEC4;
                            } else {
                                defIVec4 = new int[] {
                                    Integer.parseInt(vecElements[0]),
                                    Integer.parseInt(vecElements[1]),
                                    Integer.parseInt(vecElements[2]),
                                    Integer.parseInt(vecElements[3])
                                };
                            }

                            nUniformDefVal = null;
                        }
                    }
                }

                switch (uType) {
                    case UNIFORM_TYPE_INT -> { uniforms.pushInt(uName, defInt); defInt = DEFAULT_INT; }

                    case UNIFORM_TYPE_FLOAT -> { uniforms.pushFloat(uName, defFloat); defFloat = DEFAULT_FLOAT; }

                    case UNIFORM_TYPE_VEC2 -> { uniforms.pushFVec2(uName, defVec2); defVec2 = DEFAULT_VEC2; }
                    case UNIFORM_TYPE_VEC3 -> { uniforms.pushFVec3(uName, defVec3); defVec3 = DEFAULT_VEC3; }
                    case UNIFORM_TYPE_VEC4 -> { uniforms.pushFVec4(uName, defVec4); defVec4 = DEFAULT_VEC4; }

                    case UNIFORM_TYPE_IVEC2 -> { uniforms.pushIVec2(uName, defIVec2); defIVec2 = DEFAULT_IVEC2; }
                    case UNIFORM_TYPE_IVEC3 -> { uniforms.pushIVec3(uName, defIVec3); defIVec3 = DEFAULT_IVEC3; }
                    case UNIFORM_TYPE_IVEC4 -> { uniforms.pushIVec4(uName, defIVec4); defIVec4 = DEFAULT_IVEC4; }
                }
            }
        }

        return uniforms;
    }
}
