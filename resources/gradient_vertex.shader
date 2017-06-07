attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projectionViewMatrix;
uniform float u_gradient;
varying vec4 v_color;
varying vec2 v_texCoords;
varying float gradient;

void main()
{
   v_color = a_color;
   v_color.a = v_color.a * (255.0/254.0);
   v_texCoords = a_texCoord0;
   gradient = u_gradient;
   gl_Position =  u_projectionViewMatrix * a_position;
}