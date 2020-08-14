import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class KdTree {

    private Node root;
    private int size;

    private static class Node { // 2d-tree node
        private final Point2D point;
        private Node left, right;

        private Node(Point2D point) {
            this.point = point;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, p, true);
    }

    private Node insert(Node n, Point2D p, boolean evenLevel) {
        if (n == null) {
            size++;
            return new Node(p);
        }

        Point2D q = n.point;
        if (q.equals(p)) {
            return n;
        }

        double pCoord, qCoord;
        if (evenLevel) { // Compare by x coordinate if level is even
            pCoord = p.x();
            qCoord = q.x();
        } else {
            pCoord = p.y();
            qCoord = q.y();
        }

        if (pCoord < qCoord) n.left = insert(n.left, p, !evenLevel);
        else n.right = insert(n.right, p, !evenLevel);
        return n;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p, true);
    }

    private boolean contains(Node n, Point2D p, boolean evenLevel) {
        if (n == null) return false;
        Point2D q = n.point;
        if (q.equals(p)) return true;

        double pCoord, qCoord;
        if (evenLevel) { // Compare by x coordinate if level is even
            pCoord = p.x();
            qCoord = q.x();
        } else {
            pCoord = p.y();
            qCoord = q.y();
        }

        if (pCoord < qCoord) return contains(n.left, p, !evenLevel);
        else return contains(n.right, p, !evenLevel);
    }

    public void draw() {
        draw(root, 0.0, 1.0, 0.0, 1.0, true);
    }

    private void draw(Node n, double startV, double endV, double startH, double endH, boolean evenLevel) {
        if (n == null) return;
        Point2D p = n.point;

        // Draw line
        if (evenLevel) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(p.x(), startV, p.x(), endV);
            draw(n.left, startV, endV, startH, p.x(), !evenLevel);
            draw(n.right, startV, endV, p.x(), endH, !evenLevel);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(startH, p.y(), endH, p.y());
            draw(n.left, startV, p.y(), startH, endH, !evenLevel);
            draw(n.right, p.y(), endV, startH, endH, !evenLevel);
        }

        // Draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(p.x(), p.y());

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<>();
        range(root, rect, queue, true);

        return queue;
    }

    private void range(Node x, RectHV rect, Queue<Point2D> queue, boolean evenLevel) {
        if (x == null) return;

        Point2D p = x.point;
        if (rect.contains(p)) {
            queue.enqueue(p);
        }

        // Select coordinates by which to check which nodes should be explored further
        double rectMinCoord, rectMaxCoord, pCoord;
        if (evenLevel) {
            rectMinCoord = rect.xmin();
            rectMaxCoord = rect.xmax();
            pCoord = p.x();
        } else {
            rectMinCoord = rect.ymin();
            rectMaxCoord = rect.ymax();
            pCoord = p.y();
        }

        if (rectMinCoord < pCoord) { // Go left if at least some part of the rectangle is on the left/below
            range(x.left, rect, queue, !evenLevel);
        }
        if (rectMaxCoord >= pCoord) { // Same logic for the right
            range(x.right, rect, queue, !evenLevel);
        }

    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return null;
        return nearest(root, p, root.point, true);
    }

    private Point2D nearest(Node x, Point2D p, Point2D champ, boolean evenLevel) {
        if (x == null) return champ;

        Point2D q = x.point;
        double qDist = p.distanceSquaredTo(q);
        double champDist = p.distanceSquaredTo(champ);
        if (qDist < champDist)
            champ = q;

        // Decide which coordinates to compare by
        double pCoord, qCoord;
        Point2D closest; // Point closest to p on the line of q
        if (evenLevel) {
            pCoord = p.x();
            qCoord = q.x();
            closest = new Point2D(q.x(), p.y());
        } else {
            pCoord = p.y();
            qCoord = q.y();
            closest = new Point2D(p.x(), q.y());
        }

        // Decide which nodes to explore further
        if (pCoord < qCoord) {
            champ = nearest(x.left, p, champ, !evenLevel);
            if (p.distanceSquaredTo(champ) > p.distanceSquaredTo(closest)) {
                champ = nearest(x.right, p, champ, !evenLevel);
            }
        } else {
            champ = nearest(x.right, p, champ, !evenLevel);
            if (p.distanceSquaredTo(champ) > p.distanceSquaredTo(closest)) {
                champ = nearest(x.left, p, champ, !evenLevel);
            }
        }
        return champ;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree tree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }

        StdOut.println(tree.size());
        tree.draw();

    }
}
