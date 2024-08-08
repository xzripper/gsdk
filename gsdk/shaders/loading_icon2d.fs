// GSDK Loading Icon (IVUI) Fragment Shader.
// Since v0.0.0.6-alpha/dev shader is supported only by GShader (or custom pre-processor definitions system implementation).

#version 330

uniform sampler2D texture0;

uniform float progress;
uniform float tint;

in vec2 fragTexCoord;
in vec4 fragColor;

out vec4 finalColor;

#define PI 3.14159265358979323846

void main() {
    vec4 texColor = texture(texture0, fragTexCoord);

    #ifdef top
    texColor.rgb *= fragTexCoord.y > progress ? tint : 1.0;
    #endif

    #ifdef vertical
    texColor.rgb *= fragTexCoord.x > progress ? tint : 1.0;
    #endif

    #ifdef pie
    float angle = atan(fragTexCoord.y - 0.5, fragTexCoord.x - 0.5) + PI;
    float radius = length(fragTexCoord - vec2(0.5));

    float progressAngle = 2.0 * PI * progress;

    texColor.rgb *= (radius <= 0.5 && angle <= progressAngle) ? 1.0 : tint;
    #endif

    finalColor = texColor * fragColor;
}
