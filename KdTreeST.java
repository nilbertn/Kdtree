import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class KdTreeST<Value> {
    // root node instance variable
    private Node root;
    private int size; //

    private class Node {
        private Point2D p; // point
        private Value val; // value associated with the point in the symbol table
        private RectHV rect; // rectangle
        private Node leftBottom; // the left/bottom subtree
        private Node rightTop; // the right/top subtree
        private boolean xcoord; // tracks what level the BST (to use the x or y
        // coordinate)

        // Node constructor
        public Node(Point2D p, Value value, RectHV rect, boolean xcoord) {
            this.p = p;
            this.val = value;
            this.rect = rect;
            this.xcoord = xcoord;
        }
    }

    // Constructor for the KD-Tree
    public KdTreeST() {
        root = null;
        size = 0;
    }

    // Returns whether the kd tree is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the size of te kd tree
    public int size() {
        return size;
    }

    // puts a 2D point p with a certain value into the kd tree
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new IllegalArgumentException();
        root = put(root, p, val, true,
                   new RectHV(0.0, 0.0,
                              1.0, 1.0));

    }

    // Private helper method that checks the level of the tree and the
    // relevant rectangle boundary conditions to put the point in the tree
    private Node put(Node node, Point2D p, Value val, boolean xcoord, RectHV rect) {
        if (node == null) {
            size++;
            return new Node(p, val, rect, xcoord);
        }
        // case when node = p
        if (node.p.equals(p)) {
            node.val = val;
            return node;
        }

        if (xcoord) {
            if (p.x() < node.p.x()) {
                node.leftBottom = put(node.leftBottom, p, val, false,
                                      new RectHV(rect.xmin(), rect.ymin(), node.p.x(),
                                                 rect.ymax()));
            }
            else {
                node.rightTop = put(node.rightTop, p, val, false,
                                    new RectHV(node.p.x(), rect.ymin(), rect.xmax(),
                                               rect.ymax()));
            }
        }
        else {
            if (p.y() < node.p.y()) {
                node.leftBottom = put(node.leftBottom, p, val, true,
                                      new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                                                 node.p.y()));
            }
            else {
                node.rightTop = put(node.rightTop, p, val, true,
                                    new RectHV(rect.xmin(), node.p.y(), rect.xmax(),
                                               rect.ymax()));
            }
        }

        return node;
    }

    // returns the value associated with the Point2D input
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p, true);
    }

    // private helper method to find the value associated with the point
    private Value get(Node node, Point2D p, boolean xcoord) {
        if (node == null || p == null) return null;
        if (node.p.equals(p)) return node.val;
        if (xcoord) {
            if (p.x() < node.p.x()) return get(node.leftBottom, p, false);
            else return get(node.rightTop, p, false);
        }
        else {
            if (p.y() < node.p.y()) return get(node.leftBottom, p, true);
            else return get(node.rightTop, p, true);
        }

    }

    // checks whether the kd tree contains the input point p
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(p) != null;
    }

    // find the nearest point in the tree to an input point p
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, p, root.p, true);
    }

    // private helper method that calculates which point in the tree is nearest
    private Point2D nearest(Node node, Point2D point, Point2D nearest,
                            boolean xcoord) {
        if (node == null) return nearest;
        double nearestDist1 = nearest.distanceSquaredTo(point);
        double nearestDist2 = node.rect.distanceSquaredTo(point);

        if (Double.compare(nearestDist2, nearestDist1) < 0) {
            double distanceToNode = node.p.distanceSquaredTo(point);
            if (Double.compare(distanceToNode, nearestDist1) < 0) {
                nearest = node.p;
                nearestDist1 = distanceToNode;
            }

            // Determine which subtree to search first
            if (xcoord) {
                if (point.x() < node.p.x()) {
                    nearest = nearest(node.leftBottom, point, nearest, false);
                    nearest = nearest(node.rightTop, point, nearest, false);
                }
                else {
                    nearest = nearest(node.rightTop, point, nearest, false);
                    nearest = nearest(node.leftBottom, point, nearest, false);
                }
            }
            else {
                if (point.y() < node.p.y()) {
                    nearest = nearest(node.leftBottom, point, nearest, true);
                    nearest = nearest(node.rightTop, point, nearest, true);
                }
                else {
                    nearest = nearest(node.rightTop, point, nearest, true);
                    nearest = nearest(node.leftBottom, point, nearest, true);
                }
            }
        }

        return nearest;
    }

    // Method that iterates across all points in the tree
    public Iterable<Point2D> points() {
        List<Point2D> points = new LinkedList<>();
        if (root == null) return points;

        Queue<Node> p = new Queue<>();
        p.enqueue(root);
        while (!p.isEmpty()) {
            Node current = p.dequeue();
            points.add(current.p);
            if (current.leftBottom != null)
                p.enqueue(current.leftBottom);
            if (current.rightTop != null)
                p.enqueue(current.rightTop);

        }
        return points;
    }

    // iterates across all 2d points as range search
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        return rangeSearch(root, rect, new LinkedList<>());
    }

    // Private helper method to perform a range search
    private List<Point2D> rangeSearch(Node node, RectHV rect, List<Point2D> inRect) {
        // Base
        if (node == null) return inRect;
        if (rect.contains(node.p)) inRect.add(node.p);

        // Recurse through the left/top subtree points
        if (node.leftBottom != null && rect.intersects(node.leftBottom.rect)) {
            rangeSearch(node.leftBottom, rect, inRect);
        }

        // Recurse through the right/bottom subtree points
        if (node.rightTop != null && node.rightTop.rect.intersects(rect)) {
            rangeSearch(node.rightTop, rect, inRect);
        }
        return inRect;
    }

    public static void main(String[] args) {

        // Sample kd tree
        KdTreeST<Double> kd = new KdTreeST<>();
        // Sample points for testing
        Point2D[] points = {
                new Point2D(0.7, 0.2),
                new Point2D(0.5, 0.4),
                new Point2D(0.2, 0.3),
                new Point2D(0.4, 0.7),
                new Point2D(0.9, 0.6)
        };
        Double[] val = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        for (int i = 0; i < 5; i++) {
            kd.put(points[i], val[i]);
        }
        Point2D p = new Point2D(0.5, 0.4);

        // query rectangle
        RectHV queryRect = new RectHV(0.2, 0.2, 0.5, 0.5);

        // Print statements
        StdOut.println(kd.size()); // should print 5
        StdOut.println(kd.isEmpty()); // should print false
        StdOut.println(kd.get(p)); // should print 2.0

        Point2D p6 = new Point2D(0.4, 0.6);
        StdOut.println(kd.nearest(p6));

        // Print the points found within the query rectangle
        for (Point2D point : kd.range(queryRect)) {
            System.out.println(point);
        } // Should print (0.4, 0.7), (0.5, 0.4), (0.2, 0.3)
        Point2D p7 = new Point2D(0.4, 0.7);
        StdOut.println(kd.contains(p7)); // should return true
        StdOut.println(kd.points());
    }

        /* String filename = args[0];
        In input = new In(filename);
        KdTreeST<Integer> kd = new KdTreeST<Integer>();
        while (!input.isEmpty()) {
            double x = input.readDouble();
            double y = input.readDouble();
            Point2D p = new Point2D(x, y);
            kd.put(p, 0);
        }
        double x = StdRandom.uniformDouble();
        double val = StdRandom.uniformDouble();
        Point2D pTest = new Point2D(x, val);
        Stopwatch stopwatch = new Stopwatch();
        System.out.println(kd.nearest(pTest));
        double time = stopwatch.elapsedTime();
        StdOut.println(time);
        StdOut.printf("%.10f", time);
        StdOut.println();

         */

}


