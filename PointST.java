import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PointST<Value> {
    // TreeMap that tracks all points and values
    private TreeMap<Point2D, Value> symbolTable;

    // construct an empty symbol table of points
    public PointST() {
        symbolTable = new TreeMap<>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return symbolTable.isEmpty();
    }

    // number of points
    public int size() {
        return symbolTable.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new IllegalArgumentException();
        symbolTable.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return symbolTable.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return (symbolTable.containsKey(p));
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return symbolTable.keySet();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException(
                "Argument to range() is null");
        // list that adds all points in the symbol table contained in the rectangle
        List<Point2D> inRectangle = new ArrayList<>();
        for (Point2D p : symbolTable.keySet()) {
            if (rect.contains(p)) inRectangle.add(p);
        }
        return inRectangle;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException(
                "Argument to nearest() is null");
        double distance = Double.POSITIVE_INFINITY; // tracks shortest distance
        Point2D nearestPoint = null; // tracks which point has shortest distance
        for (Point2D point : symbolTable.keySet()) {
            double pointDistance = p.distanceSquaredTo(point);
            if (pointDistance < distance) {
                distance = pointDistance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Double> st = new PointST<>();
        Point2D p1 = new Point2D(0.5, 0.6);
        Point2D p2 = new Point2D(0.3, 0.3);
        Point2D p3 = new Point2D(0.1, 0.1);
        Point2D p4 = new Point2D(0.4, 0.4);
        st.put(p1, 1.0);
        st.put(p2, 2.0);
        st.put(p3, 3.0);
        st.put(p4, 2.5);
        RectHV queryRect = new RectHV(0.2, 0.5, 0.2, 0.5);
        StdOut.println("Symbol table is empty: "
                               + st.isEmpty()); // should return false
        StdOut.println("Number of points in the symbol table: "
                               + st.size()); // should be 4
        StdOut.println("Symbol table contains point p1: "
                               + st.contains(p1)); // should return true
        StdOut.println("Value associated with point p2: "
                               + st.get(p2)); // should return 2.0


        // Call points() method
        StdOut.println("All points in the symbol table:");
        for (Point2D point : st.points()) {
            StdOut.println(point);
        }

        // Call nearest() method
        Point2D queryPoint = new Point2D(0.2, 0.2);
        Point2D nearestPoint = st.nearest(queryPoint);
        StdOut.println("Nearest point to " + queryPoint + ": " + nearestPoint);

        // Call range
        for (Point2D point : st.range(queryRect)) {
            System.out.println(point);
        }
    }
}
