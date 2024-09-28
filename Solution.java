import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Stack;
import java.lang.Math;
import java.util.ArrayList;



public class Solution {

    private static InputReader in;
    private static PrintWriter out;

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
    static int[] kesabaranAwal;

    static ArrayList<ArrayList<ArrayList<Integer>>> dp = new ArrayList<>(); 
    // ArrayList of (ArrayList of (ArrayList (urutan indeks suvenir, kebahagiaan max))
    // kebahagiaan max dimasukkan sebagai ArrayList dengan ukuran 1 di indeks paling akhir.

    static int lastInserted = 0;

    public static void main(String[] args) {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInteger();  // Banyaknya ikan
        int M = in.nextInteger();  // Banyaknya suvenir
        int Q = in.nextInteger();  // Banyaknya aktivitas

        P = new int[N];
        H = new int[M];
        V = new int[M];
        
        dataPelanggan = new int[Q][3];
        kesabaranAwal = new int[Q];

        for (int i=0; i<N; i++) {
            P[i] = in.nextInteger();
        }

        for (int i=0; i<M; i++) {
            H[i] = in.nextInteger();
        }

        for (int i=0; i<M; i++) {
            V[i] = in.nextInteger();
        }

        for (int i=0; i<Q; i++) {
            t = i;
            checkDepan();  // Cek kesabaran orang paling depan, jika sudah marah pop sampai ketemu yang masih sabar

            String query = in.next();

            switch (query) {
                case "A":  // Query A = masukkan pelanggan baru ke queue
                    int budget = in.nextInteger();
                    int kesabaran = in.nextInteger();
                    System.out.println(A(budget, kesabaran));
                    break;

                case "S":  // Query S = selisih minimum antara <hargaDicari> dengan semua harga ikan di toko
                    int hargaDicari = in.nextInteger();
                    System.out.println(S(hargaDicari));
                    break;

                case "L":  // Query L = keluarkan pelanggan dengan id <idPelanggan> 
                    int idPelanggan = in.nextInteger();
                    System.out.println(L(idPelanggan));
                    break;

                case "D":  // Tambahkan kupon diskon senilai <diskon> di atas tumpukan kupon diskon
                    int diskon = in.nextInteger();
                    System.out.println(D(diskon));
                    break;

                case "B":  // Layani pelanggan di antrian paling depan
                    System.out.println(B());
                    break;

                case "O":  // 
                    int tipeQuery = in.nextInteger();
                    int x = in.nextInteger();
                    if (tipeQuery == 1) O1(x);
                    else if (tipeQuery == 2) O2(x);
                    break;
                
                case "DEBUG":
                    printQueue();
                    printKupon();
                    break;
            }
        }

        out.close();
        
    }

    // Method2 query
    static int A(int budget, int kesabaran) {
        int[] newPelanggan = new int[]{id, budget, kesabaran + t};
        queue.offer(newPelanggan);
        dataPelanggan[id] = newPelanggan;
        kesabaranAwal[id] = kesabaran;
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

    static void O(int tipeQuery, int x) {

        // int[] pelanggan = queue.poll();

        // if (pelanggan != null) {
        //     int id = pelanggan[0];
        //     int kesabaran = kesabaranAwal[id];
    
        //     pelanggan[2] = kesabaran + t;
        //     dataPelanggan[id][2] = kesabaran + t;

        //     queue.offer(pelanggan);
        // }

        if (tipeQuery == 1) O1(x);
        else if (tipeQuery == 2) O2(x);
    }

    static void O1(int x) {
        if (x >= lastInserted) {
            insertDP(lastInserted, x, H, V);
            lastInserted = x + 1;
        }
        ArrayList<ArrayList<Integer>> jawaban = dp.get(x);
        int kebahagiaanMaksimum = jawaban.get(jawaban.size()-1).get(0);
        System.out.println(kebahagiaanMaksimum);
    }

    static void O2(int x) {
        if (x >= lastInserted) {
            insertDP(lastInserted, x, H, V);
            lastInserted = x + 1;
        }
        ArrayList<ArrayList<Integer>> jawaban = dp.get(x);
        int kebahagiaanMaksimum = jawaban.get(jawaban.size()-1).get(0);
        System.out.print(kebahagiaanMaksimum);
        
        ArrayList<Integer> indexMin = dp.get(x).get(0);
        for (int i=0; i<indexMin.size(); i++) {
            System.out.print(" ");
            System.out.print(indexMin.get(i) + 1);
        }
        System.out.println();

    }


    // Method pembantu
    static void checkDepan() {  // Cek kesabaran orang paling depan, jika sudah marah pop sampai ketemu yang masih sabar

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
        if (budget >= harga[r]) return harga[r];

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
        int kesabaran = kesabaranAwal[id];

        int hargaIkan = hargaMax(budget, P);

        if (hargaIkan == -1) {  // Jika pelanggan tidak bisa membeli ikan apapun
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
                    if (lastArrayIndex.contains(j)) {
                        if (lastKebahagiaan >= maxKebahagiaan) {
                            if (lastKebahagiaan > maxKebahagiaan) {
                                res.clear();
                                maxKebahagiaan = lastKebahagiaan;
                            }
                            if (!res.contains(arrayIndex)) {
                                res.add(arrayIndex);
                            }
                        }
                        continue;
                    }

                    // Jika ada 3 consecutive suvenir diambil berturut turut
                    if (lastArrayIndex.contains(j+1) && lastArrayIndex.contains(j+2)) continue;  
                    if (lastArrayIndex.contains(j-1) && lastArrayIndex.contains(j+1)) continue;
                    if (lastArrayIndex.contains(j-2) && lastArrayIndex.contains(j-1)) continue;


                    if (lastArrayIndex.size() > 0 && lastArrayIndex.get(0) > j) arrayIndex.add(0, j);
                    else if (lastArrayIndex.size() > 0 && lastArrayIndex.get(lastArrayIndex.size()-1) < j) arrayIndex.add(j);
                    else if (lastArrayIndex.size() == 0) arrayIndex.add(j);
                    else insertSort(j, arrayIndex);

                    if ((lastKebahagiaan + kebahagiaan) > maxKebahagiaan) res.clear();

                    if (res.contains(arrayIndex)) continue;
                    res.add(arrayIndex);

                    maxKebahagiaan = lastKebahagiaan + kebahagiaan;
                }
            }
    
            if (res.size() == 0) res.add(new ArrayList<>());

            res.add(new ArrayList<>(Arrays.asList(maxKebahagiaan)));

            if (uang > 0) {
                ArrayList<ArrayList<Integer>> recSebelum = dp.get(uang-1);
                int kebahagiaanSebelum = recSebelum.get(recSebelum.size()-1).get(0);
                if (kebahagiaanSebelum > maxKebahagiaan) {
                    res.clear();
                    res.addAll(recSebelum);
                }
            }
            dp.add(res);
        }
    }

    static void insertSort(int num, ArrayList<Integer> arr) {
        // Using binary search for inserting num to sortedArray in sorted order.
        int l = 0;
        int r = arr.size() - 1;

        while (l < r-1) {
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


    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            return Integer.parseInt(next());
        }
    }


}
 