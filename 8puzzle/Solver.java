import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Node solutionNode;
    private boolean solvable;

    public Solver(Board initial) {
        // Solves the board (if solvable)

        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinPq = new MinPQ<>();
        Node node = new Node(initial, null, 0);
        Node twinNode = new Node(initial.twin(), null, 0);
        pq.insert(node);
        twinPq.insert(twinNode);

        while (true) {
            node = pq.delMin();
            twinNode = twinPq.delMin();
            if (node.board.isGoal()) {
                solutionNode = node;
                solvable = true;
                break;
            }
            if (twinNode.board.isGoal()) {
                solvable = false;
                break;
            }
            for (Board neighbor : node.board.neighbors()) {
                if (node.prevNode == null || !neighbor.equals(node.prevNode.board))
                    pq.insert(new Node(neighbor, node, node.moves + 1));
            }
            for (Board neighbor : twinNode.board.neighbors()) {
                if (twinNode.prevNode == null || !neighbor.equals(twinNode.prevNode.board))
                    twinPq.insert(new Node(neighbor, twinNode, twinNode.moves + 1));
            }
        }
    }

    private class Node implements Comparable<Node> {
        int moves, priority;
        Board board;
        Node prevNode;

        public Node(Board current, Node prev, int moves) {
            board = current;
            prevNode = prev;
            this.moves = moves;
            priority = board.manhattan() + moves;
        }

        public int compareTo(Node that) {
            if (that == null)
                throw new NullPointerException();

            if (this.priority > that.priority)
                return 1;
            else if (this.priority < that.priority)
                return -1;
            return 0;
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable())
            return -1;
        return solutionNode.moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> s = new Stack<>();
        Node node = solutionNode;
        while (node != null) {
            s.push(node.board);
            node = node.prevNode;
        }
        return s;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
