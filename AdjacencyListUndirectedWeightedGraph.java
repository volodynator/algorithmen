import java.util.*;
import java.util.function.Predicate;

public class AdjacencyListUndirectedWeightedGraph<T> implements UndirectedWeightedGraph<T> {

    protected Map<T, List<Edge<T>>> map;

    public AdjacencyListUndirectedWeightedGraph() {
        map = new HashMap<>();
    }

    @Override
    public boolean addNodeElement(T element) {
        if (!map.containsKey(element)){
            map.put(element, new LinkedList<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeNodeElement(T element) {
        if (map.containsKey(element)){
            map.remove(element);
            return true;
        }
        return false;
    }

    @Override
    public void addEdge(T t1, T t2, int weight) throws InvalidEdgeException {
        if (map.containsKey(t1) && map.containsKey(t2)){
            Edge<T> edge = new Edge<>(t1, t2, weight);
            map.get(t1).add(edge);
            map.get(t2).add(edge);
        }
        else {
            throw new InvalidEdgeException();
        }
    }

    @Override
    public boolean removeEdge(T t1, T t2) throws InvalidNodeException {
        List<Edge<T>> list = map.get(t1);
        for (Edge<T> edge:list) {
            if (edge.getT2().equals(t2)){
                list.remove(edge);
                map.get(t2).remove(edge);
                return true;
            }
            else {
                return false;
            }
        }
        throw new InvalidNodeException();
    }

    Comparator<Edge<T>> comparator = new Comparator<Edge<T>>() {
        @Override
        public int compare(Edge<T> o1, Edge<T> o2) {
            return Integer.compare(o1.getWeight(), o2.getWeight());
        }
    };

    public void kruskal() throws InvalidEdgeException {
        AdjacencyListUndirectedWeightedGraph<T> newGraph = new AdjacencyListUndirectedWeightedGraph<>();
        PriorityQueue<Edge<T>> queue = new PriorityQueue<>(comparator);
        // node -> component
        Map<T, Set<T>> components = new HashMap<>();

        for (T node: map.keySet()) {
            // adding nodes to components
            Set<T> set = new HashSet<>();
            set.add(node);
            components.put(node, set);

            // adding all edges to queue
            List<Edge<T>> list = map.get(node);
            for (Edge<T> edge: list) {
                if (!queue.contains(edge)){
                    queue.add(edge);
                }
            }
        }

        // check all edges
        while (!queue.isEmpty()){
            Edge<T> edge = queue.poll();
            T t1 = edge.getT1();
            T t2 = edge.getT2();
            Set<T> set1 = components.get(t1);
            Set<T> set2 = components.get(t2);
            if (!set1.equals(set2)){
                newGraph.addNodeElement(t1);
                newGraph.addNodeElement(t2);
                newGraph.addEdge(t1, t2, edge.getWeight());
                System.out.println("Edge " + t1 + " <-> " + t2 + " accepted");

                set1.addAll(set2);
                for (T node : set2) {
                    components.put(node, set1);

                }
                System.out.println(components.values());
            }

        }
    }


    public static void main(String[] args) throws InvalidEdgeException, InvalidNodeException {
        AdjacencyListUndirectedWeightedGraph<Character> graph = new AdjacencyListUndirectedWeightedGraph<>();
        graph.addNodeElement('A');
        graph.addNodeElement('B');
        graph.addNodeElement('C');
        graph.addNodeElement('D');
        graph.addNodeElement('E');
        graph.addNodeElement('F');
        graph.addEdge('A', 'B', 30);
        graph.addEdge('A', 'C', 40);
        graph.addEdge('A', 'E', 100);
        graph.addEdge('A', 'F', 90);
        graph.addEdge('B', 'C', 10);
        graph.addEdge('B', 'D', 40);
        graph.addEdge('C', 'F', 10);
        graph.addEdge('D', 'E', 30);
        graph.addEdge('E', 'F', 20);
        graph.kruskal();
    }
}
