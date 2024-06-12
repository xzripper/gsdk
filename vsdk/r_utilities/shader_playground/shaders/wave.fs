#version 330

in vec2 fragTexCoord;
in vec3 fragNormal;
in vec3 fragPosition;

out vec4 finalColor;

//p_DefVal=float:1.0
uniform float cAdd;

//p_DefVal=float:1.0
uniform float cMult;

vec3 getRainbowColor(float position) {
    return vec3(abs(sin(position)), abs(sin(position + (1.0 + cAdd) * cMult)), abs(sin(position + (3.0 + cAdd) * cMult)));
}

void main() {
    finalColor = vec4(getRainbowColor(fragPosition.y * 2.0 + fragPosition.x + fragPosition.z), 1.0);
}
