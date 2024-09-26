import java.util.Arrays;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        int[] hargaSuvenir = new int[] {100,5,5,6,7,8,9};
        int[] kebahagiaanSuvenir = new int[] {1000,20,20,90,90,90,21};
        int budget = 10;

        O(1, budget, hargaSuvenir, kebahagiaanSuvenir);

        System.out.println(dp);
    }

    static ArrayList<ArrayList<ArrayList<Integer>>> dp = new ArrayList<>(); 
    // ArrayList of (ArrayList of (ArrayList (urutan indeks suvenir yang diurutkan secara leksikografis), int kebahagiaan max))
    // 

    static void O(int lastInserted, int budget, int[] hargaSuvenir, int[]kebahagiaanSuvenir) {

        for (int uang=lastInserted; uang<=budget; uang++) {

            int maxKebahagiaan = 0;

            ArrayList<ArrayList<Integer>> res = new ArrayList<>();

            for (int j=0; j<hargaSuvenir.length; j++) {

                int harga = hargaSuvenir[j];
                int kebahagiaan = kebahagiaanSuvenir[j];

                int sisa = uang - harga;
                
                if (sisa < 0) continue;

                ArrayList<ArrayList<Integer>> rec = dp.get(sisa);

                int lastKebahagiaan = rec.get(rec.size()-1).get(0);
                if ((lastKebahagiaan + kebahagiaan) < maxKebahagiaan) continue;

                for (int jj=0; jj<rec.size()-1; jj++) {
                    ArrayList<Integer> lastArrayIndex = rec.get(jj);

                    // Jika suvenir j sudah diambil
                    if (lastArrayIndex.contains(j)) continue;

                    // Jika ada 3 consecutive suvenir diambil berturut turut
                    if (lastArrayIndex.contains(j+1) && lastArrayIndex.contains(j+2)) continue;  
                    if (lastArrayIndex.contains(j-1) && lastArrayIndex.contains(j+1)) continue;
                    if (lastArrayIndex.contains(j-2) && lastArrayIndex.contains(j-1)) continue;

                    ArrayList<Integer> arrayIndex = new ArrayList<>(lastArrayIndex);
                    arrayIndex.add(j);

                    if ((lastKebahagiaan + kebahagiaan) > maxKebahagiaan) res.clear();
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