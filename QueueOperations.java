import java.util.LinkedList;
import java.util.Queue;

public class QueueOperations{
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();

        queue.add(5);
        queue.add(2);
        queue.poll(); // 5 wird entfernt
        queue.add(13);
        queue.add(queue.poll() * queue.poll()); // 2 * 13 = 26
        queue.add(13);
        queue.add(3);
        queue.add(queue.poll() + queue.poll()); // 26 + 13 = 39
        queue.add(9);
        queue.add(queue.poll() / queue.poll()); // 3 / 39 = 0 (Integer-Division)
        System.out.println(queue.poll()); // 9

        // Ausgabe des Zustands der Queue nach den Operationen
        System.out.println("Queue nach der Ausgabe: " + queue);
    }
}