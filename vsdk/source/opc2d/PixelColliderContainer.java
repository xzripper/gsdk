package vsdk.source.opc2d;

import java.util.ArrayList;

import vsdk.source.vectors.Vector2Di;

import vsdk.source.utils.VPolygon2D;

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
     * Rescale the points container proportionally.
     *
     * @param scaleWidth  New width of the container.
     * @param scaleHeight New height of the container.
     */
    public void scale(int scaleWidth, int scaleHeight) {
        int[] originalSize = compSize();

        ArrayList<int[]> scaled = new ArrayList<>();

        for(int y = 0; y < scaleHeight; y++) {
            for(int x = 0; x < scaleWidth; x++) {
                for(int[] point : points) {
                    if(point[0] == (int) (x / ((double) scaleWidth / originalSize[0])) &&
                        point[1] == (int) (y / ((double) scaleHeight / originalSize[1]))) {
                        scaled.add(new int[] {x, y}); break;
                    }
                }
            }
        }

        int[][] scaledArray = new int[scaled.size()][2];

        scaled.toArray(scaledArray);

        setPoints(scaledArray);
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
     * Compute points dimension based on points size.
     */
    public int[] compSize() {
        if(points.length <= 0) return new int[] {0, 0};

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
    
        for(int[] point : points) {
            if(point[0] < minX) minX = point[0];
            if(point[1] < minY) minY = point[1];

            if(point[0] > maxX) maxX = point[0];
            if(point[1] > maxY) maxY = point[1];
        }
    
        return new int[] {maxX - minX + 1, maxY - minY + 1};
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

        for(int y=0; y <= height; y++) {
            for(int x=0; x <= width; x++) {
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

    /**
     * Generate line as points array.
     * 
     * @param startX Line start X.
     * @param startY Line start Y.
     * @param endX Line end X.
     * @param endY Line end Y.
     */
    public static PixelColliderContainer line(int startX, int startY, int endX, int endY) {
        ArrayList<int[]> points = new ArrayList<>();

        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);

        int sx = startX < endX ? 1 : -1;
        int sy = startY < endY ? 1 : -1;

        int err = dx - dy;

        while(true) {
            points.add(new int[] {startX, startY});

            if(startX == endX && startY == endY) break;

            if((2 * err) > -dy) {
                err -= dy;

                startX += sx;
            }

            if((2 * err) < dx) {
                err += dx;

                startY += sy;
            }
        }

        return _createContainer(points);
    }

    /**
     * Generate polygon as points array.
     * 
     * @param polygon Polygon.
     */
    public static PixelColliderContainer polygon(VPolygon2D.Polygon polygon) {
        ArrayList<int[]> points = new ArrayList<>();

        for(int v=0; v < polygon.getLength(); v++) {
            Vector2Di thisVertice = polygon.getVerticeVec2D(v);
            Vector2Di nextVertice = polygon.getVerticeVec2D((v + 1) % polygon.getLength());

            PixelColliderContainer container = line(thisVertice.x(), thisVertice.y(), nextVertice.x(), nextVertice.y());

            for(int[] point : container.getPoints()) {
                points.add(point);
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
