package gsdk.source.generic;

import com.raylib.Raylib;

import static com.raylib.Jaylib.BLANK;

import gsdk.source.vectors.Vector4Di;

import static gsdk.source.generic.VMath.tolerance;

/**
 * Utility class for filtering image pixels.
 */
public class ImagePixelsFilter {
    /**
     * Returns image without pixels with specified colors (custom threshold possible).
     *
     * @param image image.
     * @param rmRgba Color to remove.
     * @param threshold Color threshold.
     */
    public static Raylib.Image filterPixels(Raylib.Image image, Raylib.Color rmRgba, Vector4Di threshold) {
        Raylib.Image filteredImage = Raylib.GenImageColor(image.width(), image.height(), BLANK);

        for(int y = 0; y < image.height(); y++) {
            for(int x = 0; x < image.width(); x++) {
                Raylib.Color pixelColor = Raylib.GetImageColor(image, x, y);

                if(!tolerance(
                    new double[] {pixelColor.r(), pixelColor.g(), pixelColor.b(), pixelColor.a()},
                    new double[] {rmRgba.r(), rmRgba.g(), rmRgba.b(), rmRgba.a()},
                    new double[] {threshold.x(), threshold.y(), threshold.z(), threshold.w()}
                )) Raylib.ImageDrawPixel(filteredImage, x, y, pixelColor);
            }
        }

        return filteredImage;
    }

    /**
     * Returns image without pixels with specified colors.
     *
     * @param image image.
     * @param rmRgba Color to remove.
     */
    public static Raylib.Image filterPixels(Raylib.Image image, Raylib.Color rmRgba) {
        return filterPixels(image, rmRgba, new Vector4Di(0, 0, 0, 0));
    }
}
