// GSDK 2D Vignette Fragment Shader (Created by Apfelstrudel Technologien).

#version 330

in vec2 fragTexCoord;
out vec4 finalColor;

uniform sampler2D texture0;

uniform float vRadius;
uniform float vBlur;
uniform vec3 vColor;

uniform float vPosX;
uniform float vPosY;

void main() {
    finalColor = mix(texture(texture0, fragTexCoord), vec4(vColor, 1.0), smoothstep(vRadius, vRadius + vBlur, distance(fragTexCoord, vec2(vPosX, vPosY))));
}
