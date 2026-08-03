public class DataStructuresGatekeeper {
    
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        
        System.out.println(CYAN + "╔══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║ " + BOLD + PURPLE + "⚙️  SYSTEM LOG: Initiating Data_Structures_Gatekeeper " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════╝" + RESET);
        System.out.println(GREEN + " [OK] Status: Memory arrays initialized.\n" + RESET);

        int[] rawDataStream = {15, 22, 8, 47, 90, 3, 22, 104, 55, 22};
        
        System.out.print(BOLD + YELLOW + " 🗄️  Raw Data Stream: " + RESET);
        printArray(rawDataStream);

        int[] evenDataBlocks = extractEvens(rawDataStream);
        
        System.out.print(BOLD + CYAN + "\n 🛡️  Filtered Output (Evens Only): " + RESET);
        printArray(evenDataBlocks);

        int target = 22;
        int duplicateCount = countTargetOccurrences(rawDataStream, target);
        
        System.out.println("\n" + YELLOW + " ------------------------------------------------------" + RESET);
        System.out.println(BOLD + RED + " 🔍 Diagnostic Check: " + RESET + "Target value [" + YELLOW + target + RESET + "] detected " + RED + BOLD + duplicateCount + RESET + " times.");
        System.out.println(YELLOW + " ------------------------------------------------------" + RESET);
        
        System.out.println("\n" + GREEN + BOLD + " ✔️  System Status: Sequence complete. Garbage collection averted." + RESET);
    }

    public static int[] extractEvens(int[] data) {
        int evenCount = 0;
        for (int num : data) { 
            if (num % 2 == 0) { 
                evenCount++;
            }
        }

        int[] evens = new int[evenCount];
        int index = 0;
        
        for (int i = 0; i < data.length; i++) { 
            if (data[i] % 2 == 0) {
                evens[index] = data[i];
                index++;
            }
        }
        
        return evens; 
    }

    public static int countTargetOccurrences(int[] data, int target) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == target) { 
                count++;
            }
        }
        return count;
    }

    public static void printArray(int[] arr) {
        System.out.print(PURPLE + "[" + RESET);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(PURPLE + ", " + RESET);
            }
        }
        System.out.println(PURPLE + "]" + RESET);
    }
}