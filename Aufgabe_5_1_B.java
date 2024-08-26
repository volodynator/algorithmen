import java.util.HashSet;

public class Aufgabe_5_1_A {
    public static void main(String[] args) {

        int[] arr = { 1, 10, 12, 24, 36, 100, 9, 2, -5 };
        System.out.println(twoSum(arr, 25));
    }
    public static boolean twoSum(int[] nums, int target) {
        HashSet<Integer> hs = new HashSet<>();
        int lookFor;
        for(int num : nums){
            lookFor = target - num;
            if(hs.contains(lookFor)) return true;
            hs.add(num);
        }
        return false;
    }
}