```java

import com.raylib.Raylib;

import com.raylib.Jaylib.Vector3;
import gsdk.source.particles.*;
import gsdk.source.vectors.Vector3Df;
import gsdk.source.vectors.Vector3Di;
import gsdk.source.vrender.Texture;

import static com.raylib.Jaylib.BLACK;
import static gsdk.source.vectors.Vector3Df.vec3df;


// in development
public class Main {
    public static void main(String[] args) {
        Raylib.InitWindow(1000, 800, "Test window.");

        Raylib.SetTargetFPS(60);

        Raylib.Camera3D cam = new Raylib.Camera3D()
            ._position(new Vector3(3.0f, 0.0f, 0.0f))
            .target(new Vector3(0.0f, 0.0f, 0.0f))
            .up(new Vector3(0.0f, 1.0f, 0.0f))
            .fovy(45.0f)
            .projection(Raylib.CAMERA_PERSPECTIVE);

        ParticleEmitterConfig pec = new ParticleEmitterConfig(
            1000, // max particles
            0.01f, // explosiveness
            24.0f, // emission rate
            new EmissionVelocity(0.1f, 0.1f, 0.1f, EmissionVelocity.X_ADD_EXPL, EmissionVelocity.Z_ADD_EXPL), // emission velocity
            false, // inversed emission
            ParticleBlending.ALPHA, // blending
            32.0f, // lifetime
            3.5f, // scale
            0, // rotation
            1.0f, // alpha
            0.2f, // fade
            new Vector3Di(255, 255, 255), // color
            ParticleType.TEXTURE // type
        );

        ParticleEmitter3D particleEmitter3D = new ParticleEmitter3D(pec);

        particleEmitter3D.setPFColThreshold(0.15f, 0.15f, 0.15f, 0.8f);

        particleEmitter3D.setParticleTex(new Texture("src\\Smoke2.png"));

//        particleEmitter3D.filterParticleTex(new Vector4Di(0, 0, 0, 255), new Vector4Di(5, 5, 5, 255));

        particleEmitter3D.loadParticles();

        Raylib.DisableCursor();
        while(!Raylib.WindowShouldClose()) {

            Raylib.UpdateCamera(cam, Raylib.CAMERA_FREE);

            pec.setDelta(Raylib.GetFrameTime());

            particleEmitter3D.simulateParticles();

            Raylib.BeginDrawing();

            Raylib.ClearBackground(BLACK);

            Raylib.DrawFPS(0, 0);

            Raylib.BeginMode3D(cam);

            Raylib.DrawCube(new Raylib.Vector3().x(0).y(0).z(0), 0.5f, 0.5f, 0.5f, vec3df(1.0f, 0, 0.2f).toRlCol());

            particleEmitter3D.renderParticles(cam, new Vector3Df(0, 0, 0));
//
            Raylib.EndMode3D();

            Raylib.EndDrawing();
        }

        particleEmitter3D.unloadResources();

        Raylib.CloseWindow();
    }
}

```