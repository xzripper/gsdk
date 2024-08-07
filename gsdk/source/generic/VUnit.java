package gsdk.source.generic;

/**
 * Violent Unit (VU) (N: 10, VU: 72.5 [* 7.25]).
 */
public class VUnit {
    /**
     * Translate double number into double VU unit.
     * 
     * @param num Double number.
     */
    public static double tgud(double num) { return num * 7.25; }

    /**
     * Translate float number into float VU unit.
     * 
     * @param num Float number.
     */
    public static float tguf(float num) { return num * 7.25f; }

    /**
     * Translate integer number into integer VU unit.
     * 
     * @param num Integer number.
     */
    public static int tgui(int number) { return (int) (number * 7.25f); }

    /**
     * Translate double VU unit to stock number.
     * 
     * @param vu Double VUnit.
     */
    public static double ftgud(double vu) { return vu / 7.25; }

    /**
     * Translate float VU unit to stock number.
     * 
     * @param vu Float VUnit.
     */
    public static float ftguf(float vu) { return vu / 7.25f; }

    /**
     * Translate integer VU unit to stock number.
     * 
     * @param vu Integer VUnit.
     */
    public static int ftgui(float vu) { return (int) (vu / 7.25f); }
}
