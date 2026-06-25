#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform vec2 OutSize;
uniform float Time;

out vec4 fragColor;

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);

    float breathing = sin(Time * 3.0) * 0.0015;
    float split = 0.05 + breathing;

    vec2 leftUv = clamp(texCoord + vec2(-split, 0.0), 0.0, 1.0);
    vec2 rightUv = clamp(texCoord + vec2(split, 0.0), 0.0, 1.0);

    vec4 leftVision = texture(DiffuseSampler, leftUv);
    vec4 rightVision = texture(DiffuseSampler, rightUv);

    vec3 doubledVision = mix(leftVision.rgb, rightVision.rgb, 0.5);

    vec3 pinkTint = vec3(1.0, 0.28, 0.72);

    vec3 color = mix(base.rgb, doubledVision, 0.85);
    color = mix(color, pinkTint, 0.12);

    fragColor = vec4(color, 1.0);
}