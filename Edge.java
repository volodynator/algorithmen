public class Edge<T> {
    private final T t1;
    private final T t2;
    private final int weight;

    public Edge(T t1, T t2, int weight) {
        this.t1 = t1;
        this.t2 = t2;
        this.weight = weight;
    }

    public T getT1() {
        return t1;
    }

    public T getT2() {
        return t2;
    }

    public T getOtherNode(T node) {
        if (node.equals(t1)) {
            return t2;
        }
        return t1;
    }

    public int getWeight() {
        return weight;
    }
}
