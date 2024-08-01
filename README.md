<p align="center"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk.gif?raw=true"></p>
<h3 align="center">VSDK Development Phase: <code>VSDK_V0.0.0.5-ALPHA/DEV_PUBLISH</code>.</h3>
<h3 align="center">VSDK is currently being developed by a solo developer. I appreciate your patience and understanding as progress may take some time.</h3>

![opera_bFDdi1rxJH](https://github.com/user-attachments/assets/eb75b696-df66-453c-98d9-80a1c10e35b3)
New! Visit documentation (extremely unfinished): https://violent-studio.github.io/vsdk

<h3 align="center">Example 1</h3>

```java
import com.raylib.Raylib;

import com.raylib.Jaylib.Vector3;

import static com.raylib.Jaylib.BLACK;

import vsdk.source.utils.WindowUtilities;
import static vsdk.source.utils.WindowUtilities.WindowFlags;

import vsdk.source.vectors.Vector2Df;
import vsdk.source.vectors.Vector3Df;

import vsdk.source.vrender.Quad;

import vsdk.source.utils.VShader;

public class Main {
    public static void main(String[] args) {
        WindowFlags flags = WindowUtilities.initWindow(
            "Window.",
            1000,
            800,
            120,
            WindowUtilities.FULLSCREEN_MODE,
            Raylib.FLAG_VSYNC_HINT | Raylib.FLAG_MSAA_4X_HINT,
            false
        );

        Raylib.Camera3D cam = new Raylib.Camera3D()
                ._position(new Vector3(1.0f, 0.0f, 0.0f))
                .target(new Vector3(0.0f, 0.0f, 0.0f))
                .up(new Vector3(0.0f, 1.0f, 0.0f))
                .fovy(45.0f)
                .projection(Raylib.CAMERA_PERSPECTIVE);

        VShader rainbow = new VShader(null, "shdr.frag", VShader.FILE, false);

        rainbow.combineFragment(VShader.FILE, "shdr2.frag");

        Raylib.DisableCursor();

        while(!Raylib.WindowShouldClose()) {
            Raylib.UpdateCamera(cam, Raylib.CAMERA_FREE);

            Raylib.BeginDrawing();
            Raylib.ClearBackground(BLACK);
            Raylib.BeginMode3D(cam);

            rainbow.setUniformFloat("time", (float) Raylib.GetTime());

            rainbow.begin();

            for(int z=-10; z < 20; z += 10) {
                Quad.drawQuad(
                    new Vector2Df(10, 10),
                    new Vector3Df(0, 0, z),
                    new Vector3Df(90, (float) Math.cos(Raylib.GetTime() * 0.7) * 90, 0),
                    BLACK,
                    true
                );
            }

            rainbow.end();

            Raylib.EndMode3D();
            Raylib.EndDrawing();
        }

        rainbow.unload();

        Raylib.CloseWindow();
    }
}
```

shdr.frag
```glsl
//:put-version

//:put-io
//:put-uni

void main() {
    //:pshdr#grad

    finalColor = color;
}
```

shdr2.frag
```glsl
//:version
#version 330

//:io
in vec2 fragTexCoord;
out vec4 finalColor;
//:eio

//:uni
uniform float time;
//:euni

void main() {
    //:shdr#grad
    float speed = 4.0;
    float frequency = 64.0;

    float red = 0.5 + 0.5 * sin(frequency * fragTexCoord.x + speed * time + 0.0);
    float green = 0.5 + 0.5 * sin(frequency * fragTexCoord.x + speed * time + 2.0);
    float blue = 0.5 + 0.5 * sin(frequency * fragTexCoord.x + speed * time + 4.0);

    vec4 color = vec4(red, green, blue, 1.0);
    //:eshdr

    finalColor = color;
}
```

<p align="center"><img src="https://github.com/user-attachments/assets/f46519ac-9f5c-4656-baf8-04eb11e94921"></p>


<h3 align="center">VSDK Bump Mapping (Youtube).</h3>
<p align="center"><a href="https://www.youtube.com/watch?v=HUJ3RxE8DhQ"><img src="http://markdown-videos-api.jorgenkh.no/youtube/HUJ3RxE8DhQ.gif?width=450&height=250&duration=1000" alt="Bump Mapping ALPHA" title="Bump Mapping ALPHA"/></a></p>
<h3 align="center"><a href="https://github.com/violent-studio/vsdk/blob/main/vsdk/examples%26docs/VLib.Vinet.TCP.md">VSDK Chat implementation via Vinet (Violent Immediate mode Networking library).</a></h3>

<h3 align="center">VSDK Noise Generator.</h3>

<img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/PerlinBillowNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/PerlinFractalNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/PerlinNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/PerlinRidgeNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/PerlinTurbulenceNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/SimplexBillowNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/SimplexNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/SimplexRidgeNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/SimplexTurbulenceNoiseImage.png?raw=true" width="300"><img src="https://github.com/violent-studio/vmedia/blob/main/vsdk/WhiteNoiseImage.png?raw=true" width="300">

<h3 align="center">VSDK Shader Playground.</h3>

<p align="center"><img src="https://github.com/violent-studio/vsdk/blob/main/vsdk/r_utilities/shader_playground/utility.png?raw=true" width="600"></p>

<h3 align="center">VSDK IVUI.</h3>

<p align="center"><img src="https://github.com/violent-studio/vsdk/assets/94743980/4208bfdc-1fdc-40eb-99c6-07c9c0511b1f?raw=true" width="650"></p>

<h3 align="center">VSDK SPPS.</h3>

<p align="center"><img src="https://github.com/violent-studio/vsdk/assets/94743980/58f7891d-ba0f-4f4b-b89f-3317fc21e094" width="650"></p>

<h3 align="center">VSDK 3D Particle Emitter.</h3>

<p align="center"><img src="https://github.com/user-attachments/assets/7e441f9e-fda3-4cee-bb28-99ecd8000b07" width="650"><br><i><sub>Smoke Tunnel</sub></i></p>
<p align="center"><img src="https://github.com/user-attachments/assets/9883ddf4-c3df-41ba-bc22-4e079685ad88" width="650"><br><i><sub>Smoke Torus</sub></i></p>
<p align="center"><img src="https://github.com/user-attachments/assets/6b8a1b14-1234-4797-8c7d-30d833676392" width="650"><br><i><sub>Particles</sub></i></p>
<p align="center"><img src="https://github.com/user-attachments/assets/32ccc465-c570-43a2-a427-7542b1a60374" width="650"><br><i><sub>Smoke particles</sub></i></p>
