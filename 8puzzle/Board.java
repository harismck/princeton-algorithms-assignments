import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private int[][] tiles;
    private final int dim;
    private final int size; // number of tiles

    public Board(int[][] tiles) {
        dim = tiles.length;
        size = dim * dim;
        this.tiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], dim);
        }
    }

    public String toString() {
        String s = String.format("%d\n", dim);
        String rowFormat = "%s %d";
        String colFormat = "%s \n";
        for (int[] row : tiles) {
            for (int tile : row)
                s = String.format(rowFormat, s, tile);
            s = String.format(colFormat, s);
        }
        return s;
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        // Number of out-of-place tiles
        int dist = 0;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (tiles[i][j] != 0 && tiles[i][j] != (i * dim + j + 1) % size)
                    dist++;
        return dist;
    }

    public int manhattan() {
        // Sum of distances of each tile to its correct position
        int dist = 0;
        int n, x, y;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                n = tiles[i][j];
                if (n == 0) continue;
                x = (n - 1) / dim;
                y = n - x * dim - 1;
                dist += Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return dist;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null || this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;
        if (that.dim != this.dim)
            return false;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    public Iterable<Board> neighbors() {
        // All board states reachable by moving a single tile
        // Find index of 0
        int count = 0, tileNum = 1, i = 0, j = 0;
        while (tileNum != 0) {
            i = count / dim;
            j = count % dim;
            tileNum = tiles[i][j];
            count++;
        }

        // Find neighbors
        Stack<Board> q = new Stack<>();
        Board tmp;
        if (i != 0) {
            tmp = swap(i, j, i - 1, j);
            q.push(tmp);
        }
        if (i != dim - 1) {
            tmp = swap(i, j, i + 1, j);
            q.push(tmp);
        }
        if (j != 0) {
            tmp = swap(i, j, i, j - 1);
            q.push(tmp);
        }
        if (j != dim - 1) {
            tmp = swap(i, j, i, j + 1);
            q.push(tmp);
        }

        return q;
    }

    private Board swap(int i, int j, int p, int q) {
        // A Board obtained by swapping tiles (i, j) and (p, q)
        Board that = new Board(this.tiles);
        that.tiles[i][j] = this.tiles[p][q];
        that.tiles[p][q] = this.tiles[i][j];
        return that;
    }

    public Board twin() {
        // A board obtained by swapping any two tiles
        if (tiles[0][0] == 0)
            return swap(0, 1, 1, 0);
        if (tiles[0][1] == 0)
            return swap(0, 0, 1, 0);
        return swap(0, 0, 0, 1);
    }

}
