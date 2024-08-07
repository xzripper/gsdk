// GSDK Bump Map Fragment Shader.

#version 330

in vec2 fragTexCoord;
in vec3 fragNormal;
in vec3 fragPosition;

out vec4 finalColor;

uniform sampler2D texture0;
uniform sampler2D bumpMap;

uniform vec3 lightPos;
uniform vec3 viewPos;

uniform float specularShininess;
uniform float ambientValue;

void main() {
    vec3 normal = normalize(texture(bumpMap, fragTexCoord).rgb * 2.0 - 1.0);

    vec3 lightDir = normalize(lightPos - fragPosition);
    vec3 viewDir = normalize(viewPos - fragPosition);
    vec3 reflectDir = reflect(-lightDir, normal);

    float diffuse = max(dot(lightDir, normal), 0.0);
    float specular = pow(max(dot(viewDir, reflectDir), 0.0), specularShininess);

    vec3 texCol = texture(texture0, fragTexCoord).rgb;

    finalColor = vec4((ambientValue * texCol) + (diffuse * texCol) + (specular * vec3(1.0, 1.0, 1.0)), 1.0);
}
