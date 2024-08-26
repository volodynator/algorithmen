import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class SimpleSorting {
    /**
     * Ceck if a given list is sorted
     *
     * @param list list of generic objects
     * @param comp comparator
     * @param <E>  type
     * @return true if sorted
     */
    static <E> boolean isSorted(List<E> list, Comparator<E> comp) {
        E curr = list.getFirst();
        for (E next : list) {
            if (comp.compare(next, curr) < 0) return false;
            curr = next;
        }
        return true;
    }

    /**
     * Sorts list with selection sort
     *
     * @param list list of generic objects
     * @param comp comparator
     * @param <E>  type
     */
    static <E> void selectionSort(List<E> list, Comparator<E> comp) {
        selectionSortHelper(list, comp, 0);
    }

    private static <E> void selectionSortHelper(List<E> list, Comparator<E> comp, int startIndex) {
        int n = list.size();
        if (startIndex >= n - 1) {
            return;
        }
        int minIndex = startIndex;
        for (int i = startIndex + 1; i < n; i++) {
            if (comp.compare(list.get(i), list.get(minIndex)) < 0) {
                minIndex = i;
            }
        }
        if (minIndex != startIndex) {
            E temp = list.get(minIndex);
            list.set(minIndex, list.get(startIndex));
            list.set(startIndex, temp);
        }
        selectionSortHelper(list, comp, startIndex + 1);
    }

//    static <E> void bubbleSort(List<E> list, Comparator<E> comp) {
//        if(list.isEmpty() || list.size() == 1 || isSorted(list, comp)) return;
//        Iterator<E> iterator = list.iterator();
//        E current = iterator.next();
//        E dummy;
//        while(iterator.hasNext()) {
//            E next = iterator.next();
//            if(comp.compare(current, next) > 0) {
//                dummy = next;
//                next = current;
//                current = dummy;
//            }
//            current = iterator.next();
//        }
//    }

    /**
     * Sorts list with insertion sort
     *
     * @param list list of generic objects
     * @param comp comparator
     * @param <E>  type
     */
    public static <E> void insertionSort(List<E> list, Comparator<E> comp) {
        for (int i = 1; i < list.size(); i++) {
            insertionSortHelper(list, comp, i);
        }
    }

    private static <E> void insertionSortHelper(List<E> list, Comparator<E> comp, int index) {
        E temp, curr, prev;
        for (int i = index; i >= 1; i--) {
            curr = list.get(i);
            prev = list.get(i - 1);
            if (comp.compare(curr, prev) < 0) {
                temp = curr;
                list.set(i, prev);
                list.set(i - 1, temp);

            }
        }
    }


    public static void main(String[] args) throws Exception {

        int[] sizes = {100, 1000, 10000, 100000};
        Comparator<Integer> comp = Integer::compareTo;

        for (int n : sizes) {
            List<Integer> unsortedList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                unsortedList.add((int) (Math.random() * n));
            }

            List<Integer> sortedList = new ArrayList<>(unsortedList);
            insertionSort(sortedList, comp);  // Make sure we have a sorted list to start with

            // Test selection sort with unsorted data
            List<Integer> listToSort = new ArrayList<>(unsortedList);
            long startTime = System.currentTimeMillis();
            selectionSort(listToSort, comp);
            long endTime = System.currentTimeMillis();
            System.out.println("Selection sort with unsorted data, n = " + n + " : " + (endTime - startTime) + " ms");

            // Test selection sort with sorted data
            listToSort = new ArrayList<>(sortedList);
            startTime = System.currentTimeMillis();
            selectionSort(listToSort, comp);
            endTime = System.currentTimeMillis();
            System.out.println("Selection sort with sorted data, n = " + n + " : " + (endTime - startTime) + " ms");

            // Test insertion sort with unsorted data
            listToSort = new ArrayList<>(unsortedList);
            startTime = System.currentTimeMillis();
            insertionSort(listToSort, comp);
            endTime = System.currentTimeMillis();
            System.out.println("Insertion sort with unsorted data, n = " + n + " : " + (endTime - startTime) + " ms");

            // Test insertion sort with sorted data
            listToSort = new ArrayList<>(sortedList);
            startTime = System.currentTimeMillis();
            insertionSort(listToSort, comp);
            endTime = System.currentTimeMillis();
            System.out.println("Insertion sort with sorted data, n = " + n + " : " + (endTime - startTime) + " ms");

            // Check if the list is sorted
            System.out.println("List sorted (selection sort): " + isSorted(listToSort, comp));
            System.out.println("List sorted (insertion sort): " + isSorted(listToSort, comp));

        }
    }
}
