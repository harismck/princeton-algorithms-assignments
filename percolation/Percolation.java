import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private int openSitesCount = 0;
    private int topNode;
    private int bottomNode;
    private boolean[][] sites; // Tracks which sites are open
    private WeightedQuickUnionUF grid;

    public Percolation(int n) {
        // Creates an nxn grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        grid = new WeightedQuickUnionUF(n * n + 2); // Plus two virtual nodes

        // Initialize an array for keeping a record of open sites
        sites = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                sites[i][j] = false;

        // Initialize two virtual nodes
        topNode = n * n;
        bottomNode = n * n + 1;
        for (int i = 0; i < n; i++) {
            grid.union(topNode, i);
            grid.union(bottomNode, n * (n - 1) + i);
        }
    }

    public void open(int row, int col) {
        // Opens the site by connecting it to adjacent sites
        if (row > size || row < 1 || col > size || col < 1)
            throw new IllegalArgumentException();

        // row and col should be zero-indexed
        row--;
        col--;
        if (sites[row][col])
            return;
        sites[row][col] = true;

        // Connect to adjacent open sites
        int nodeID = row * size + col;
        if (row != 0 && sites[row - 1][col]) // Upper site
            grid.union(nodeID, (row - 1) * size + col);
        if (row != size - 1 && sites[row + 1][col]) // Lower site
            grid.union(nodeID, (row + 1) * size + col);
        if (col != 0 && sites[row][col - 1]) // Left site
            grid.union(nodeID, row * size + (col - 1));
        if (col != size - 1 && sites[row][col + 1]) // Right site
            grid.union(nodeID, row * size + (col + 1));

        openSitesCount++;
    }

    public boolean isOpen(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1)
            throw new IllegalArgumentException();
        return sites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1)
            throw new IllegalArgumentException();
        row--;
        col--;
        return grid.find(topNode) == grid.find(row * size + col) && sites[row][col];
    }

    public int numberOfOpenSites() {
        return openSitesCount;
    }

    public boolean percolates() {
        if (openSitesCount == 0)
            return false;
        return grid.find(topNode) == grid.find(bottomNode);
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation p = new Percolation(n);

        Stopwatch timer = new Stopwatch();
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();

            if (p.isOpen(row, col))
                continue;
            p.open(row, col);
        }

        double elapsed = timer.elapsedTime();
        StdOut.println(elapsed + " elapsed," + p.percolates() + " percolates");
    }
}
