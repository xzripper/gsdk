Example of using SPPS (made example while developing SPPS).

```java
import com.raylib.Raylib;
import vsdk.source.spps.PixelColliderContainer;
import vsdk.source.spps.SPCData;
import vsdk.source.spps.SpritePixelCollider;
import vsdk.source.utils.VPolygon2D;
import vsdk.source.vectors.Vector2Di;

import static com.raylib.Jaylib.BLACK;

import static com.raylib.Jaylib.WHITE;

import com.raylib.Jaylib.Vector3;

import static com.raylib.Jaylib.Vector3;

// EXAMPLE UPDATE DATE: 7/17/2024 12:37 AM (after commit "SPPS Update.").
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

        // SpritePixelCollider collider = new SpritePixelCollider(Raylib.LoadImage("g.png"));
        // SpritePixelCollider rectangle = new SpritePixelCollider(PixelColliderContainer.rectangle(23, 17));
        // rectangle.scaleCollider(30, 10);
        // rectangle.rotateCollider(60);

        SpritePixelCollider collider = new SpritePixelCollider(Raylib.LoadImage("g.png"));
        SpritePixelCollider rectangle = new SpritePixelCollider(PixelColliderContainer.polygon(new VPolygon2D.Polygon(150, 100, 100, 200, 200, 200)));

        long s = System.currentTimeMillis();
        collider.bake();
        System.out.println(System.currentTimeMillis() - s);

        int pointX = 250, pointY = 20;
        int pointW = 30, pointH = 10;

        int spriteX = 0, spriteY = 0;
        int spriteX2 = 340, spriteY2 = 250;

        Raylib.Texture tex = Raylib.LoadTextureFromImage(collider.getSpriteImage());

        while (!Raylib.WindowShouldClose()) {
            pointX = Raylib.GetMouseX();
            pointY = Raylib.GetMouseY();

            rectangle.bakeCollider(pointX, pointY);

            boolean i = collider.intersectsSPCBaked(spriteX, spriteY, rectangle);

            Raylib.UpdateCamera(cam, Raylib.CAMERA_FREE);

            Raylib.BeginDrawing();

            Raylib.ClearBackground(BLACK);

            // rectangle.debugCollider(new Vector2Di(pointX + 50, pointY));

            Raylib.DrawTexture(tex, spriteX, spriteY, WHITE);

            // Raylib.DrawRectanglePro(new Raylib.Rectangle().x(pointX).y(pointY).width(pointW).height(pointH), new Raylib.Vector2().x(pointW / 2).y(pointH / 2), 60, WHITE);
            // Raylib.DrawRectangle(pointX, pointY, pointW, pointH, WHITE);
            // Raylib.DrawLine( pointX + 0,  pointY + 0,  pointX + 50,  pointY + 80, WHITE);
            VPolygon2D.drawPoly2D(new VPolygon2D.Polygon(pointX + 150, pointY + 100, pointX + 100, pointY + 200, pointX + 200, pointY + 200), WHITE);

            SPCData.debug(550);

            Raylib.EndDrawing();
        }

        Raylib.CloseWindow();
    }
}
```

<img src="https://github.com/violent-studio/vsdk/assets/94743980/58f7891d-ba0f-4f4b-b89f-3317fc21e094">
