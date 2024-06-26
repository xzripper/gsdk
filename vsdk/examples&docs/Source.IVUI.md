IVUI is an immediate mode graphical user interface designed for simple, fast, customizable UI for applications/games.

### Example of using:
```java
import com.raylib.Raylib;

import vsdk.source.Texture;

import vsdk.source.VMath;

import vsdk.source.Vector2Df;

import vsdk.source.ivui.*;

public class Main {
    public static void main(String[] args) {
        Raylib.InitWindow(1000, 800, "Test window.");

        Raylib.SetTargetFPS(60);

        VUI.newVuiCtx(null);

        VUI.loadTexelBleedingFixShader();

        var rBtnGroup = VUI.allocRBGroup(3, 1);

        var state = VUI.newStateRef();

        var specialStyle = new VUIStyle();
        var specialStyle2 = new VUIStyle();

        specialStyle2.setTextSize(14);

        var pBar = VUI.newRef(0);

        var string = VUI.newRef(0);

        var slider = VUI.newRef(255.0f);

        Texture tex = new Texture("src\\RES\\bhole.png");

        while(!Raylib.WindowShouldClose()) {
            Raylib.BeginDrawing();

            Raylib.ClearBackground(BLACK);

            if(VUI.hollowRectangle(230, 210, 480, 180)) System.out.println("Clicked rectangle inside!");

            VUI.beginStyle(specialStyle);

            VUI.text("Hover me!", 393, 300);

            int dist = (int) VMath.dist2D(new Vector2Df(Raylib.GetMouseX(), Raylib.GetMouseY()), new Vector2Df(420, 310));

            if(VUI.hollowRectangle(375, 285, 100, 50, true)) {
                VUIColor color = new VUIColor(255 - dist, dist, 148 + dist, 255);

                specialStyle.setTextCol(color);

                specialStyle.setBorderColor(color);
            } else {
                specialStyle.setTextCol(VUI.getFinalStyle().getTextCol());

                specialStyle.setBorderColor(VUI.getFinalStyle().getBorderColor());
            }

            VUI.endStyle();

            VUI.beginStyle(specialStyle2);
            VUI.text(String.format("Dist2D(Mouse, HoverMe): %d", dist), 510, 355);
            VUI.endStyle();

            pBar.set(dist);

            if(VUI.text("Hello VUI!", 250, 225)) System.out.println("Hello VUI!");

            if(VUI.button("Hello World!", 250, 250)) {
                System.out.println("Button clicked!");

                rBtnGroup.switchAll(1);
            }

            if(VUI.buttonRadio(rBtnGroup, 1, "\"Hello\"", 255, 290)) System.out.println("Selected hello.");

            if(VUI.buttonRadio(rBtnGroup, 2, "\"Bye\"", 255, 315)) System.out.println("Selected bye.");

            if(VUI.buttonRadio(rBtnGroup, 3, "\"What is\"", 255, 340)) System.out.println("Selected what is.");

            if(VUI.text(String.format("%s world!", rBtnGroup.isActive(1) ? "Hello" : rBtnGroup.isActive(2) ? "Bye" : "What is"), 250, 355)) {
                System.out.printf("%s world!%n", rBtnGroup.isActive(1) ? "Hello" : rBtnGroup.isActive(2) ? "Bye" : "What is");
            }

            if(VUI.checkbox(state, "I'm checkbox 1!", 375, 225)) System.out.println("Checkbox 1 clicked!");

            if(VUI.checkbox(state, "I'm checkbox 2 with same state reference!", 375, 250)) System.out.println("Checkbox 2 clicked!");

            VUI.progressBar(pBar, 375, 350);

            if(VUI.image(tex, 490, 290, 0.1f)) {
                System.out.println("Bullet hole!");
            }

            if(VUI.rectangle(535, 295, 30, 30, new VUIColor(
                    (int) VMath.clamp(0, 255, dist / 2.0f), 0, 0,

                    (int) VMath.clamp(0, slider.get(), VMath.dist2D(
                            new Vector2Df(Raylib.GetMouseX(), Raylib.GetMouseY()),
                            new Vector2Df(550, 310)))))) {
                System.out.println("Clicked rectangle!");
            }

            if(VUI.stringSlider(string, new String[] {"Fire", "Air", "Earth", "Water"}, 590, 300)) {
                System.out.println(new String[] {"Fire", "Air", "Earth", "Water"}[string.get()]);
            }

            if(VUI.floatSlider(slider, 0.0f, 255.0f, 570, 284)) {
                System.out.println(slider.get());
            }

            Raylib.EndDrawing();
        }

        VUI.unloadTexelBleedingFixShader();

        Raylib.CloseWindow();
    }
}

```
