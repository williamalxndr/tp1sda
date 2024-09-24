import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.lang.Math;

public class Solution {

    static PriorityQueue<int[]> queue = new PriorityQueue<>((a,b) -> {  // Queue pelanggan {id, budget, kesabaran}
        if (a[1]!=b[1]) return Integer.compare(b[1], a[1]);  // Compare budget (descending)
        else if (a[2]!= b[2]) return Integer.compare(a[2], b[2]);  // Compare kesabaran (ascending)
        else return Integer.compare(a[0], b[0]);  // Compare id (ascending)
    });

    static int id = 0;  // Iterator id pelanggan

    static int t;

    static int[] P;  // Harga ikan
    static int[] H;  // Harga suvenir
    static int[] V;  // Nilai kebahagiaan suvenir

    static Stack<Integer> kupon = new Stack<>();  // Kupon diskon 

    static int[][] dataPelanggan;  // Untuk menyimpan data pelanggan awal, index i = pelanggan id i

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int N = in.nextInt();  // Banyaknya ikan
        int M = in.nextInt();  // Banyaknya suvenir
        int Q = in.nextInt();  // Banyaknya aktivitas

        P = new int[N];
        H = new int[M];
        V = new int[M];

        Arrays.sort(P);  // Sort harga ikan agar bisa dilakukan binary search utk method2 yang akan dipakai
        
        dataPelanggan = new int[Q][3];

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
            t = i;
            checkDepan();  // Cek kesabaran orang paling depan, jika sudah marah pop sampai ketemu yang masih sabar

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
                    printKupon();
                    break;
            }
        }

        in.close();
        
    }

    // Method2 query
    static int A(int budget, int kesabaran) {
        int[] newPelanggan = new int[]{id, budget, kesabaran + t};
        queue.offer(newPelanggan);
        dataPelanggan[id] = newPelanggan;
        id++;
        return id-1;
    }

    static int S(int hargaDicari) {
        return minDiff(hargaDicari, P);
    }

    static int L(int idPelanggan) {
        
        int[] pelanggan = dataPelanggan[idPelanggan];
        if (pelanggan[2] < (t+1) && queue.remove(pelanggan)) {
            return -1;
        }

        if (queue.remove(pelanggan)) {return pelanggan[1];}
        else return -1;

    } 

    static int D(int diskon) {
        kupon.push(diskon);
        return kupon.size();
    }

    static int B() {
        int kembalian = serve();
        return kembalian;
    }

    static int O(int tipeQuery, int x) {
        return 0;
    }

    // Method2 pembantu
    static void checkDepan() {

        if (queue.isEmpty()) return;

        while (queue.peek() == null || queue.peek()[2] < (t+1)) {
            queue.poll();
            if (queue.isEmpty()) return;
        }

    }

    // Debug 
    static void printQueue() {  // Debug queue
        PriorityQueue<int[]> newQueue = new PriorityQueue<>(queue.comparator());
        System.out.println(queue.size());
        while (!queue.isEmpty()) {
            int[] pelanggan = queue.poll();
            newQueue.offer(pelanggan);
            for (int i=0; i<3; i++) {
                System.out.print(pelanggan[i]);
                System.out.print(" ");
            }
            System.out.println();
        }
        queue = newQueue;
    }

    static void printKupon() {  // Print tumpukan kupon
        System.out.print("Kupon diskon: ");
        System.out.println(Arrays.toString(kupon.toArray()));
    }

    static int hargaMax(int budget, int[] harga) {  
        // Menggunakan binary search untuk mencari harga paling mahal yang bisa dibeli dari budget yang dimiliki pelanggan (untuk method B).
        // Return harga ikan paling mahal yang bisa dibeli
        int l = 0;
        int r = harga.length - 1;

        if (budget < harga[l]) return -1;
        if (budget > harga[r]) return harga[r];

        while (l < r-1) {

            int mid = (l + r) / 2;

            if (budget == harga[mid]) return harga[mid];
            else if (budget > harga[mid]) l = mid;
            else r = mid;

        }

        return harga[l];
    }

    static int minDiff(int hargaDicari, int[] harga) {
        // Mencari selisih minimum menggunakan binary search (berbeda dengan method hargaMax(), method ini bisa mencari selisih meskipun harga ikan > hargaDicari)
        // Untuk method S
        int l = 0;
        int r = harga.length - 1;

        if (harga[l] > hargaDicari) return harga[l] - hargaDicari;
        if (harga[r] < hargaDicari) return hargaDicari - harga[r];

        while (l < r-1) {
            int mid = (l + r) / 2;

            int diffLeft = hargaDicari - harga[l];
            int diffMid = hargaDicari - harga[mid];
            int diffRight = hargaDicari - harga[r];

            if ((diffLeft * diffMid) < 0) r = mid;
            else if ((diffRight * diffMid) < 0) l = mid;
            else if (diffLeft == 0 || diffMid == 0 || diffRight == 0) return 0;
        }

        return Math.min(Math.abs(hargaDicari - harga[l]), Math.abs(hargaDicari - harga[r]));
    }

    static int serve() {
        if (queue.isEmpty()) return -1;

        int[] pelanggan = queue.poll();

        int id = pelanggan[0];
        int budget = pelanggan[1];
        int kesabaran = pelanggan[2];

        int hargaIkan = hargaMax(budget, P);

        if (hargaIkan == -1) return -1;

        int diskon = 0;
        if (budget == hargaIkan) {
            diskon += kupon.isEmpty() ? 0 : kupon.pop();

        } else kupon.push(budget - hargaIkan);

        int pay = Math.max(1, hargaIkan-diskon);

        int kembalian = budget - pay;

        pelanggan[1] = kembalian;
        dataPelanggan[id][1] = kembalian;

        pelanggan[2] = kesabaran + t;
        dataPelanggan[id][2] = kesabaran + t;

        queue.offer(pelanggan);

        return kembalian;
    }

}
 