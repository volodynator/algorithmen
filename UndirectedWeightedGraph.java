/**
 * Interface of a UndirectedWeightedGraph
 *
 * @param <T> the type to store in the nodes of the graph
 */
public interface UndirectedWeightedGraph<T> {

    /**
     * add a node to the graph with the given element
     *
     * @param element the element which should be represented in the graph as a node
     * @return true, if the element was added; false, if the element already existed.
     */
    boolean addNodeElement(T element);

    /**
     * remove the node with the given element from the graph
     *
     * @param element the element to remove
     * @return true, if the element was removed; false, if the element did not exist.
     */
    boolean removeNodeElement(T element);

    /**
     * add a new edge to the graph which connects the given elements in the graph and associates the given weight to that connection
     *
     * @param t1     one element connected by the edge
     * @param t2     the other element connected by the edge
     * @param weight the weight of the new edge
     * @throws InvalidEdgeException if one of the two elements is not part of the graph
     */
    void addEdge(T t1, T t2, int weight) throws InvalidEdgeException;

    /**
     * removes edge with the given two nodes from the graph
     *
     * @param t1 one element connected by the edge
     * @param t2 the other element connected by the edge
     * @return true, if the edge was removed; false, if the edge did not exist.
     * @throws InvalidNodeException if one of the two elements is not part of the graph
     */
    boolean removeEdge(T t1, T t2) throws InvalidNodeException;

    final class InvalidEdgeException extends Exception {

        public InvalidEdgeException() {
        }

        public InvalidEdgeException(String message) {
            super(message);
        }
    }

    final class InvalidNodeException extends Exception {

        public InvalidNodeException() {
        }

        public InvalidNodeException(String message) {
            super(message);
        }
    }
}
