import java.lang.Math;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] harga = new int[] {2,8,12,19,30,39};
        System.out.println(Arrays.toString(harga));
        System.out.println(binarySearch(23, harga));
    }

    static int binarySearch(int hargaDicari, int[] harga) {
        int l = 0;
        int r = harga.length - 1;

        if (harga[l] >= hargaDicari) return harga[l] - hargaDicari;
        if (harga[r] <= hargaDicari) return hargaDicari - harga[r];

        while (l < r-1) {

            int mid = (l + r) / 2;

            System.out.print("l: ");
            System.out.print(l);
            System.out.print("   ");
            System.out.print("mid: ");
            System.out.print(mid);
            System.out.print("    ");
            System.out.print("r: ");
            System.out.println(r);

            int diffMid = hargaDicari - harga[mid];
            
            System.out.print("diffMid: ");
            System.out.println(diffMid);

            if (diffMid > 0) l = mid;
            else if (diffMid < 0) r = mid;
            else return 0;

        }

        System.out.print("l: ");
        System.out.println(l);
        System.out.print("r: ");
        System.out.println(r);

        return Math.min(Math.abs(hargaDicari - harga[l]), Math.abs(hargaDicari - harga[r]));

    }
}