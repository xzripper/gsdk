package vsdk.source.structures;

import java.util.ArrayList;

import java.util.List;

/**
 * VSDK implementation of QuadTree. What is quadtree?:
 * A quadtree is a tree data structure in which each internal node has exactly four children.
 * Quadtrees are the two-dimensional analog of octrees and are most often used to partition a two-dimensional space by recursively subdividing it into four quadrants or regions.
 * The data associated with a leaf cell varies by application, but the leaf cell represents a "unit of interesting spatial information".
 */
public class QuadTree {
    public static final int MAX_LEVELS = 5;
    public static final int MAX_POINTS = 4;

    public List<int[]> points;

    public QuadTree[] nodes;

    public int level;

    public int[] bounds;

    /**
     * Initialize QuadTree.
     *
     * @param level_ Quad level.
     * @param bounds_ Desired bounds.
     */
    public QuadTree(int level_, int[] bounds_) {
        points = new ArrayList<>();
        nodes = new QuadTree[4];

        level = level_;
        bounds = bounds_;
    }

    /**
     * Clear points and nodes.
     */
    public void clear() {
        points.clear();

        for (int i = 0; i < nodes.length; i++) {
            if(nodes[i] != null) {
                nodes[i].clear();

                nodes[i] = null;
            }
        }
    }

    /**
     * Splits the current node into four subnodes.
     *
     * This method divides the current node's bounding box into four equal
     * quadrants, creating new child nodes for each quadrant. This is typically
     * done when the node exceeds its maximum capacity, allowing for more efficient
     * spatial partitioning.
     */
    public void split() {
        int subWidth = (bounds[2] - bounds[0]) / 2;
        int subHeight = (bounds[3] - bounds[1]) / 2;

        int x = bounds[0];
        int y = bounds[1];

        nodes[0] = new QuadTree(level + 1, new int[] {x + subWidth, y, x + subWidth * 2, y + subHeight});
        nodes[1] = new QuadTree(level + 1, new int[] {x, y, x + subWidth, y + subHeight});
        nodes[2] = new QuadTree(level + 1, new int[] {x, y + subHeight, x + subWidth, y + subHeight * 2});
        nodes[3] = new QuadTree(level + 1, new int[] {x + subWidth, y + subHeight, x + subWidth * 2, y + subHeight * 2});
    }

    /**
     * Determines which quadrant of the current node's bounds contains the given point.
     *
     * @param point The point to be checked, represented as an array [x, y].
     */
    private int getIndex(int[] point) {
        int index = -1;

        double vertMidp = bounds[0] + (bounds[2] - bounds[0]) / 2.0;
        double horMidp = bounds[1] + (bounds[3] - bounds[1]) / 2.0;

        if(point[0] < vertMidp) index = (point[1] < horMidp) ? 1 : (point[1] > horMidp) ? 2 : index;
        else if(point[0] > vertMidp) index = (point[1] < horMidp) ? 0 : (point[1] > horMidp) ? 3 : index;

        return index;
    }

    /**
     * Inserts a point into the quadtree. If the node exceeds the capacity, it splits
     * and distributes the points among the subnodes.
     *
     * @param point The point to be inserted, represented as an array [x, y].
     */
    public void insert(int[] point) {
        if(nodes[0] != null) {
            int index = getIndex(point);

            if(index != -1) {
                nodes[index].insert(point);

                return;
            }
        }

        points.add(point);

        if(points.size() > MAX_POINTS && level < MAX_LEVELS) {
            if(nodes[0] == null) {
                split();
            }

            int i = 0;

            while (i < points.size()) {
                int index = getIndex(points.get(i));

                if(index != -1) nodes[index].insert(points.remove(i));
                else i++;
            }
        }
    }

    /**
     * Retrieves all points that could collide with the given point.
     *
     * @param returnPoints The list to store the retrieved points.
     * @param point The point to check for possible collisions, represented as an array [x, y].
     */
    public List<int[]> retrieve(List<int[]> returnPoints, int[] point) {
        int index = getIndex(point);

        if(index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnPoints, point);
        }

        returnPoints.addAll(points);

        return returnPoints;
    }
}
