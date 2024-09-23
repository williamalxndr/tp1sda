import java.util.PriorityQueue;

public class Test {
    public static void main(String[] args) {
        PriorityQueue<int[]> queue = new PriorityQueue<>((a,b) -> {
            if (a[1]!=b[1]) return Integer.compare(b[1], a[1]);
            else if (a[2]!= b[2]) return Integer.compare(a[2], b[2]);
            else return Integer.compare(a[0], b[0]);
        });

        queue.offer(new int[]{0,20,4});
        queue.offer(new int[]{1,10,20});
        queue.offer(new int[]{2,25,10});
        queue.offer(new int[]{3,10,4});

        while (!queue.isEmpty()) {
            int[] arr = queue.poll();
            for (int i=0; i<arr.length; i++) {
                System.out.print(arr[i]);
                System.out.print(" ");
            }
            System.out.println();
        }

    }
}