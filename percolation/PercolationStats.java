import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholds;
    private int t;

    public PercolationStats(int n, int trials) {
        thresholds = new double[trials];
        t = trials;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                if (p.isOpen(row, col))
                    continue;
                p.open(row, col);
            }

            thresholds[i] = (double) p.numberOfOpenSites() / (n * n);

        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(t);
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(t);
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        StdOut.println("mean \t\t\t = " + ps.mean());
        StdOut.println("stddev \t\t\t = " + ps.stddev());
        StdOut.printf("confidence interval \t\t\t = [%f, %f] %n",
                ps.confidenceHi(), ps.confidenceLo());

    }
}
