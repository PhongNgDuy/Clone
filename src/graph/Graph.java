package graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

    LinkedList<Node<T>> nodeList = new LinkedList<>();
    // getters and setters


    public LinkedList<Node<T>> getNodeList() {
        return nodeList;
    }

    public void calculateShortestPathFromSource(Node<T> source) {
        source.setDistance(0);

        Set<Node<T>> settledNodes = new HashSet<>();
        Set<Node<T>> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            Node<T> currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node<T>, Integer> adjacencyPair: currentNode.getAdjacentNodes().entrySet()) {
                Node<T> adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private Node<T> getLowestDistanceNode(Set<Node<T>> unsettledNodes) {
        Node<T> lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node<T> node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node<T> evaluationNode,
                                                 Integer edgeWeigh, Node<T> sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node<T>> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public static void main(String[] args) {
        Node<String> nodeA = new Node<>("A");
        Node<String> nodeB = new Node<>("B");
        Node<String> nodeC = new Node<>("C");
        Node<String> nodeD = new Node<>("D");
        Node<String> nodeE = new Node<>("E");
        Node<String> nodeF = new Node<>("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        Graph<String> graph = new Graph<>();

        graph.calculateShortestPathFromSource(nodeB);
        for (Node<String> i : nodeC.getShortestPath()) {
            System.out.println(i.getData());
        }
        System.out.println(nodeC.getDistance());
    }
}