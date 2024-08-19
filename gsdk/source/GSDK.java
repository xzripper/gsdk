package gsdk.source;

import static gsdk.source.generic.GLogger.error;

import static gsdk.glib.futils.FReadUtil.readLines;

import static gsdk.r_utilities.PathResolver.resolvePath;

import static gsdk.source.generic.Assert.assert_f;

/**
 * Class containing GSDK information and credits.
 */
public class GSDK {
    /**
     * GSDK Version.
     */
    public static final String VERSION;

    /**
     * GSDK Developers array.
     */
    public static final String[] DEVS;

    /**
     * GSDK License.
     */
    public static final String LICENSE;

    /**
     * GSDK Last release year.
     */
    public static final int LR_YEAR;

    static {
        String[] cinfLines = readLines(resolvePath("gsdk/gsdk.cinf"));

        assert_f(cinfLines.length == 5, "invalid cinfLines length");

        VERSION = cinfLines[1];

        DEVS = cinfLines[2].split("\\|");

        LICENSE = cinfLines[3];

        int lrYear = 0;

        try {
            lrYear = Integer.parseInt(cinfLines[4]);
        } catch(NumberFormatException numFormatExc) {
            error("Failed to parse LR_YEAR.");
        }

        LR_YEAR = lrYear;
    }
}
