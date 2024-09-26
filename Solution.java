import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.lang.Math;
import java.util.ArrayList;

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

    static ArrayList<ArrayList<ArrayList<Integer>>> dp = new ArrayList<>(); 
    // ArrayList of (ArrayList of (ArrayList (urutan indeks suvenir, kebahagiaan max))
    // kebahagiaan max dimasukkan sebagai ArrayList dengan ukuran 1.

    static int lastInserted = 0;

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
                    if (tipeQuery == 1) O1(x);
                    else if (tipeQuery == 2) O2(x);
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

    static void O1(int x) {
        if (x >= lastInserted) {
            insertDP(lastInserted, x, H, V);
            lastInserted = x + 1;
        }
        int kebahagiaanMaksimum = dp.get(x).getLast().get(0);
        System.out.println(kebahagiaanMaksimum);
    }

    static void O2(int x) {
        if (x >= lastInserted) {
            insertDP(lastInserted, x, H, V);
            lastInserted = x + 1;
        }

        int kebahagiaanMaksimum = dp.get(x).getLast().get(0);
        System.out.print(kebahagiaanMaksimum);
        
        ArrayList<Integer> indexMin = dp.get(x).getFirst();
        for (int i=0; i<indexMin.size(); i++) {
            System.out.print(" ");
            System.out.print(indexMin.get(i) + 1);
        }
        System.out.println();

    }


    // Method pembantu
    static void checkDepan() {

        if (queue.isEmpty()) return;

        while (queue.peek() == null || queue.peek()[2] < (t+1)) {
            queue.poll();
            if (queue.isEmpty()) return;
        }

    }

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

        if (harga[l] >= hargaDicari) return harga[l] - hargaDicari;
        if (harga[r] <= hargaDicari) return hargaDicari - harga[r];

        while (l < r-1) {
            int mid = (l + r) / 2;

            int diffMid = hargaDicari - harga[mid];

            if (diffMid > 0) l = mid;
            else if (diffMid < 0) r = mid;
            else return 0;
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

        if (hargaIkan == -1) {
            L(id);
            return id;
        }

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

    static void insertDP(int lastInserted, int budget, int[] hargaSuvenir, int[]kebahagiaanSuvenir) {

        for (int uang=lastInserted; uang<=budget; uang++) {

            int maxKebahagiaan = 0;

            ArrayList<ArrayList<Integer>> res = new ArrayList<>();

            for (int j=0; j<hargaSuvenir.length; j++) {

                int harga = hargaSuvenir[j];
                int kebahagiaan = kebahagiaanSuvenir[j];

                int sisa = uang - harga;
                
                if (sisa < 0) continue;

                ArrayList<ArrayList<Integer>> rec = sisa != harga ? dp.get(sisa) : dp.get(sisa - 1); 

                int lastKebahagiaan = rec.get(rec.size()-1).get(0);
                if ((lastKebahagiaan + kebahagiaan) < maxKebahagiaan) continue;

                for (int jj=0; jj<rec.size()-1; jj++) {
                    ArrayList<Integer> lastArrayIndex = rec.get(jj);
                    ArrayList<Integer> arrayIndex = new ArrayList<>(lastArrayIndex);

                    // Jika suvenir j sudah diambil
                    if (lastArrayIndex.contains(j) && lastKebahagiaan >= maxKebahagiaan) {
                        if (lastKebahagiaan > maxKebahagiaan) {
                            res.clear();
                            maxKebahagiaan = lastKebahagiaan;
                        }
                        if (!res.contains(arrayIndex)) {
                            res.add(arrayIndex);
                        }
                        continue;
                    } 

                    // Jika ada 3 consecutive suvenir diambil berturut turut
                    if (lastArrayIndex.contains(j+1) && lastArrayIndex.contains(j+2)) continue;  
                    if (lastArrayIndex.contains(j-1) && lastArrayIndex.contains(j+1)) continue;
                    if (lastArrayIndex.contains(j-2) && lastArrayIndex.contains(j-1)) continue;


                    if (lastArrayIndex.size() > 0 && lastArrayIndex.getFirst() > j) arrayIndex.addFirst(j);
                    else if (lastArrayIndex.size() > 0 && lastArrayIndex.getLast() < j) arrayIndex.addLast(j);
                    else if (lastArrayIndex.size() == 0) arrayIndex.add(j);
                    else continue;

                    if ((lastKebahagiaan + kebahagiaan) > maxKebahagiaan) res.clear();

                    if (res.contains(arrayIndex)) continue;
                    res.add(arrayIndex);

                    maxKebahagiaan = lastKebahagiaan + kebahagiaan;
                }
            }
            if (res.size() == 0) res.add(new ArrayList<>());

            res.add(new ArrayList<>(Arrays.asList(maxKebahagiaan)));
            dp.add(res);

        }
    
    }

}
 