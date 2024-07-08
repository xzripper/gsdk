package vsdk.source;

import java.util.ArrayList;

/**
 * Pixel Collider Container.
 */
public class PixelColliderContainer {
    private int[][] points;

    /**
     * Initialize pixel collider points container.
     *
     * @param maxSize Maximal points size.
     */
    public PixelColliderContainer(int maxSize) {
        points = new int[maxSize][2];
    }

    /**
     * Set points array.
     *
     * @param points_ Array.
     */
    public void setPoints(int[][] points_) {
        points = points_;
    }

    /**
     * Rotate current points.
     *
     * @param angle Angle.
     * @param origin Rotation origin.
     */
    public void rotate(double angle, Vector2Di origin) {
        if(getPointsLength() <= 0) return;

        int[][] rotatedPoints = new int[getPointsLength()][2];

        for(int i=0; i < getPointsLength(); i++) {
            int[] rotatedPoint = rotatePoint(points[i][0], points[i][1], origin.x(), origin.y(), angle);

            rotatedPoints[i][0] = rotatedPoint[0];
            rotatedPoints[i][1] = rotatedPoint[1];
        }

        points = rotatedPoints;
    }

    /**
     * Get points array.
     */
    public int[][] getPoints() {
        return points;
    }

    /**
     * Get points array length.
     */
    public int getPointsLength() {
        return points.length;
    }

    /**
     * Rotate point.
     *
     * @param x X.
     * @param y Y.
     * @param originX Origin X.
     * @param originY Origin Y.
     * @param angle Angle.
     */
    public static int[] rotatePoint(int x, int y, int originX, int originY, double angle) {
        double radians = Math.toRadians(angle);

        return new int[] {
            (int) ((x - originX) * Math.cos(radians) - (y - originY) * Math.sin(radians)),
            (int) ((x - originX) * Math.sin(radians) + (y - originY) * Math.cos(radians))
        };
    }

    /**
     * Generate rectangle as points array.
     *
     * @param width Rectangle width.
     * @param height Rectangle height.
     */
    public static PixelColliderContainer rectangle(int width, int height) {
        ArrayList<int[]> points = new ArrayList<>();

        for(int y=0; y < height; y++) {
            for(int x=0; x < width; x++) {
                points.add(new int[] {x, y});
            }
        }

        return _createContainer(points);
    }

    /**
     * Generate rectangle as points array.
     *
     * @param radius Circle radius.
     */
    public static PixelColliderContainer circle(int radius) {
        ArrayList<int[]> points = new ArrayList<>();

        for(int y = -radius; y <= radius; y++) {
            for(int x = -radius; x <= radius; x++) {
                if(x * x + y * y <= radius * radius) {
                    points.add(new int[] {x, y});
                }
            }
        }

        return _createContainer(points);
    }

    private static PixelColliderContainer _createContainer(ArrayList<int[]> points) {
        PixelColliderContainer container = new PixelColliderContainer(points.size());

        int[][] pointsArray = new int[points.size()][2];

        points.toArray(pointsArray);

        container.setPoints(pointsArray);

        return container;
    }
}
