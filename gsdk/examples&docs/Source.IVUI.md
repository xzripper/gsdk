IVUI is an immediate mode graphical user interface designed for simple, fast, customizable UI for applications/games.

### Example of using:

```java
import com.raylib.Raylib;

import gsdk.source.grender.Texture;

import gsdk.source.generic.VMath;

import gsdk.source.vectors.Vector2Df;

import gsdk.source.igui.*;

public class Main {
    public static void main(String[] args) {
        Raylib.InitWindow(1000, 800, "Test window.");

        Raylib.SetTargetFPS(60);

        GUI.newVuiCtx(null);

        GUI.loadTexelBleedingFixShader();

        var rBtnGroup = GUI.allocRBGroup(3, 1);

        var state = GUI.newStateRef();

        var specialStyle = new GUIStyle();
        var specialStyle2 = new GUIStyle();

        specialStyle2.setTextSize(14);

        var pBar = GUI.newRef(0);

        var string = GUI.newRef(0);

        var slider = GUI.newRef(255.0f);

        Texture tex = new Texture("src\\RES\\bhole.png");

        while (!Raylib.WindowShouldClose()) {
            Raylib.BeginDrawing();

            Raylib.ClearBackground(BLACK);

            if (GUI.hollowRectangle(230, 210, 480, 180)) System.out.println("Clicked rectangle inside!");

            GUI.beginStyle(specialStyle);

            GUI.text("Hover me!", 393, 300);

            int dist = (int) VMath.dist2D(new Vector2Df(Raylib.GetMouseX(), Raylib.GetMouseY()), new Vector2Df(420, 310));

            if (GUI.hollowRectangle(375, 285, 100, 50, true)) {
                GUIColor color = new GUIColor(255 - dist, dist, 148 + dist, 255);

                specialStyle.setTextCol(color);

                specialStyle.setBorderColor(color);
            } else {
                specialStyle.setTextCol(GUI.getFinalStyle().getTextCol());

                specialStyle.setBorderColor(GUI.getFinalStyle().getBorderColor());
            }

            GUI.endStyle();

            GUI.beginStyle(specialStyle2);
            GUI.text(String.format("Dist2D(Mouse, HoverMe): %d", dist), 510, 355);
            GUI.endStyle();

            pBar.set(dist);

            if (GUI.text("Hello GUI!", 250, 225)) System.out.println("Hello GUI!");

            if (GUI.button("Hello World!", 250, 250)) {
                System.out.println("Button clicked!");

                rBtnGroup.switchAll(1);
            }

            if (GUI.buttonRadio(rBtnGroup, 1, "\"Hello\"", 255, 290)) System.out.println("Selected hello.");

            if (GUI.buttonRadio(rBtnGroup, 2, "\"Bye\"", 255, 315)) System.out.println("Selected bye.");

            if (GUI.buttonRadio(rBtnGroup, 3, "\"What is\"", 255, 340)) System.out.println("Selected what is.");

            if (GUI.text(String.format("%s world!", rBtnGroup.isActive(1) ? "Hello" : rBtnGroup.isActive(2) ? "Bye" : "What is"), 250, 355)) {
                System.out.printf("%s world!%n", rBtnGroup.isActive(1) ? "Hello" : rBtnGroup.isActive(2) ? "Bye" : "What is");
            }

            if (GUI.checkbox(state, "I'm checkbox 1!", 375, 225)) System.out.println("Checkbox 1 clicked!");

            if (GUI.checkbox(state, "I'm checkbox 2 with same state reference!", 375, 250))
                System.out.println("Checkbox 2 clicked!");

            GUI.progressBar(pBar, 375, 350);

            if (GUI.image(tex, 490, 290, 0.1f)) {
                System.out.println("Bullet hole!");
            }

            if (GUI.rectangle(535, 295, 30, 30, new GUIColor(
                (int) VMath.clamp(0, 255, dist / 2.0f), 0, 0,

                (int) VMath.clamp(0, slider.get(), VMath.dist2D(
                    new Vector2Df(Raylib.GetMouseX(), Raylib.GetMouseY()),
                    new Vector2Df(550, 310)))))) {
                System.out.println("Clicked rectangle!");
            }

            if (GUI.stringSlider(string, new String[]{"Fire", "Air", "Earth", "Water"}, 590, 300)) {
                System.out.println(new String[]{"Fire", "Air", "Earth", "Water"}[string.get()]);
            }

            if (GUI.floatSlider(slider, 0.0f, 255.0f, 570, 284)) {
                System.out.println(slider.get());
            }

            Raylib.EndDrawing();
        }

        GUI.unloadTexelBleedingFixShader();

        Raylib.CloseWindow();
    }
}

```
