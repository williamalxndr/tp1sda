import java.lang.Math;

public class Test {
    public static void main(String[] args) {
        int[] harga = new int[] {1,3,4,7,10,15,31};
        System.out.println(binarySearch(4, harga));
    }

    static int binarySearch(int hargaDicari, int[] harga) {
        int l = 0;
        int r = harga.length - 1;

        if (harga[l] > hargaDicari) return harga[l] - hargaDicari;
        if (harga[r] < hargaDicari) return hargaDicari - harga[r];

        while (l < r-1) {

            int mid = (l + r) / 2;

            System.out.print("l: ");
            System.out.print(l);
            System.out.print("  ");
            System.out.print("mid: ");
            System.out.print(mid);
            System.out.print("  ");
            System.out.print("r: ");
            System.out.println(r);

            int diffLeft = hargaDicari - harga[l];
            int diffMid = hargaDicari - harga[mid];
            int diffRight = hargaDicari - harga[r];
            
            System.out.print("diffLeft: ");
            System.out.print(diffLeft);
            System.out.print("  ");
            System.out.print("diffMid: ");
            System.out.print(diffMid);
            System.out.print("  ");
            System.out.print("diffRight: ");
            System.out.println(diffRight);



            if ((diffLeft * diffMid) < 0) r = mid;
            else if ((diffRight * diffMid) < 0) l = mid;
            else if (diffLeft == 0 || diffMid == 0 || diffRight == 0) return 0;

        }

        System.out.print("l: ");
        System.out.println(l);
        System.out.print("r: ");
        System.out.println(r);

        return Math.min(Math.abs(hargaDicari - harga[l]), Math.abs(hargaDicari - harga[r]));

    }
}