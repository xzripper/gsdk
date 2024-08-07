// GSDK Blur Vertex Shader.

#version 330

layout(location = 0) in vec3 vertexPosition; 
layout(location = 1) in vec2 vertexTexCoord;
layout(location = 2) in vec4 vertexColor;

out vec2 fragTexCoord;
out vec4 fragColor;

uniform mat4 mvp;

void main() {
    fragTexCoord = vertexTexCoord;
    fragColor = vertexColor;
    
    gl_Position = mvp * vec4(vertexPosition, 1.0);
}
