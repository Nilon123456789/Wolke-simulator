#version 330 core

in vec2 TexCoords;
out vec4 FragColor;

uniform sampler2D u_texture;
uniform vec2 u_resolution = vec2(800, 600);

// Simple shader affichant la grille de la simulation en noir et blanc

void main()
{
    vec2 uv = gl_FragCoord.xy / u_resolution.xy;
    uv.y = 1.0 - uv.y;
    vec3 color = texture(u_texture, uv).rrr;
    FragColor = vec4(color, 1.0);
}
