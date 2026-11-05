import java.util.Arrays;

public class BigONotations {
    public static int getFirstElement(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        return arr[0];
    }
    public static boolean isEven(int n) {
        return (n & 1) == 0;
    }

    public static int binarySearch(int[] sortedArr, int target) {
        if (sortedArr == null) return -1;
        
        int low = 0;
        int high = sortedArr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; 

            if (sortedArr[mid] == target) {
                return mid;           
            } else if (sortedArr[mid] < target) {
                low = mid + 1;        
            } else {
                high = mid - 1;       
            }
        }
        return -1; 
    }
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        
        int max = arr[0];
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }
    public static long sumArray(int[] arr) {
        if (arr == null) return 0;
        
        long sum = 0;
        for (int num : arr) {   
            sum += num;
        }
        return sum;
    }
    public static void mergeSort(int[] arr, int left, int right) {
        if (arr == null || left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);          
        mergeSort(arr, mid + 1, right);     
        merge(arr, left, mid, right);       
    }
    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr  = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        while (i < n1) arr[k++] = leftArr[i++];
        while (j < n2) arr[k++] = rightArr[j++];
    }
    public static void bubbleSort(int[] arr) {
        if (arr == null) return;
        
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j]   = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
    public static void printDuplicatePairs(int[] arr) {
        if (arr == null) return;
        
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] == arr[j]) {
                    System.out.printf("Duplicate pair: (%d, %d)%n", arr[i], arr[j]);
                }
            }
        }
    }
    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        if (A == null || B == null || A.length == 0 || A[0].length != B.length) {
            throw new IllegalArgumentException("Invalid matrices for multiplication.");
        }
        
        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
    public static void threeSum(int[] arr) {
        if (arr == null || arr.length < 3) return;
        
        int n = arr.length;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (arr[i] + arr[j] + arr[k] == 0) {
                        System.out.printf("Triplet: [%d, %d, %d]%n", arr[i], arr[j], arr[k]);
                    }
                }
            }
        }
    }
    public static long fibonacciExponential(int n) {
        if (n <= 1) return n;
        return fibonacciExponential(n - 1) + fibonacciExponential(n - 2);
    }
    public static void printAllSubsets(int[] arr) {
        if (arr == null) return;
        
        int n = arr.length;
        int total = 1 << n;

        for (int mask = 0; mask < total; mask++) {
            System.out.print("{ ");
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    System.out.print(arr[i] + " ");
                }
            }
            System.out.println("}");
        }
    }
    public static void generatePermutations(int[] arr, int start) {
        if (arr == null) return;
        
        if (start == arr.length - 1) {
            System.out.println(Arrays.toString(arr));
            return;
        }

        for (int i = start; i < arr.length; i++) {
            swap(arr, start, i);                  
            generatePermutations(arr, start + 1); 
            swap(arr, start, i);                  
        }
    }
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i]   = arr[j];
        arr[j]   = temp;
    }
    public static boolean isPrime(int n) {
        if (n < 2) return false;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    public static void main(String[] args) {

        System.out.println("=== O(1) — Constant ===");
        int[] sample = {10, 20, 30, 40, 50};
        System.out.println("First element : " + getFirstElement(sample));
        System.out.println("Is 42 even?   : " + isEven(42));

        System.out.println("\n=== O(log n) — Logarithmic ===");
        int[] sorted = {1, 3, 5, 7, 9, 11, 13};
        System.out.println("Index of 7    : " + binarySearch(sorted, 7));
        System.out.println("Index of 6    : " + binarySearch(sorted, 6));

        System.out.println("\n=== O(n) — Linear ===");
        int[] nums = {4, 2, 9, 1, 7, 3};
        System.out.println("Max value     : " + findMax(nums));
        System.out.println("Sum           : " + sumArray(nums));

        System.out.println("\n=== O(n log n) — Linearithmic ===");
        int[] toSort = {5, 3, 8, 4, 2};
        mergeSort(toSort, 0, toSort.length - 1);
        System.out.println("Merge sorted  : " + Arrays.toString(toSort));

        System.out.println("\n=== O(n²) — Quadratic ===");
        int[] bubbleArr = {64, 34, 25, 12, 22, 11};
        bubbleSort(bubbleArr);
        System.out.println("Bubble sorted : " + Arrays.toString(bubbleArr));
        System.out.println("Duplicate pairs in {1,2,3,1,2}:");
        printDuplicatePairs(new int[]{1, 2, 3, 1, 2});

        System.out.println("\n=== O(n³) — Cubic ===");
        int[][] A = {{1, 2}, {3, 4}};
        int[][] B = {{5, 6}, {7, 8}};
        int[][] C = multiplyMatrices(A, B);
        System.out.printf("Matrix multiply result: [%d, %d] [%d, %d]%n", C[0][0], C[0][1], C[1][0], C[1][1]);
        System.out.println("3-Sum triplets in {-1,0,1,2,-1,-4}:");
        threeSum(new int[]{-1, 0, 1, 2, -1, -4});

        System.out.println("\n=== O(2ⁿ) — Exponential ===");
        System.out.println("Fibonacci(10) : " + fibonacciExponential(10));
        System.out.println("All subsets of {1,2,3}:");
        printAllSubsets(new int[]{1, 2, 3});

        System.out.println("\n=== O(n!) — Factorial ===");
        System.out.println("All permutations of {1,2,3}:");
        generatePermutations(new int[]{1, 2, 3}, 0);

        System.out.println("\n=== BONUS O(√n) — Square Root ===");
        System.out.println("Is 97 prime?  : " + isPrime(97));
        System.out.println("Is 100 prime? : " + isPrime(100));
    }
}