// GSDK Fragment Shader for Particle Emitter 3D for pixels filtering.

#version 330

in vec2 fragTexCoord;
in vec4 fragColor;

uniform sampler2D texture0;

uniform vec4 colDiffuse;

uniform vec4 threshold;

out vec4 finalColor;

void main() {
    vec4 texelColor = texture(texture0, fragTexCoord);

    if((texelColor.r < threshold.x && texelColor.g < threshold.y && texelColor.b < threshold.z) || texelColor.a < threshold.w) {
        discard;
    }

    finalColor = texelColor * colDiffuse * fragColor;
}
