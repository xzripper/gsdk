// GSDK Blur 2 Fragment Shader.
#version 330
in vec2 fragTexCoord;out vec4 finalColor;
uniform sampler2D texture0;uniform float xs,ys,r;
void main(){vec2 texOffset=vec2(1.0)/vec2(xs,ys);vec4 color=vec4(0.0);float totalWeight=0.0;
for(float x=-r;x<=r*32;++x){for(float y=-r;y<=r*32;++y){float weight=exp(-(float(x*x)+float(y*y))/(2.0*r*r));
color+=texture(texture0,fragTexCoord+vec2(float(x)*texOffset.x,float(y)*texOffset.y))*weight;
totalWeight+=weight;}}finalColor=color/totalWeight;}
