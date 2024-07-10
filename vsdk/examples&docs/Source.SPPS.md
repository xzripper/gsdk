Example of using SPPS (made example while developing SPPS).

```java
import com.raylib.Raylib;
import vsdk.source.spps.PixelColliderContainer;
import vsdk.source.spps.SPCData;
import vsdk.source.spps.SpritePixelCollider;
import vsdk.source.vectors.Vector2Di;

import static com.raylib.Jaylib.BLACK;

import static com.raylib.Jaylib.WHITE;

public class Main {
    public static void main(String[] args) {
        Raylib.InitWindow(1000, 800, "Test window.");

        Raylib.SetTargetFPS(60);

        SpritePixelCollider collider = new SpritePixelCollider(Raylib.LoadImage("src\\axis.png"));
        SpritePixelCollider rectangle = new SpritePixelCollider(PixelColliderContainer.rectangle(10, 10));
        rectangle.rotateCollider(40, new Vector2Di(5, 5));

        collider.bake();

        int pointX = 250, pointY = 20;
        int pointW = 10, pointH = 10;

        int spriteX = 0, spriteY = 0;
        int spriteX2 = 340, spriteY2 = 250;

        Raylib.Texture tex = Raylib.LoadTextureFromImage(collider.getSpriteImage());

        while (!Raylib.WindowShouldClose()) {
            pointX = Raylib.GetMouseX();
            pointY = Raylib.GetMouseY();

            rectangle.bakeCollision(pointX, pointY);

            boolean i = collider.intersectsSPCBaked(spriteX, spriteY, rectangle);

            Raylib.UpdateCamera(cam, Raylib.CAMERA_FREE);

            Raylib.BeginDrawing();

            Raylib.ClearBackground(BLACK);

            Raylib.DrawTexture(tex, spriteX, spriteY, WHITE);

            Raylib.DrawRectanglePro(new Raylib.Rectangle().x(pointX).y(pointY).width(pointW).height(pointH), new Raylib.Vector2().x(5).y(5), 40, WHITE);

            SPCData.debug(550);

            Raylib.EndDrawing();
        }

        Raylib.CloseWindow();
    }
}
```

<img src="https://github.com/violent-studio/vsdk/assets/94743980/58f7891d-ba0f-4f4b-b89f-3317fc21e094">
