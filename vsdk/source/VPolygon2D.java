package vsdk.source;

import com.raylib.Raylib;

import static vsdk.source.Assert.assert_t;

/**
 * Utility class for working with polygons.
 */
public class VPolygon2D {
    /**
     * Draw 2D Polygon.
     *
     * @param polygon Polygon container.
     * @param color Polygon color.
     */
    public static void drawPoly2D(Polygon polygon, Raylib.Color color) {
        for(int i = 0; i < polygon.getLength(); i++) {
            Raylib.DrawLineV(
                polygon.getVerticeVec2D(i).toRlVec(),
                polygon.getVerticeVec2D((i + 1) % polygon.getLength()).toRlVec(), color);
        }
    }

    /**
     * Polygon container.
     */
    public static class Polygon {
        private final int[][] vertices;

        /**
         * Create vertices array from each vertice. Vertices length should be even.
         *
         * @param vertices_ Vertices (points).
         */
        public Polygon(int ...vertices_) {
            assert_t(vertices_.length % 2 == 1, "vertices length should be even (vertices_.length % 2 == 1)");

            vertices = new int[vertices_.length / 2][2];

            for(int i=0; i < vertices_.length; i += 2) {
                vertices[i / 2][0] = vertices_[i];
                vertices[i / 2][1] = vertices_[i + 1];
            }
        }

        /**
         * Get vertices.
         */
        public int[][] getVertices() {
            return vertices;
        }

        /**
         * Get vertice as 2D vector.
         *
         * @param index Vertice index.
         */
        public Vector2Di getVerticeVec2D(int index) {
            return new Vector2Di(vertices[index][0], vertices[index][1]);
        }

        /**
         * Get vertices length.
         */
        public int getLength() {
            return vertices.length;
        }
    }
}
