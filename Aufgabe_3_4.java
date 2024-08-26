import java.util.Arrays;

public class Aufgabe_3_4 {
    public static void main(String[] args) {
        int[] arr = { 1, 10, 12, 24, 36, 100, 9, 2, -5 };
        System.out.println(checkTwoSumOpt(arr, 136));
    }
    static boolean checkTwoSumOpt(int[] arr, int sum) {

        Arrays.sort(arr);

        for (int i = 0; i < arr.length; i++) {
            int lookingFor = sum - arr[i];
            if (lookingFor == arr[i]) break;
            if(binSearch(arr, lookingFor, 0, arr.length - 1 )) return true;
        } return false;
    }

    static boolean binSearch(int[] arr, int n, int low, int high){
        if((low > high)) return false;
        int mid = (low + high) / 2;
        if(arr[mid] == n) return true;
        if(arr[mid] > n) return binSearch(arr, n, low, mid - 1);
        else return binSearch(arr, n, mid + 1, high);
    }


}
