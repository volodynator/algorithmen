import java.util.HashMap;
import java.util.Map;

public class AdjacencyMatrixGraph<T> implements Graph<T> {
    public static final int INITIAL_CAPACITY = 10;
    private boolean[][] matrix;
    private Map<T, Integer> map;
    private int count;

    public AdjacencyMatrixGraph() {
        matrix = new boolean[INITIAL_CAPACITY][INITIAL_CAPACITY];
        map = new HashMap<>();
        count = 0;
    }

    @Override
    public boolean addNodeElement(T element) {
        if (map.containsKey(element)){
            return false;
        }
        if (count==matrix.length){
            int newSize = matrix.length * 2;
            boolean[][] newMatrix = new boolean[newSize][newSize];
            for (int i = 0; i < matrix.length; i++) {
                System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
            }
            matrix = newMatrix;
        }
        map.put(element, count);
        count++;
        return true;
    }

    @Override
    public boolean removeNodeElement(T element) {
        if (map.containsKey(element)){
            // Search for index
            int elementIndex = map.get(element);
            // Delete all edges with this node
            for (int i = 0; i<count; i++){
                matrix[elementIndex][i] = false;
                matrix[i][elementIndex] = false;
            }
            map.remove(element);
            count--;
            return true;
        }
        return false;
    }

    @Override
    public void addEdge(T from, T to) throws InvalidEdgeException{
        if (!map.containsKey(from) || !map.containsKey(to)){
            throw new InvalidEdgeException();
        }
        else{
            int fromIndex = map.get(from);
            int toIndex = map.get(to);
            matrix[fromIndex][toIndex] = true;
        }
    }

    @Override
    public boolean removeEdge(T from, T to) throws InvalidNodeException {
        if (map.containsKey(from) && map.containsKey(to)){
            int fromIndex = map.get(from);
            int toIndex = map.get(to);
            matrix[fromIndex][toIndex] = false;
            matrix[toIndex][fromIndex] = false;
            return true;
        }
        else {
            throw new InvalidNodeException();
        }
    }
    public void printGraph() {
        System.out.print("  ");
        for (T key : map.keySet()) {
            System.out.print(key + " ");
        }
        System.out.println();
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            System.out.print(entry.getKey() + " ");
            int rowIndex = entry.getValue();
            for (int j = 0; j < count; j++) {
                System.out.print((matrix[rowIndex][j] ? 1 : 0) + " ");
            }
            System.out.println();
        }
    }

    // convert method O(n^3):
    AdjacencyListGraph<T> convert() throws InvalidEdgeException {
        AdjacencyListGraph<T> newGraph = new AdjacencyListGraph<>();
        // adding nodes O(n):
        for (T element : map.keySet()){
            newGraph.addNodeElement(element);
        }
        // adding edges O(n^3):
        for (Map.Entry<T, Integer> entry:map.entrySet()){
            int position = entry.getValue();
            for (int i = 0; i<count; i++){
                // if edge exists:
                if (matrix[position][i]){
                    // searching for key by value:
                    for (Map.Entry<T, Integer> edgeEntry: map.entrySet()){
                        // finding key:
                        if (edgeEntry.getValue().equals(i)){
                            // adding edge:
                            newGraph.addEdge(entry.getKey(), edgeEntry.getKey());
                        }
                    }
                }
            }
        }
        return newGraph;
    }

    public static void main(String[] args) throws InvalidNodeException, InvalidEdgeException {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        System.out.println("addNodeElement(\"A\")\n" +
                "addNodeElement(\"B\")\n" +
                "addNodeElement(\"C\")\n" +
                "addEdge(\"A\", \"B\"):");
        graph.addNodeElement("A");
        graph.addNodeElement("B");
        graph.addNodeElement("C");
        graph.addEdge("A", "B");
        graph.printGraph();
        System.out.println("removeNodeElement(\"C\"):");
        graph.removeNodeElement("C");
        graph.printGraph();
        System.out.println("addNodeElement(\"D\")\n" +
                "addEdge(\"D\", \"B\")\n" +
                "removeEdge(\"A\", \"B\"):");
        graph.addNodeElement("D");
        graph.addEdge("D", "B");
        graph.removeEdge("A", "B");
        graph.printGraph();
        System.out.println("graph.convert():");
        AdjacencyListGraph<String> newGraph = graph.convert();
        newGraph.print();
    }
}
