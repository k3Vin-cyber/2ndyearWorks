import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DataStructureDemos {

    public static void reverseArray() {
        System.out.println("--- Reversed Array ---");
        int[] numbers = { 5, 6, 7, 10 };

        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.println(numbers[i]);
        }
    }

    public static void stackDemo() {
        System.out.println("\n--- Stack ---");
        Stack<String> stack = new Stack<>();

        stack.push("someone");
        stack.push("Who");
        stack.push("Sino");

        System.out.println("Initial Stack: " + stack);

        stack.pop(); 

        System.out.println("After pop(): " + stack);
    }

    public static void queueDemo() {
        System.out.println("\n--- Queue ---");
        
        Queue<Integer> queue = new LinkedList<>();

        queue.add(7);
        queue.add(6);
        queue.add(11);
        queue.add(4);

        System.out.println("Initial Queue: " + queue);

        queue.poll(); 

        System.out.println("After poll(): " + queue);
    }

    public static void printEvenNumbers() {
        System.out.println("\n--- Even Numbers ---");
        int[] numbers = { 3, 5, 1, 2, 6, 8, 7 };

        for (int number : numbers) {
            if (number % 2 == 0) {
                System.out.println("even: " + number);
            }
        }
    }

    public static void main(String[] args) {
        reverseArray();
        stackDemo();
        queueDemo();
        printEvenNumbers();
    }
}