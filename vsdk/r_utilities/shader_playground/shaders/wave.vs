#version 330

in vec3 vertexPosition;
in vec2 vertexTexCoord;
in vec3 vertexNormal;

uniform mat4 mvp;

uniform float time;

//p_DefVal=float:1.5
uniform float mult1;

//p_DefVal=float:0.2
uniform float mult2;

out vec2 fragTexCoord;
out vec3 fragNormal;
out vec3 fragPosition;

void main() {
    vec3 mPos = vertexPosition;

    mPos.y += sin(mPos.x * mult1 + time) * mult2;

    mPos.y += cos(mPos.z * mult1 + time) * mult2;

    mPos.x += sin(mPos.z * mult1 + time) * mult2;

    mPos.z += cos(mPos.y * mult1 + time) * mult2;

    gl_Position = mvp * vec4(mPos, 1.0);

    fragTexCoord = vertexTexCoord;
    fragNormal = vertexNormal;
    fragPosition = mPos;
}
