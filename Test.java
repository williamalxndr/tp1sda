import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        int[] hargaSuvenir = new int[] {100,5,5,6,7,8,9};
        int[] kebahagiaanSuvenir = new int[] {1000,20,20,90,90,90,21};
        int budget = 1000;

        insertDP(0, budget, hargaSuvenir, kebahagiaanSuvenir);

        Scanner in = new Scanner(System.in);

        System.out.println(dp);

        in.close();

    }

    static ArrayList<ArrayList<ArrayList<Integer>>> dp = new ArrayList<>(); 
    // ArrayList of (ArrayList of (ArrayList (urutan indeks suvenir, kebahagiaan max))
    // kebahagiaan max dimasukkan sebagai ArrayList dengan ukuran 1 di index paling terakhir

    static void insertDP(int lastInserted, int budget, int[] hargaSuvenir, int[]kebahagiaanSuvenir) {

        for (int uang=lastInserted; uang<=budget; uang++) {
            
            System.out.println();
            System.out.print("UANG: ");
            System.out.println(uang);

            int maxKebahagiaan = 0;

            ArrayList<ArrayList<Integer>> res = new ArrayList<>();

            for (int j=0; j<hargaSuvenir.length; j++) {

                int harga = hargaSuvenir[j];
                int kebahagiaan = kebahagiaanSuvenir[j];

                int sisa = uang - harga;
                
                if (sisa < 0) continue;

                System.out.print("Index Suvenir: ");
                System.out.print(j);
                System.out.print(" (Harga: ");
                System.out.printf("%d, kebahagiaan: %d)", harga, kebahagiaan);
                System.out.println();

                System.out.print("Sisa: ");
                System.out.println(sisa);

                ArrayList<ArrayList<Integer>> rec = sisa != harga ? dp.get(sisa) : dp.get(sisa - 1); 

                int lastKebahagiaan = rec.get(rec.size()-1).get(0);
                if ((lastKebahagiaan + kebahagiaan) < maxKebahagiaan) continue;

                System.out.print("Last kebahagiaan: ");
                System.out.println(lastKebahagiaan);


                for (int jj=0; jj<rec.size()-1; jj++) {
                    ArrayList<Integer> lastArrayIndex = rec.get(jj);
                    System.out.print("Last Array Index: ");
                    System.out.println(lastArrayIndex);

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
                        System.out.printf("MAX = %d", maxKebahagiaan);
                        System.out.println();
                        System.out.printf("j: %d, sisa: %d", j, sisa);
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
                    System.out.printf("MAX = Last kebahagiaan: %d + kebahagiaan: %d = %d", lastKebahagiaan, kebahagiaan, maxKebahagiaan);
                    System.out.println();
                }
                System.out.println("======================");
            }

            if (res.size() == 0) res.add(new ArrayList<>());

            res.add(new ArrayList<>(Arrays.asList(maxKebahagiaan)));
            System.out.println();
            System.out.println("RES: ");
            System.out.println(res);
            System.out.println();

            dp.add(res);

        }
    
    }
}