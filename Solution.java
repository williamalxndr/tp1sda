import java.util.PriorityQueue;
import java.util.Scanner;

public class Solution {

    static PriorityQueue<int[]> queue = new PriorityQueue<>((a,b) -> {  // Queue pelanggan {id, budget, kesabaran}
        if (a[1]!=b[1]) return Integer.compare(b[1], a[1]);  // Compare budget (descending)
        else if (a[2]!= b[2]) return Integer.compare(a[2], b[2]);  // Compare kesabaran (ascending)
        else return Integer.compare(a[0], b[0]);  // Compare id (ascending)
    });

    static int id = 0;  // Iterator id pelanggan

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int N = in.nextInt();  // Banyaknya ikan
        int M = in.nextInt();  // Banyaknya suvenir
        int Q = in.nextInt();  // Banyaknya aktivitas

        int[] P = new int[N];  // Harga ikan
        int[] H = new int[M];  // Harga suvenir
        int[] V = new int[M];  // Nilai kebahagiaan suvenir 

        for (int i=0; i<N; i++) {
            P[i] = in.nextInt();
        }

        for (int i=0; i<M; i++) {
            H[i] = in.nextInt();
        }

        for (int i=0; i<M; i++) {
            V[i] = in.nextInt();
        }

        for (int i=0; i<Q; i++) {

            decrementKesabaran();  // Kurangi satu poin kesabaran untuk semua pelanggan yang ada di queue 

            String query = in.next();

            switch (query) {
                case "A":  // Query A = masukkan pelanggan baru ke queue
                    int budget = in.nextInt();
                    int kesabaran = in.nextInt();
                    System.out.println(A(budget, kesabaran));
                    break;

                case "S":  // Query S = selisih minimum antara <hargaDicari> dengan semua harga ikan di toko
                    int hargaDicari = in.nextInt();
                    System.out.println(S(hargaDicari));
                    break;

                case "L":  // Query L = keluarkan pelanggan dengan id <idPelanggan> 
                    int idPelanggan = in.nextInt();
                    System.out.println(L(idPelanggan));
                    break;

                case "D":  // Tambahkan kupon diskon senilai <diskon> di atas tumpukan kupon diskon
                    int diskon = in.nextInt();
                    System.out.println(D(diskon));
                    break;

                case "B":  // Layani pelanggan di antrian paling depan
                    System.out.println(B());
                    break;

                case "O":  // 
                    int tipeQuery = in.nextInt();
                    int x = in.nextInt();
                    System.out.println(O(tipeQuery, x));
                    break;
                
                case "DEBUG":
                    printQueue();
                    break;
            }
        }

        in.close();
        
    }

    // Method2 query
    static int A(int budget, int kesabaran) {
        int[] newPelanggan = new int[]{id, budget, kesabaran};
        queue.offer(newPelanggan);
        id++;
        return id-1;
    }

    static int S(int hargaDicari) {
        return 0;
    }

    static int L(int idPelanggan) {
        return 0;
    }

    static int D(int diskon) {
        return 0;
    }

    static int B() {
        return 0;
    }

    static int O(int tipeQuery, int x) {
        return 0;
    }


    // Method2 pembantu
    static int[] sortedArr(int[] unsortedArr) {
        return new int[0];
    }

    static void decrementKesabaran() {
        PriorityQueue<int[]> newQueue = new PriorityQueue<>(queue.comparator());
        while (!queue.isEmpty()) {
            int[] pelanggan = queue.poll();
            pelanggan[2]--; 
            if (pelanggan[2] > 0) {
                newQueue.offer(pelanggan); 
            }
        }
        queue = newQueue;
    }

    static void printQueue() {  // Debug
        while (!queue.isEmpty()) {
            int[] pelanggan = queue.poll();
            for (int i=0; i<3; i++) {
                System.out.print(pelanggan[i]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}
 