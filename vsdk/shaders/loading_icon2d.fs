// VSDK Loading Icon (IVUI) Fragment Shader.

#version 330

uniform sampler2D texture0;

uniform float progress;
uniform float tint;

in vec2 fragTexCoord;
in vec4 fragColor;

out vec4 finalColor;

void main() {
    vec4 texColor = texture(texture0, fragTexCoord);

    texColor.rgb *= fragTexCoord.y > progress ? tint : 1.0;

    finalColor = texColor * fragColor;
}
