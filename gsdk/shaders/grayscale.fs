#version 330

in vec2 fragTexCoord;
in vec4 fragColor;

uniform sampler2D texture0;

out vec4 finalColor;

void main() {
    vec4 texelColor = texture(texture0, fragTexCoord);
    
    finalColor = vec4(vec3(dot(texelColor.rgb, vec3(0.299, 0.587, 0.114))), texelColor.a);
}
