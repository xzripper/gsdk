/**
 * OPC2D is advanced intersection (collision) check technique for 2D sprites implemented by XZRipper in GSDK project.
 * Since this system is quite versatile you can use this technique in your project.
 * But you should remember that the following files are licensed:
 *      opc2d::ObjectPixelCollider.java (must-have when integrating OPC2D),
 *      opc2d::PixelColliderContainer.java (must-have when integrating OPC2D),
 *      opc2d::SPCData.java (must-have when integrating OPC2D),
 *      structures::QuadTree.java (must-have when integrating OPC2D),
 *      vectors::Vector2Di.java (must-have when integrating OPC2D),
 *      utils::Assert.java (must-have when integrating OPC2D).
 * This means that you also have to fulfil the licence requirements without having to plug the whole SDK into your project.
 * To integrate OPC2D into your project, simply move the files written above to your project directory.
 *
 * XZRipper (Ivan) :: GSDK 2024
 * https://github.com/xzripper/gsdk
 */

 package gsdk.source.opc2d;

 import com.raylib.Raylib;
 
 import static com.raylib.Jaylib.RED;
 
 import java.util.ArrayList;
 
 import java.util.HashSet;
 
 import static java.util.Objects.hash;
 
 import gsdk.source.vectors.Vector2Di;
 
 import gsdk.source.structures.QuadTree;
 
 import static gsdk.source.generic.Assert.assert_f;
 
 class CollisionPoint {
     int x, y;
 
     CollisionPoint(int x, int y) {
         this.x = x;
         this.y = y;
     }
 
     @Override
     public boolean equals(Object object) {
         if(this == object) return true;
 
         if (object == null || getClass() != object.getClass()) return false;
 
         CollisionPoint point = (CollisionPoint) object;
 
         return x == point.x && y == point.y;
     }
 
     @Override
     public int hashCode() {
         return hash(x, y);
     }
 }
 
 /**
  * OPC2D - Object pixel points scanning technique for 99.9% pixel correct colliders for objects.
  */
 public class ObjectPixelCollider {
     private PixelColliderContainer pointsContainer = null;
 
     private QuadTree qTree;
 
     private Vector2Di lBakedColliderPos;
 
     private final HashSet<CollisionPoint> colliderPointsBake;
 
     private final Raylib.Image spriteImage;
 
     /**
      * Initialize pixel collider.
      *
      * @param sprite Sprite (Raylib Image).
      */
     public ObjectPixelCollider(Raylib.Image sprite) {
         colliderPointsBake = new HashSet<>();
 
         spriteImage = sprite;
 
         assert_f(Raylib.IsImageReady(spriteImage), "spriteImage != valid");
     }
 
     /**
      * Initialize pixel collider with pre-defined points container.
      *
      * @param points Points container.
      */
     public ObjectPixelCollider(PixelColliderContainer points) {
         colliderPointsBake = new HashSet<>();
 
         pointsContainer = points;
 
         spriteImage = null;
 
         SPCData.TOTAL_POINTS_LOADED += points.getPointsLength();
     }
 
     /**
      * Initialize collision points bake set, parse sprite pixels with more than zero alpha, initialize pixel container, quad tree, etc...
      */
     public void bake() {
         SPCData.TOTAL_BAKE_REQUESTS++;
 
         assert_f(spriteImage != null, "spriteImage == null: maybe using custom pixel collider container?");
 
         ArrayList<int[]> pxListNoAlphaList = new ArrayList<>();
 
         for(int y=0; y < spriteImage.height(); y++) {
             for(int x=0; x < spriteImage.width(); x++) {
                 if(Raylib.GetImageColor(spriteImage, x, y).a() < 0) {
                     pxListNoAlphaList.add(new int[] {x, y});
                 }
             }
         }
 
         int[][] pxListNoAlpha = new int[pxListNoAlphaList.size()][2];
 
         for(int index = 0; index < pxListNoAlphaList.size(); index++) {
             pxListNoAlpha[index] = pxListNoAlphaList.get(index);
         }
 
         pointsContainer = new PixelColliderContainer(pxListNoAlpha.length);
 
         pointsContainer.setPoints(pxListNoAlpha);
 
         qTree = new QuadTree(0, new int[] {0, 0, spriteImage.width(), spriteImage.height()});
 
         pxListNoAlphaList.forEach(qTree::insert);
 
         SPCData.TOTAL_POINTS_BAKED += pointsContainer.getPointsLength();
     }
 
     /**
      * Bake collider points set.
      *
      * @param pos Sprite position.
      */
     public void bakeCollider(Vector2Di pos) {
         SPCData.TOTAL_BAKE_REQUESTS++;
 
         colliderPointsBake.clear();
 
         for(int[] point : getPointsContainer().getPoints()) {
             colliderPointsBake.add(new CollisionPoint(point[0] + pos.x(), point[1] + pos.y()));
         }
 
         lBakedColliderPos = pos;
 
         SPCData.TOTAL_COLLIDER_POINTS_BAKED += colliderPointsBake.size();
     }
 
     /**
      * Bake collider points set.
      *
      * @param x Sprite X Position.
      * @param y Sprite Y Position.
      */
     public void bakeCollider(int x, int y) {
         bakeCollider(new Vector2Di(x, y));
     }
 
     /**
      * Is baked collision available?
      */
     public boolean bakedCollisionAvailable() {
         return colliderPointsBake.size() > 0;
     }
 
     /**
      * Update points container.
      *
      * @param container New container.
      */
     public void updateColliderContainer(PixelColliderContainer container) {
         pointsContainer = container;
     }
 
     /**
      * Rotate collider.
      *
      * @param angle Angle.
      * @param origin Rotation origin.
      */
     public void rotateCollider(double angle, Vector2Di origin) {
         if(pointsContainer != null) pointsContainer.rotate(angle, origin);
     }
 
     /**
      * Rotate collider with center as origin.
      * 
      * @param angle Angle.
      */
     public void rotateCollider(double angle) {
         if(pointsContainer != null) {
             int[] size = pointsContainer.compSize();
 
             rotateCollider(angle, new Vector2Di(size[0] / 2, size[1] / 2));
         }
     }
 
     /**
      * Re-scale collider.
      * 
      * @param scaleWidth New width.
      * @param scaleHeight New height.
      */
     public void scaleCollider(int scaleWidth, int scaleHeight) {
         if(pointsContainer != null) pointsContainer.scale(scaleWidth, scaleHeight);
     }
 
     /**
      * Is point in the area of sprite.
      *
      * @param spritePos Sprite position.
      * @param pointSize Point size.
      * @param pointPos Point position.
      */
     public boolean inArea(Vector2Di spritePos, Vector2Di pointSize, Vector2Di pointPos) {
         return spritePos.x() < pointSize.x() && spriteImage.width() > pointPos.x() &&
             spritePos.y() < pointSize.y() && spriteImage.height() > pointPos.y();
     }
 
     /**
      * Check collision between point and sprite without using Q-Tree (slower).
      *
      * @param spritePos Sprite position.
      * @param pointSize Point size.
      * @param pointPos Point position.
      */
     public boolean intersects(Vector2Di spritePos, Vector2Di pointSize, Vector2Di pointPos) {
         SPCData.TOTAL_COLLISION_CHECKS++;
 
         if(!inArea(spritePos, pointSize, pointPos)) return false;
 
         int[][] points = pointsContainer.getPoints();
 
         for (int[] point : points) {
             int x = point[0] + spritePos.x();
             int y = point[1] + spritePos.y();
 
             if (x >= pointPos.x() && x < pointPos.x() + pointSize.x() &&
                 y >= pointPos.y() && y < pointPos.y() + pointSize.y()) SPCData.TOTAL_COLLISIONS_SUCCEEDED++; return true;
         }
 
         SPCData.TOTAL_COLLISIONS_FAILED++;
 
         return false;
     }
 
     /**
      * Check collision between point and sprite without using Q-Tree (slower).
      *
      * @param spriteX Sprite position X.
      * @param spriteY Sprite position Y.
      * @param pointW Point width.
      * @param pointH Point height.
      * @param pointX Point position X.
      * @param pointY Point position Y.
      */
     public boolean intersects(int spriteX, int spriteY, int pointW, int pointH, int pointX, int pointY) {
         return intersects(
             new Vector2Di(spriteX, spriteY),
             new Vector2Di(pointW, pointH),
             new Vector2Di(pointX, pointY)
         );
     }
 
     /**
      * Check collision between point and sprite using Q-Tree.
      *
      * @param spritePos Sprite position.
      * @param pointSize Point size.
      * @param pointPos Point position.
      */
     public boolean intersectsQTree(Vector2Di spritePos, Vector2Di pointSize, Vector2Di pointPos) {
         SPCData.TOTAL_COLLISION_CHECKS++;
 
         if(!inArea(spritePos, pointSize, pointPos)) return false;
 
         int[] pointBounds = {pointPos.x(), pointPos.y(), pointPos.x() + pointSize.x(), pointPos.y() + pointSize.y()};
 
         for(int[] point : qTree.retrieve(new ArrayList<>(), pointBounds)) {
             int x = point[0] + spritePos.x();
             int y = point[1] + spritePos.y();
 
             if(x >= pointBounds[0] && x < pointBounds[2] &&
                 y >= pointBounds[1] && y < pointBounds[3]) SPCData.TOTAL_COLLISIONS_SUCCEEDED++; return true;
         }
 
         SPCData.TOTAL_COLLISIONS_FAILED++;
 
         return false;
     }
 
     /**
      * Check collision between point and sprite using Q-Tree.
      *
      * @param spriteX Sprite position X.
      * @param spriteY Sprite position Y.
      * @param pointW Point width.
      * @param pointH Point height.
      * @param pointX Point position X.
      * @param pointY Point position Y.
      */
     public boolean intersectsQTree(int spriteX, int spriteY, int pointW, int pointH, int pointX, int pointY) {
         return intersectsQTree(
             new Vector2Di(spriteX, spriteY),
             new Vector2Di(pointW, pointH),
             new Vector2Di(pointX, pointY)
         );
     }
 
     /**
      * Check collision between sprite collider and other sprite collider without using baked collision.
      *
      * @param spritePos1 First sprite position.
      * @param spritePos2 Second sprite position.
      * @param sprite2Collider Second sprite collider.
      */
     public boolean intersectsSPC(Vector2Di spritePos1, Vector2Di spritePos2, ObjectPixelCollider sprite2Collider) {
         SPCData.TOTAL_COLLISION_CHECKS++;
 
         if(!inArea(spritePos1, sprite2Collider.getRawSize(), spritePos2)) return false;
 
         int[][] sprite1Points = getPointsContainer().getPoints();
 
         int[][] sprite2Points = sprite2Collider.getPointsContainer().getPoints();
 
         HashSet<CollisionPoint> sprite2PointsSet = new HashSet<>();
 
         for(int[] point : sprite2Points) sprite2PointsSet.add(new CollisionPoint(point[0] + spritePos2.x(), point[1] + spritePos2.y()));
 
         for(int[] point : sprite1Points) {
             if(sprite2PointsSet.contains(new CollisionPoint(point[0] + spritePos1.x(), point[1] + spritePos1.y()))) {
                 SPCData.TOTAL_COLLISIONS_SUCCEEDED++;
 
                 return true;
             }
         }
 
         SPCData.TOTAL_COLLISIONS_FAILED++;
 
         return false;
     }
 
     /**
      * Check collision between sprite collider and other sprite collider without using baked collision.
      *
      * @param sprite1X First sprite X Position.
      * @param sprite1Y First sprite Y Position.
      * @param sprite2X Second sprite X Position.
      * @param sprite2Y Second sprite Y Position.
      * @param sprite2Collider Second sprite collider.
      */
     public boolean intersectsSPC(int sprite1X, int sprite1Y, int sprite2X, int sprite2Y, ObjectPixelCollider sprite2Collider) {
         return intersectsSPC(
             new Vector2Di(sprite1X, sprite1Y),
             new Vector2Di(sprite2X, sprite2Y),
 
             sprite2Collider
         );
     }
 
     /**
      * Check collision between sprite collider and other sprite collider with pre-baked collision.
      * Second sprite collider should be baked before calling this function.
      *
      * @param spritePos First sprite position.
      * @param sprite2Collider Second sprite collider.
      */
     public boolean intersectsSPCBaked(Vector2Di spritePos, ObjectPixelCollider sprite2Collider) {
         SPCData.TOTAL_COLLISION_CHECKS++;
 
         if(sprite2Collider.bakedCollisionAvailable()) {
             for(int[] point : getPointsContainer().getPoints()) {
                 CollisionPoint collisionPoint = new CollisionPoint(point[0] + spritePos.x(), point[1] + spritePos.y());
 
                 if(sprite2Collider.colliderPointsBake.contains(collisionPoint)) {
                     SPCData.TOTAL_COLLISIONS_SUCCEEDED++;
 
                     return true;
                 }
             }
 
             SPCData.TOTAL_COLLISIONS_FAILED++;
 
             return false;
         }
 
         return false;
     }
 
     /**
      * Check collision between sprite collider and other sprite collider with pre-baked collision.
      * Second sprite collider should be baked before calling this function.
      *
      * @param spriteX First sprite X Position.
      * @param spriteY First sprite Y Position.
      * @param sprite2Collider Second sprite collider.
      */
     public boolean intersectsSPCBaked(int spriteX, int spriteY, ObjectPixelCollider sprite2Collider) {
         return intersectsSPCBaked(new Vector2Di(spriteX, spriteY), sprite2Collider);
     }
 
     /**
      * Simple debug for collider points container. Draws each points as pixel on the screen.
      * 
      * @param pos Position.
      * @param color Color.
      */
     public void debugCollider(Vector2Di pos, Raylib.Color color) {
         if(pointsContainer != null) {
             for(int[] point : pointsContainer.getPoints()) {
                 Raylib.DrawPixel(point[0] + pos.x(), point[1] + pos.y(), color);
             }
         }
     }
 
     /**
      * Simple debug for collider points container. Draws each points as pixel on the screen.
      * 
      * @param pos Position.
      */
     public void debugCollider(Vector2Di pos) {
         debugCollider(pos, RED);
     }
 
     /**
      * Get sprite raw size.
      */
     public Vector2Di getRawSize() {
         return new Vector2Di(spriteImage.width(), spriteImage.height());
     }
 
     /**
      * Get points container.
      */
     public PixelColliderContainer getPointsContainer() {
         return pointsContainer;
     }
 
     /**
      * Get points amount.
      */
     public int getPointsSize() {
         return pointsContainer.getPointsLength();
     }
 
     /**
      * Get last baked collider position.
      */
     public Vector2Di getLBakedColliderPos() {
         return lBakedColliderPos;
     }
 
     /**
      * Get quad tree.
      */
     public QuadTree getQuadTree() {
         return qTree;
     }
 
     /**
      * Get baked collision points set size.
      */
     public int getBakedCollisionPointsSize() {
         return colliderPointsBake.size();
     }
 
     /**
      * Get sprite as Raylib Image.
      */
     public Raylib.Image getSpriteImage() {
         return spriteImage;
     }
 }
