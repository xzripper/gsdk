package gsdk.source.vrender;

import gsdk.source.vectors.Vector3Df;
import gsdk.source.vectors.Vector2Df;

import static gsdk.source.vectors.Vector3Df.vec3df;

import com.raylib.Raylib;

/**
 * Class for rendering quads.
 */
public class Quad {
    /**
     * Generate quad vertex coordinates.
     * 
     * @param size Quad size.
     * @param pos Quad position.
     * @param rotation Quad rotation.
     */
    public static Vector3Df[] genQuadVertCoords(Vector2Df size, Vector3Df pos, Vector3Df rotation) {
        Vector3Df[] vertices = new Vector3Df[] {
            vec3df(-size.x() / 2.0f, 0, -size.y() / 2.0f),
            vec3df(size.x() / 2.0f, 0, -size.y() / 2.0f),
            vec3df(size.x() / 2.0f, 0, size.y() / 2.0f),
            vec3df(-size.x() / 2.0f, 0, size.y() / 2.0f)
        };

        double rotX = Math.toRadians(rotation.x()), rotY = Math.toRadians(rotation.y()), rotZ = Math.toRadians(rotation.z());
    
        for(int i = 0; i < vertices.length; i++) {
            Vector3Df vert = vertices[i];
    
            float tY = vert.y();

            vert.y((float) (vert.y() * Math.cos(rotX) - vert.z() * Math.sin(rotX)));
            vert.z((float) (tY * Math.sin(rotX) + vert.z() * Math.cos(rotX)));
    
            float tX = vert.x();

            vert.x((float) (vert.x() * Math.cos(rotY) + vert.z() * Math.sin(rotY)));
            vert.z((float) (-tX * Math.sin(rotY) + vert.z() * Math.cos(rotY)));
    
            tX = vert.x();

            vert.x((float) (vert.x() * Math.cos(rotZ) - vert.y() * Math.sin(rotZ)));
            vert.y((float) (tX * Math.sin(rotZ) + vert.y() * Math.cos(rotZ)));
    
            vertices[i] = vec3df(vert.x() + pos.x(), vert.y() + pos.y(), vert.z() + pos.z());
        }
    
        return vertices;
    }

    /**
     * Generate quad vertex coordinates with zero origin and rotation.
     * 
     * @param size Quad size.
     * @param pos Quad position.
     */
    public static Vector3Df[] genQuadVertCoords(Vector2Df size, Vector3Df pos) {
        return genQuadVertCoords(size, pos, vec3df(0, 0, 0));
    }

    /**
     * Draw 3D Quad mesh.
     * 
     * @param size Quad size.
     * @param pos Quad position.
     * @param rotation Quad rotation.
     * @param color Quad color.
     * @param backface Render Quad backface?
     */
    public static void drawQuad(Vector2Df size, Vector3Df pos, Vector3Df rotation, Raylib.Color color, boolean backface) {
        Vector3Df[] vertCoords = genQuadVertCoords(size, pos, rotation);

        Raylib.DrawTriangle3D(vertCoords[0].toRlVec(), vertCoords[1].toRlVec(), vertCoords[2].toRlVec(), color);
        Raylib.DrawTriangle3D(vertCoords[0].toRlVec(), vertCoords[2].toRlVec(), vertCoords[3].toRlVec(), color);

        if(backface)  {
            Raylib.DrawTriangle3D(vertCoords[2].toRlVec(), vertCoords[1].toRlVec(), vertCoords[0].toRlVec(), color);
            Raylib.DrawTriangle3D(vertCoords[3].toRlVec(), vertCoords[2].toRlVec(), vertCoords[0].toRlVec(), color);
        }
    }

    /**
     * Draw 3D Quad mesh with backface culling.
     * 
     * @param size Quad size.
     * @param pos Quad position.
     * @param rotation Quad rotation.
     * @param color Quad color.
     */
    public static void drawQuad(Vector2Df size, Vector3Df pos, Vector3Df rotation, Raylib.Color color) {
        drawQuad(size, pos, color, false);
    }

    /**
     * Draw 3D Quad mesh with default rotation.
     * 
     * @param size Quad size.
     * @param pos Quad position.
     * @param color Quad color.
     * @param backface Render Quad backface?
     */
    public static void drawQuad(Vector2Df size, Vector3Df pos, Raylib.Color color, boolean backface) {
        Vector3Df[] vertCoords = genQuadVertCoords(size, pos);

        Raylib.DrawTriangle3D(vertCoords[0].toRlVec(), vertCoords[1].toRlVec(), vertCoords[2].toRlVec(), color);
        Raylib.DrawTriangle3D(vertCoords[0].toRlVec(), vertCoords[2].toRlVec(), vertCoords[3].toRlVec(), color);

        if(backface) {
            Raylib.DrawTriangle3D(vertCoords[2].toRlVec(), vertCoords[1].toRlVec(), vertCoords[0].toRlVec(), color);
            Raylib.DrawTriangle3D(vertCoords[3].toRlVec(), vertCoords[2].toRlVec(), vertCoords[0].toRlVec(), color);
        }
    }

    /**
     * Draw 3D Quad mesh with default rotation and backface culling.
     * 
     * @param size Quad size.
     * @param pos Quad position.
     * @param color Quad color.
     */
    public static void drawQuad(Vector2Df size, Vector3Df pos, Raylib.Color color) {
        drawQuad(size, pos, color, false);
    }
}
