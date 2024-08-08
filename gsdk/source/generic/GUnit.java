package gsdk.source.generic;

/**
 * Game Unit (GU) (N: 10, gU: 72.5 [* 7.25]).
 */
public class GUnit {
    /**
     * Translate double number into double GU unit.
     * 
     * @param num Double number.
     */
    public static double tgud(double num) { return num * 7.25; }

    /**
     * Translate float number into float GU unit.
     * 
     * @param num Float number.
     */
    public static float tguf(float num) { return num * 7.25f; }

    /**
     * Translate integer number into integer GU unit.
     * 
     * @param num Integer number.
     */
    public static int tgui(int number) { return (int) (number * 7.25f); }

    /**
     * Translate double GU unit to stock number.
     * 
     * @param GU Double GUnit.
     */
    public static double ftgud(double GU) { return GU / 7.25; }

    /**
     * Translate float GU unit to stock number.
     * 
     * @param GU Float GUnit.
     */
    public static float ftguf(float GU) { return GU / 7.25f; }

    /**
     * Translate integer GU unit to stock number.
     * 
     * @param GU Integer GUnit.
     */
    public static int ftgui(float GU) { return (int) (GU / 7.25f); }
}
