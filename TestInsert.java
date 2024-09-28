import java.util.ArrayList;
import java.util.Arrays;

public class TestInsert {

    public static void main(String[] args) {
        ArrayList<Integer> sortedArray = new ArrayList<>(Arrays.asList(1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39,41));

        insertSort(11, sortedArray);
        System.out.println(sortedArray);
    }

    static void insertSort(int num, ArrayList<Integer> arr) {
        // Using binary search for inserting num to sortedArray in sorted order.

        int l = 0;
        int r = arr.size() - 1;

        while (l < r-1) {
            System.out.printf("l: %d", l);
            System.out.printf("r: %d", r);
            System.out.println();

            int mid = (l + r) / 2;

            if (arr.get(mid) > num) r = mid;
            else if (arr.get(mid) < num) l = mid;
            else {
                arr.add(mid, num);
                return;
            }
        }
        arr.add(r, num);
    }   
}
