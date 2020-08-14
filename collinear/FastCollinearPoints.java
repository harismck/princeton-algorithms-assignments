import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private final LinkedList<LineSegment> segmentsList;
    private final LinkedList<Point> segmentOrigins;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        // Create a copy to avoid mutating the array
        points = Arrays.copyOf(points, points.length);

        Point p, q1, q2;
        Point[] pointsToCompareWith;
        segmentOrigins = new LinkedList<>();
        segmentsList = new LinkedList<>();
        int first;
        int n = points.length;
        int k;

        if (n < 4) {
            return;
        }

        // Sort points
        // Arrays.sort() is stable.
        // Once sorted by slope, the points with equal slopes will remain sorted by coordinates.
        Arrays.sort(points);

        for (int i = 0; i < n - 3; i++) {
            p = points[i];
            if (p == null)
                throw new IllegalArgumentException();
            k = n - i - 1;

            pointsToCompareWith = new Point[k];
            for (int j = 0; j < k; j++) {
                if (p.equals(points[i + j + 1])) // Duplicate points are illegal
                    throw new IllegalArgumentException();
                pointsToCompareWith[j] = points[i + j + 1];
            }
            Arrays.sort(pointsToCompareWith, p.slopeOrder());

            first = 0; // Tracks the first point in a sequence of points with equal slopes
            for (int j = 1; j < k; j++) {
                q1 = pointsToCompareWith[j - 1];
                q2 = pointsToCompareWith[j];

                if (q1.equals(q2)) // Duplicate points are illegal
                    throw new IllegalArgumentException();

                if (p.slopeTo(q1) != p.slopeTo(q2)) {
                    if (j - first > 2) {
                        addSegment(p, q1);
                    }
                    first = j;
                }
            }

            if (k - first > 2)
                addSegment(p, pointsToCompareWith[k - 1]);

        }
    }

    private void addSegment(Point p1, Point p2) {
        for (Point origin : segmentOrigins) {
            if (origin.slopeTo(p1) == p1.slopeTo(p2))
                return;
        }
        segmentOrigins.push(p1);
        segmentsList.push(new LineSegment(p1, p2));
    }

    public int numberOfSegments() {
        return segmentsList.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[segmentsList.size()];
        int i = 0;
        for (LineSegment ls : segmentsList) {
            segments[i++] = ls;
        }
        return segments;
    }

    public static void main(String[] args) {
        // COPIED FROM THE ASSIGNMENT SPECIFICATION

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
