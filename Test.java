public class Test {
    public static void main(String[] args) {
        int[] harga = new int[] {1,3,4,7,10,15,31};
        System.out.println(binarySearch(2, harga));
    }

    static int binarySearch(int budget, int[] harga) {
        int l = 0;
        int r = harga.length - 1;

        if (budget < harga[l]) return -1;
        if (budget > harga[r]) return budget - harga[r];

        while (l < r-1) {

            int mid = (l + r) / 2;

            if (budget == harga[mid]) return 0;
            else if (budget > harga[mid]) l = mid;
            else r = mid;

        }

        return budget - harga[l];
    }
}