// GSDK Blur Fragment Shader.
#version 330
in vec2 fragTexCoord;out vec4 finalColor;
uniform sampler2D texture0;uniform float xs,ys,r;
void main(){
float x,y,xx,yy,rr=r*r,dx,dy,w,w0=0.3780/pow(r,1.975);
vec2 p;vec4 col=vec4(0.0);for(dx=1.0/xs,x=-r,p.x=fragTexCoord.x+(x*dx);x<=r;x++,p.x+=dx){
xx=x*x;for(dy=1.0/ys,y=-r,p.y=fragTexCoord.y+(y*dy);y<=r;y++,p.y+=dy){yy=y*y;
if(xx+yy<=rr){w=w0*exp((-xx-yy)/(2.0*rr));col+=texture(texture0,p)*w;}}}finalColor=col;}
