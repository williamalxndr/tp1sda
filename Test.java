import java.util.Random;
import java.util.Arrays;

public class Test {

    static int iter = 0;

    public static void main(String[] args) {
        int[] randomArr = new int[1000];

        Random random = new Random();

        int budget = random.nextInt(4000);
        System.out.print("Budget: ");
        System.out.println(budget);

        randomArr[0] = random.nextInt(10);
        for (int i=1; i<1000; i++) {
            randomArr[i] = random.nextInt(randomArr[i-1], randomArr[i-1] + 10);
        }
        System.out.println(Arrays.toString(randomArr));

        long startTime = System.nanoTime();
        System.out.println(hargaMax(budget, randomArr));

        long endTime = System.nanoTime();

        System.out.printf("Iter: %d", iter);
        System.out.println();
        System.out.println((endTime-startTime));
    }

    static int hargaMax(int budget, int[] harga) {  
        // Menggunakan binary search untuk mencari harga paling mahal yang bisa dibeli dari budget yang dimiliki pelanggan (untuk method B).
        // Return harga ikan paling mahal yang bisa dibeli
        int l = 0;
        int r = harga.length - 1;

        if (budget < harga[l]) return -1;
        if (budget > harga[r]) return harga[r];

        while (l < r-1) {
            iter++;
            int mid = (l + r) / 2;

            if (budget == harga[mid]) return harga[mid];
            else if (budget > harga[mid]) l = mid;
            else r = mid;
        }
        return harga[l];
    }
    
}