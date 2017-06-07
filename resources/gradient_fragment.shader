#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
varying float gradient;
uniform sampler2D u_texture;
void main()
{
  vec4 color = texture2D(u_texture, v_texCoords);
  if(color.r <= 1.0f - gradient) discard;
  gl_FragColor = v_color * color;
}