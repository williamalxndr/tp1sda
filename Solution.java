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

    static ArrayList<ArrayList<ArrayList<ArrayList<Long>>>> dp = new ArrayList<>();
    // ArrayList of (ArrayList of (ArrayList (kebahagiaan max, index suvenir))
    // kebahagiaan max dimasukkan sebagai ArrayList dengan ukuran 1 di indeks paling awal.

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
                    O(tipeQuery, x);
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

        if (x >= lastInserted) {
            insertDP(lastInserted, x, H, V);
            lastInserted = x + 1;
        }

        if (tipeQuery == 1) O1(x);
        else if (tipeQuery == 2) O2(x);
    }

    static void O1(int x) {
        ArrayList<ArrayList<Long>> jawaban = dp.get(x).get(0);
        long kebahagiaanMaksimum = jawaban.get(0).get(0);
        System.out.println(kebahagiaanMaksimum);
    }

    static void O2(int x) {
        ArrayList<ArrayList<Long>> jawaban = dp.get(x).get(0);
        long kebahagiaanMaksimum = jawaban.get(0).get(0);
        System.out.print(kebahagiaanMaksimum);
        
        ArrayList<Long> indexMin = jawaban.get(1);
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

    static void insertSort(long num, ArrayList<Long> arr) {
        // Using binary search for inserting num to sortedArray in sorted order.
        int l = 0;
        int r = arr.size() - 1;

        while (l < r-1) {
            int mid = (l + r) / 2;

            if (arr.get(mid) > num) r = mid;
            else if (arr.get(mid) < num) l = mid;
            else {
                arr.add(mid,(long) num);
                return;
            }
        }
        arr.add(r,(long) num);
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

    public static void insertDP(int lastInserted, int budget, int[] hargaSuvenir, int[] kebahagiaanSuvenir) {
        
        long N = hargaSuvenir.length;

        if (lastInserted == 0) {
            ArrayList<ArrayList<ArrayList<Long>>> row = new ArrayList<>();
            for (int ii=0; ii<N; ii++) {
                ArrayList<ArrayList<Long>> res = new ArrayList<>();
                res.add(new ArrayList<>(Arrays.asList(0L)));
                res.add(new ArrayList<>());
                row.add(res);
            } 
            dp.add(row);     
            lastInserted++;  
        }


        for (long uang=lastInserted; uang<=budget; uang++) {

            ArrayList<ArrayList<ArrayList<Long>>> row = new ArrayList<ArrayList<ArrayList<Long>>>();

            for (long j=N-1; j>=0; j--) {

                int harga = hargaSuvenir[(int) j];
                int kebahagiaan = kebahagiaanSuvenir[(int) j];

                ArrayList<ArrayList<Long>> res = new ArrayList<>();
                ArrayList<ArrayList<Long>> resIndices = new ArrayList<>();

                ArrayList<ArrayList<Long>> firstCandidate = null;
                ArrayList<ArrayList<Long>> secondCandidate = null;
                ArrayList<ArrayList<Long>> thirdCandidate = null;
                ArrayList<ArrayList<Long>> fourthCandidate = null;
                
                long firstKebahagiaan = 0;
                long secondKebahagiaan = 0;
                long thirdKebahagiaan = 0;
                long fourthKebahagiaan = 0;

                ArrayList<Long> firstIndices = new ArrayList<>();
                ArrayList<Long> secondIndices = new ArrayList<>();
                ArrayList<Long> thirdIndices = new ArrayList<>();
                ArrayList<Long> fourthIndices = new ArrayList<>();


                if (j<(N-1)) {  // Tidak membeli suvenir j
                    firstCandidate = row.get(0);  // Samping
                    firstKebahagiaan = firstCandidate.get(0).get(0);
                    firstIndices = firstCandidate.get(1);   
                }

                secondCandidate = dp.get((int) uang - 1).get((int) j);  // Atas (budget sebelumnya), tidak membeli suvenir j
                secondKebahagiaan = secondCandidate.get(0).get(0);
                secondIndices = secondCandidate.get(1);                    

                if (uang >= harga) {  // Membeli suvenir j
                    
                    if (j < N-2) {
            
                        thirdCandidate = dp.get((int) uang - harga).get((int) j+2);
                        thirdKebahagiaan = thirdCandidate.get(0).get(0) + kebahagiaan;
                        thirdIndices.add(j);
                        thirdIndices.addAll(thirdCandidate.get(1));  
                        
                        int hargaSebelum = hargaSuvenir[(int) j+1];  // harga suvenir sebelum (j+1)
                        int kebahagiaanSebelum = kebahagiaanSuvenir[(int) j+1];  // kebahagiaan suvenir sebelum (j+1)

                        if (j < (N-3) && uang >= (harga+hargaSebelum)) {
                            
                            fourthCandidate = dp.get((int) uang - harga - hargaSebelum).get((int) j+3);  // harga + hargaSebelum(j+1) + dp longkap 1(j+3) (agar tidak consecutive 3)
                            fourthKebahagiaan = fourthCandidate.get(0).get(0) + kebahagiaan + kebahagiaanSebelum;
                            fourthIndices.add(j);
                            fourthIndices.add(j+1);
                            fourthIndices.addAll(fourthCandidate.get(1));                    
                        } else if ((j == (N-3))) {

                            if ((uang >= (harga + hargaSuvenir[(int) j+1])) && uang >= (harga + hargaSuvenir[(int) j+2])) {

                                if (kebahagiaanSuvenir[(int) j+1] >= kebahagiaanSuvenir[(int) j+2]) {
                                    fourthCandidate = row.get(0);
                                    fourthKebahagiaan = kebahagiaanSuvenir[(int) j+1] + kebahagiaan;
                                    fourthIndices.add(j);
                                    fourthIndices.add(j+1);
                                } else {
                                    fourthCandidate = row.get(1);
                                    fourthKebahagiaan = kebahagiaanSuvenir[(int) j+2] + kebahagiaan;
                                    fourthIndices.add(j);
                                    fourthIndices.add(j+2);
                                }
                            } else if (uang >= (harga + hargaSuvenir[(int) j+1])) {
                                fourthCandidate = row.get(0);
                                fourthKebahagiaan = kebahagiaanSuvenir[(int) j+1] + kebahagiaan;
                                fourthIndices.add(j);
                                fourthIndices.add(j+1);
                            } else if (uang >= (harga + hargaSuvenir[(int) j+2])) {
                                fourthCandidate = row.get(1);
                                fourthKebahagiaan = kebahagiaanSuvenir[(int) j+2] + kebahagiaan;
                                fourthIndices.add(j);
                                fourthIndices.add(j+2);
                            } else if (uang >= harga) {
                                fourthCandidate = new ArrayList<>();
                                fourthKebahagiaan = kebahagiaan;
                                fourthIndices.add(j);
                            }

                        }
                    } else if (j >= (N-2)) {

                        if (j == (N-2)) {
                            thirdCandidate = dp.get((int) uang-harga).get((int) j+1);
                            thirdKebahagiaan = thirdCandidate.get(0).get(0) + kebahagiaan;
                            thirdIndices.add(j);
                            thirdIndices.addAll(thirdCandidate.get(1));
                        } else if ((j == (N-1)) && harga == uang) {
                            thirdCandidate = dp.get((int) uang-harga).get((int) j);
                            thirdKebahagiaan = thirdCandidate.get(0).get(0) + kebahagiaan;
                            thirdIndices.add(j);
                            thirdIndices.addAll(thirdCandidate.get(1));
                        }
                    }

                }

                long maxKebahagiaan = 0;

                if (firstCandidate != null) {
                    if (firstKebahagiaan > maxKebahagiaan) {
                        maxKebahagiaan = firstKebahagiaan;
                        resIndices.add(firstIndices);
                    }
                }
                if (secondCandidate != null) {
                    if (secondKebahagiaan > maxKebahagiaan) {
                        maxKebahagiaan = secondKebahagiaan;
                        resIndices.clear();
                        resIndices.add(secondIndices);
                    } else if (secondKebahagiaan == maxKebahagiaan) {
                        if (resIndices.isEmpty()) resIndices.add(secondIndices);
                        else resIndices.add(moreLexicographical(resIndices.remove(0), secondIndices));
                    }
                }
                if (thirdCandidate != null) {
                    if (thirdKebahagiaan > maxKebahagiaan) {
                        maxKebahagiaan = thirdKebahagiaan;
                        resIndices.clear();
                        resIndices.add(thirdIndices);
                    } else if (thirdKebahagiaan == maxKebahagiaan) {
                        if (resIndices.isEmpty()) resIndices.add(thirdIndices);
                        else resIndices.add(moreLexicographical(resIndices.remove(0), thirdIndices));
                    }
                }
                if (fourthCandidate != null) {
                    if (fourthKebahagiaan > maxKebahagiaan) {
                        maxKebahagiaan = fourthKebahagiaan;
                        resIndices.clear();
                        resIndices.add(fourthIndices);
                    } else if (fourthKebahagiaan == maxKebahagiaan) {
                        if (resIndices.isEmpty()) resIndices.add(fourthIndices);
                        else resIndices.add(moreLexicographical(resIndices.remove(0), fourthIndices));
                    }
                }

                res.add(new ArrayList<>(Arrays.asList(maxKebahagiaan)));
                if (resIndices.isEmpty()) {
                    res.add(new ArrayList<>());
                } else {
                    res.addAll(new ArrayList<>(resIndices));
                }

                row.add(0, res);
            }
            dp.add(row);

        }
    }
    
    static ArrayList<Long> moreLexicographical(ArrayList<Long> arr1, ArrayList<Long> arr2) {
        int n = Math.min(arr1.size(), arr2.size());
        
        for (int i = 0; i < n; i++) {
            if (arr1.get(i) < arr2.get(i)) {
                return arr1;
            } else if (arr1.get(i) > arr2.get(i)) {
                return arr2;
            }
        }

        if (arr1.size() < arr2.size()) {
            return arr1;
        } else {
            return arr2;
        }
    }




    static void printDP() {
        for (int i=0; i<dp.size(); i++) {
            System.out.printf("%d: ",i );
            System.out.println(dp.get(i));
        }
    }


}
 