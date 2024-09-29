import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class SouvenirDP {

    static ArrayList<ArrayList<ArrayList<ArrayList<Long>>>> dp = new ArrayList<>();
    static int lastInserted = 0;
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int[] hargaSuvenir = {100, 5, 5, 6, 7, 8, 9};
        int[] kebahagiaanSuvenir = {1000, 20, 20, 90, 90, 90, 21};

        int budget = in.nextInt(); 
        in.close();

        insertDP(lastInserted, budget, hargaSuvenir, kebahagiaanSuvenir);

        printDP();
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
            System.out.println(dp);
        }


        for (long uang=lastInserted; uang<=budget; uang++) {

            ArrayList<ArrayList<ArrayList<Long>>> row = new ArrayList<ArrayList<ArrayList<Long>>>();

            System.out.println();
            System.out.print("Uang: ");
            System.out.println(uang);

            for (long j=N-1; j>=0; j--) {

                int harga = hargaSuvenir[(int) j];
                int kebahagiaan = kebahagiaanSuvenir[(int) j];


                System.out.print("j: ");
                System.out.print(j);
                System.out.print("     ");
                System.out.printf("Harga: %d, kebahagiaan: %d", harga, kebahagiaan);
                System.out.println(); 


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
                    System.out.print("First candidate: ");
                    System.out.println(firstCandidate);
                    // System.out.print("First kebahagiaan: ");
                    // System.out.println(firstKebahagiaan);
                    // System.out.print("First Indices: ");             
                    // System.out.println(firstIndices);        
                    
                }

                secondCandidate = dp.get((int) uang - 1).get((int) j);  // Atas (budget sebelumnya), tidak membeli suvenir j
                System.out.print("Second candidate: ");
                System.out.println(secondCandidate);
                secondKebahagiaan = secondCandidate.get(0).get(0);
                // System.out.print("Second kebahagiaan: ");
                // System.out.println(secondKebahagiaan);
                secondIndices = secondCandidate.get(1);    
                // System.out.print("Second Indices: ");             
                // System.out.println(secondIndices);                        
                

                if (uang >= harga) {  // Membeli suvenir j
                    
                    if (j < N-2) {
            
                        thirdCandidate = dp.get((int) uang - harga).get((int) j+2);
                        System.out.print("Third candidate(h[j] + dp[j+2]): ");
                        System.out.println(thirdCandidate);
                        thirdKebahagiaan = thirdCandidate.get(0).get(0) + kebahagiaan;
                        // System.out.print("Third kebahagiaan: ");
                        // System.out.println(thirdKebahagiaan);
                        thirdIndices.add(j);
                        thirdIndices.addAll(thirdCandidate.get(1));  
                        // System.out.print("Third Indices: ");             
                        // System.out.println(thirdIndices);    
                        
                        int hargaSebelum = hargaSuvenir[(int) j+1];  // harga suvenir sebelum (j+1)
                        int kebahagiaanSebelum = kebahagiaanSuvenir[(int) j+1];  // kebahagiaan suvenir sebelum (j+1)

                        if (j < (N-3) && uang >= (harga+hargaSebelum)) {
                            
                            fourthCandidate = dp.get((int) uang - harga - hargaSebelum).get((int) j+3);  // harga + hargaSebelum(j+1) + dp longkap 1(j+3) (agar tidak consecutive 3)
                            fourthKebahagiaan = fourthCandidate.get(0).get(0) + kebahagiaan + kebahagiaanSebelum;
                            System.out.printf("Fourth candidate(h[j](%d) + h[j+1](%d) + dp[j+3])(%d): ", kebahagiaan, kebahagiaanSebelum, fourthKebahagiaan);
                            System.out.println(fourthCandidate);
                            fourthIndices.add(j);
                            fourthIndices.add(j+1);
                            fourthIndices.addAll(fourthCandidate.get(1));                    
                        } else if ((j == (N-3))) {

                            if ((uang >= (harga + hargaSuvenir[(int) j+1])) && uang >= (harga + hargaSuvenir[(int) j+2])) {
                                System.out.print("kebahagiaanSuvenir[j+1]: ");
                                System.out.println(kebahagiaanSuvenir[(int) j+1]);

                                System.out.print("kebahagiaanSuvenir[j+2]: ");
                                System.out.println(kebahagiaanSuvenir[(int) j+2]);

                                if (kebahagiaanSuvenir[(int) j+1] >= kebahagiaanSuvenir[(int) j+2]) {
                                    fourthCandidate = row.get(0);
                                    System.out.print("Fourth candidate(h[j]: ");
                                    System.out.println(fourthCandidate);
                                    fourthKebahagiaan = kebahagiaanSuvenir[(int) j+1] + kebahagiaan;
                                    fourthIndices.add(j);
                                    fourthIndices.add(j+1);
                                } else {
                                    fourthCandidate = row.get(1);
                                    System.out.print("Fourth candidate(h[j]: ");
                                    System.out.println(fourthCandidate);
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
                            System.out.print("Third candidate: ");
                            System.out.println(thirdCandidate);
                            thirdKebahagiaan = thirdCandidate.get(0).get(0) + kebahagiaan;
                            thirdIndices.add(j);
                            thirdIndices.addAll(thirdCandidate.get(1));
                        } else if ((j == (N-1)) && harga == uang) {
                            thirdCandidate = dp.get((int) uang-harga).get((int) j);
                            System.out.print("Third candidate: ");
                            System.out.println(thirdCandidate);
                            thirdKebahagiaan = thirdCandidate.get(0).get(0) + kebahagiaan;
                            thirdIndices.add(j);
                            thirdIndices.addAll(thirdCandidate.get(1));
                        }


                    }

                }

                long maxKebahagiaan = 0;
                // System.out.print(firstCandidate);
                // System.out.print(secondCandidate);
                // System.out.print(thirdCandidate);
                // System.out.println(fourthCandidate);

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
                        resIndices.add(secondIndices);
                    }
                }
                if (thirdCandidate != null) {
                    if (thirdKebahagiaan > maxKebahagiaan) {
                        maxKebahagiaan = thirdKebahagiaan;
                        resIndices.clear();
                        resIndices.add(thirdIndices);
                    } else if (thirdKebahagiaan == maxKebahagiaan) {
                        resIndices.add(thirdIndices);
                    }
                }
                if (fourthCandidate != null) {
                    if (fourthKebahagiaan > maxKebahagiaan) {
                        maxKebahagiaan = fourthKebahagiaan;
                        resIndices.clear();
                        resIndices.add(fourthIndices);
                    }
                }

                // System.out.print("Res Indices: ");
                // System.out.println(resIndices);

                res.add(new ArrayList<>(Arrays.asList(maxKebahagiaan)));
                if (resIndices.isEmpty()) {
                    res.add(new ArrayList<>());
                } else {
                    res.addAll(new ArrayList<>(resIndices));
                }

                // System.out.print("Res: ");
                // System.out.println(res);

                row.add(0, res);
                System.out.println("ROW:");
                System.out.println(row);
            }
            dp.add(row);
            // System.out.println(dp);

        }
    }

    static int[] candidate(long uang, int harga, long j, long idx, long N) { 
        if (idx > N-1) return null;
        if (uang < harga) return null;

        ArrayList<ArrayList<Long>> ans = dp.get((int) uang - harga).get((int) idx);

        for (int i=1; i<ans.size(); i++) {
            ArrayList<Long> indices = ans.get(i);
            // System.out.print("Indices: ");
            // System.out.println(indices);
            // System.out.println(indices.contains(j));
            if (indices.contains(j)) continue;
            else if (indices.contains(j+1) && indices.contains(j+2)) continue;  
            else return new int[]{(int) idx, i};
        }
        return candidate(uang, harga, j, idx+1, N);
    }

    static int[] firstCandidate(long uang, long idx, long j, ArrayList<ArrayList<ArrayList<Long>>> row) {

        if (idx >= row.size()) return null;

        ArrayList<ArrayList<Long>> ans = row.get((int) idx);

        for (int i=1; i<ans.size(); i++) {
            ArrayList<Long> indices = ans.get(i);
            System.out.print("Indices: ");
            System.out.println(indices);
            if (indices.contains(j+1) && indices.contains(j+2)) continue;  
            else return new int[]{(int) idx, i};
        }
        return firstCandidate(uang, idx, j, row);
    }


    static void printDP() {
        for (int i=0; i<dp.size(); i++) {
            System.out.printf("%d: ",i );
            System.out.println(dp.get(i));
        }
    }

}