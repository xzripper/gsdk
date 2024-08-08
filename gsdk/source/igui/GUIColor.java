package gsdk.source.igui;

import com.raylib.Jaylib;

/**
 * GUI Color.
 */
public class GUIColor {
    private int r, g, b, a;

    /**
     * Create new GUI color.
     *
     * @param r_ Red.
     * @param g_ Green.
     * @param b_ Blue.
     * @param a_ Alpha.
     */
    public GUIColor(int r_, int g_, int b_, int a_) {
        r = r_;
        g = g_;
        b = b_;
        a = a_;
    }

    /**
     * Get GUI color.
     *
     * @param color Color (e.g 'r' / etc).
     */
    public int get(char color) {
        return switch(color) {
            case 'r' -> r;
            case 'g' -> g;
            case 'b' -> b;
            case 'a' -> a;
            default -> -1;
        };
    }

    /**
     * Set GUI color.
     *
     * @param color Color (e.g 'r' / etc).
     * @param newCol New color.
     */
    public void set(char color, int newCol) {
        switch(color) {
            case 'r' -> r = newCol;
            case 'g' -> g = newCol;
            case 'b' -> b = newCol;
            case 'a' -> a = newCol;
        }
    }

    /**
     * Cast GUIColor to Raylib Color.
     */
    public Jaylib.Color toRlCol() {
        return new Jaylib.Color(r, g, b, a);
    }
}
