package ai;

import main.GamePanel;
import java.util.ArrayList;

public class PathFinder {

    private final GamePanel gp;
    private Node[][] nodes;
    private final ArrayList<Node> openList = new ArrayList<>();
    public final ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode, goalNode, currentNode;
    private boolean goalReached = false;
    private int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    private void instantiateNodes() {
        nodes = new Node[gp.maxWorldCol][gp.maxWorldRow];
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                nodes[col][row] = new Node(col, row);
            }
        }
    }

    private void resetNodes() {
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                Node node = nodes[col][row];
                node.open = false;
                node.checked = false;
                node.solid = false;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();
        startNode = nodes[startCol][startRow];
        goalNode = nodes[goalCol][goalRow];
        currentNode = startNode;
        openList.add(currentNode);

        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                if (gp.tileM.tile[gp.tileM.mapTileNum[col][row]].collision) {
                    nodes[col][row].solid = true;
                }
                calculateCost(nodes[col][row]);
            }
        }
    }

    private void calculateCost(Node node) {
        node.gCost = Math.abs(node.col - startNode.col) + Math.abs(node.row - startNode.row);
        node.hCost = Math.abs(node.col - goalNode.col) + Math.abs(node.row - goalNode.row);
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while (!goalReached && step < 500) {
            currentNode.checked = true;
            openList.remove(currentNode);

            int col = currentNode.col;
            int row = currentNode.row;

            if (row > 0) openNode(nodes[col][row - 1]);
            if (col > 0) openNode(nodes[col - 1][row]);
            if (row < gp.maxWorldRow - 1) openNode(nodes[col][row + 1]);
            if (col < gp.maxWorldCol - 1) openNode(nodes[col + 1][row]);

            if (openList.isEmpty()) break;

            Node bestNode = openList.get(0);
            for (Node n : openList) {
                if (n.fCost < bestNode.fCost || (n.fCost == bestNode.fCost && n.gCost < bestNode.gCost)) {
                    bestNode = n;
                }
            }
            currentNode = bestNode;

            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }
            step++;
        }
        return goalReached;
    }

    private void openNode(Node node) {
        if (!node.open && !node.solid && !node.checked) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackPath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
