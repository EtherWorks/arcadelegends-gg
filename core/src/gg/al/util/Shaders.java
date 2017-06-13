package gg.al.util;

/**
 * Created by Thomas Neumann on 07.06.2017.<br>
 * Container class holding shader code.
 */
public class Shaders {
    public interface GradientShader {
        String vertexShader = "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "uniform float u_gradient;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "varying float cooldown_gradient;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   v_color = a_color;\n" +
                "   v_color.a = v_color.a * (255.0/254.0);\n" +
                "   v_texCoords = a_texCoord0;\n" +
                "   cooldown_gradient = u_gradient;\n" +
                "   gl_Position =  u_projTrans * a_position;\n" +
                "}";
        String fragmentShader = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "varying float cooldown_gradient;\n" +
                "uniform sampler2D u_texture;\n" +
                "void main()\n" +
                "{\n" +
                "  vec4 color = texture2D(u_texture, v_texCoords);\n" +
                "  if(color.a == 0 || color.r <= 1.0f -cooldown_gradient) discard;\n" +
                "  gl_FragColor = v_color * vec4(1,1,1,1);\n" +
                "}";
    }
}
