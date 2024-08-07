// VSDK Bump Map Vertex Shader.

#version 330

in vec3 vertexPosition;
in vec2 vertexTexCoord;
in vec3 vertexNormal;
in vec3 vertexTangent;
in vec3 vertexBitangent;

out vec2 fragTexCoord;
out vec3 fragNormal;
out vec3 fragPosition;

out mat3 TBN;

uniform mat4 mvp;
uniform mat4 model;
uniform mat4 normalMatrix;

void main() {
    fragTexCoord = vertexTexCoord;

    vec3 T = normalize(vec3(normalMatrix * vec4(vertexTangent, 0.0)));
    vec3 B = normalize(vec3(normalMatrix * vec4(vertexBitangent, 0.0)));
    vec3 N = normalize(vec3(normalMatrix * vec4(vertexNormal, 0.0)));

    TBN = mat3(T, B, N);

    fragPosition = vec3(model * vec4(vertexPosition, 1.0));

    gl_Position = mvp * vec4(vertexPosition, 1.0);
}
