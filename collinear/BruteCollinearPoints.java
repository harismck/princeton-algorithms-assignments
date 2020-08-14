import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class BruteCollinearPoints {
    private final LinkedList<LineSegment> segmentsList;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }

        segmentsList = new LinkedList<>();
        int n = points.length;
        Point max, min;

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                for (int k = j + 1; k < n; k++)
                    for (int m = k + 1; m < n; m++) {
                        Point p = points[i], q = points[j], r = points[k], s = points[m];
                        if (p.equals(q) || p.equals(r) || r.equals(s)) { // Duplicate points illegal
                            throw new IllegalArgumentException();
                        }
                        if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(q) == p.slopeTo(s)) {
                            // Find the smallest and largest points
                            min = p;
                            if (min.compareTo(q) < 0)
                                min = q;
                            if (min.compareTo(r) < 0)
                                min = r;
                            if (min.compareTo(s) < 0)
                                min = s;
                            max = p;
                            if (max.compareTo(q) > 0)
                                max = q;
                            if (max.compareTo(r) > 0)
                                max = r;
                            if (max.compareTo(s) > 0)
                                max = s;

                            // Add a LineSegment
                            segmentsList.add(new LineSegment(min, max));
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
