import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private final SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!points.contains(p))
            points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> pointsInRange = new Queue<>();

        for (Point2D q : points) {
            if (rect.contains(q))
                pointsInRange.enqueue(q);
        }
        return pointsInRange;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;

        Point2D min = null;
        double dist;
        double minDist = Double.POSITIVE_INFINITY;
        for (Point2D q : points) {
            dist = q.distanceSquaredTo(p);
            if (dist < minDist) {
                minDist = dist;
                min = q;
            }
        }
        return min;
    }

}
