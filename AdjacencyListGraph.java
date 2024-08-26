import java.util.*;

public class AdjacencyListGraph<T> implements Graph<T> {

    class UndirectedGraph<T> extends AdjacencyListGraph<T> {
        @Override
        public void addEdge(T from, T to) throws InvalidEdgeException {
            // adding to directed part:
            super.addEdge(from, to);
            // adding to undirected part:
            if (!map.get(to).contains(from)) {
                map.get(to).add(from);
            }
        }

        @Override
        public boolean removeEdge(T from, T to) throws InvalidNodeException {
            // remove from directed part:
            boolean removedFrom = super.removeEdge(from, to);
            // remove from undirected part:
            boolean removedTo = map.get(to).remove(from);
            return removedFrom && removedTo;
        }
    }

    private enum NodeColor {
        UNCOLORED,
        RED,
        GREEN
    }

    private NodeColor complement(NodeColor color) {
        // this should never be called with NodeColor.UNCOLORED
        return color == NodeColor.RED ? NodeColor.GREEN : NodeColor.RED;
    }

    Map<T, List<T>> map;

    public AdjacencyListGraph() {
        map = new HashMap<>();
    }

    @Override
    public boolean addNodeElement(T element) {
        if (map.containsKey(element)) {
            return false;
        }
        map.put(element, new LinkedList<>());
        return true;
    }

    @Override
    public boolean removeNodeElement(T element) {
        if (map.remove(element) == null) {
            return false;
        }
        for (List<T> list : map.values()) {
            list.remove(element);
        }
        return true;
    }

    @Override
    public void addEdge(T from, T to) throws InvalidEdgeException {
        if (!map.containsKey(from) || !map.containsKey(to)) {
            throw new InvalidEdgeException();
        }
        List<T> list = map.get(from);
        if (!list.contains(to)) {
            list.add(to);
        }
    }

    @Override
    public boolean removeEdge(T from, T to) throws InvalidNodeException {
        if (!map.containsKey(from) || !map.containsKey(to)) {
            throw new InvalidNodeException();
        }
        return map.get(from).remove(to);
    }

    // convert O(n^2)
    AdjacencyMatrixGraph<T> convert() throws InvalidEdgeException {
        AdjacencyMatrixGraph<T> adjacencyMatrixGraph = new AdjacencyMatrixGraph<>();
        // adding nodes O(n):
        for (T element: map.keySet()) {
            adjacencyMatrixGraph.addNodeElement(element);
        }
        // adding edges max O(n^2):
        for (Map.Entry<T, List<T>> entry: map.entrySet()) {
            for (T node: entry.getValue()) {
                adjacencyMatrixGraph.addEdge(entry.getKey(),node);
            }
        }
        return adjacencyMatrixGraph;
    }

    public AdjacencyListGraph<T> undirected() throws InvalidEdgeException {
        // creating new undirected Graph:
        UndirectedGraph<T> undirectedGraph = new UndirectedGraph<>();
        // adding nodes to undirected Graph:
        for (T element: map.keySet()) {
            undirectedGraph.addNodeElement(element);
        }
        // adding edges to undirected Graph:
        for (Map.Entry<T, List<T>> entry: map.entrySet()) {
            for (T node: entry.getValue()) {
                undirectedGraph.addEdge(entry.getKey(), node);
            }
        }
        return undirectedGraph;
    }


    public boolean undirectedGraphIsBipartit() throws InvalidEdgeException {
        // Undirected Graph:
        AdjacencyListGraph<T> undirectedGraph = this.undirected();
        // All nodes to UNCOLORED:
        Map<T, NodeColor> colorMap = new HashMap<>();
        for (T node : map.keySet()) {
            colorMap.put(node, NodeColor.UNCOLORED);
        }
        // BFS:
        for (T startNode : map.keySet()) {
            if (colorMap.get(startNode) == NodeColor.UNCOLORED) {
                // Start BFS from this uncolored node
                Queue<T> queue = new LinkedList<>();
                queue.add(startNode);
                colorMap.put(startNode, NodeColor.RED);

                while (!queue.isEmpty()) {
                    T currentNode = queue.poll();
                    NodeColor currentColor = colorMap.get(currentNode);
                    NodeColor neighborColor = complement(currentColor);

                    for (T neighbor : undirectedGraph.map.get(currentNode)) {
                        if (colorMap.get(neighbor) == NodeColor.UNCOLORED) {
                            colorMap.put(neighbor, neighborColor);
                            queue.add(neighbor);
                        } else if (colorMap.get(neighbor) == currentColor) {
                            // If neighbor has the same color, graph is not bipartite
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    void print() {
        for (T node : map.keySet()) {
            for (T other : map.get(node)) {
                System.out.println(node + " -> " + other);
            }
        }
    }

    public static void main(String[] args) throws InvalidEdgeException, InvalidNodeException {
        var graph = new AdjacencyListGraph<Character>();

        graph.addNodeElement('1');
        graph.addNodeElement('2');
        graph.addNodeElement('3');
        graph.addNodeElement('4');
        graph.addNodeElement('5');
        graph.addNodeElement('A');
        graph.addNodeElement('B');
        graph.addNodeElement('C');
        graph.addNodeElement('E');

        graph.addEdge('1', 'A');
        graph.addEdge('1', 'E');
        graph.addEdge('2', 'C');
        graph.addEdge('3', 'A');
        graph.addEdge('3', 'B');
        graph.addEdge('4', 'C');
        graph.addEdge('4', 'E');
        graph.addEdge('5', 'E');

        graph.print();

        System.out.println("graph.convert():");
        AdjacencyMatrixGraph<Character> newGraph = graph.convert();
        newGraph.printGraph();
        System.out.println("graph.undirected():");
        AdjacencyListGraph<Character> undirectedGraph = graph.undirected();
        undirectedGraph.print();
        System.out.println("removeEdge('1', 'A'):");
        undirectedGraph.removeEdge('1', 'A');
        undirectedGraph.print();
        System.out.println("graph.undirectedGraphIsBipartit():");
        System.out.println(graph.undirectedGraphIsBipartit());
    }
}
