// https://github.com/Apfelstrudel-Technologien/rlPPGlitch/

#version 330

uniform sampler2D texture0;

uniform vec2 winRes;

uniform float time;

uniform float chromaMult;
uniform float gMult;
uniform float tMult;

out vec4 fragColor;

void main(void) {
    vec2 uv = gl_FragCoord.xy / winRes;

    float glitch = fract(uv.y * 32.0 + time * tMult) * gMult;

    fragColor = vec4(texture(texture0, uv - vec2(sin(time * tMult) * chromaMult + glitch * 2.0, 0.0)).r,
                     texture(texture0, uv - vec2(cos(time * tMult) * chromaMult + glitch * 1.5, 0.0)).g,
                     texture(texture0, uv - vec2(sin(time * tMult + 0.5) * chromaMult + glitch, 0.0)).b, 1.0);
}
