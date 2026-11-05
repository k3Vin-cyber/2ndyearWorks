import java.util.Scanner;

// ============================================================================
// OOP 1: ABSTRACTION VIA INTERFACE
// Defines the contract required for any student record to be cleared
// ============================================================================
interface Clearable {
    boolean isEligibleForClearance();
    boolean signOffClearance(String officerName, String date);
    String getClearanceBadge();
}

// ============================================================================
// DSA 1: SINGLY LINKED LIST (O(1) Tail Insertion)
// Stores a dynamic, chronological log of attended events, infractions, & payments
// ============================================================================
class ActivityNode {
    private String date;
    private String category; // "ATTENDED", "SANCTION", "PAYMENT", "CLEARANCE"
    private String description;
    private double amount;
    ActivityNode next;

    public ActivityNode(String date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.next = null;
    }

    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
}

class ActivityLinkedList {
    private ActivityNode head;
    private ActivityNode tail; // Tail pointer guarantees O(1) insertion without loops
    private int size;

    public ActivityLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Time Complexity: O(1) Constant Time
    public void addLog(String date, String category, String description, double amount) {
        ActivityNode newNode = new ActivityNode(date, category, description, amount);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public int getSize() {
        return size;
    }

    // Time Complexity: O(k) where k is the number of logged activities for this student
    public void printChronologicalLog() {
        if (head == null) {
            System.out.println("   | No events or infractions recorded yet.                             |");
            return;
        }
        ActivityNode current = head;
        int count = 1;
        while (current != null) {
            String amtText = current.getAmount() > 0
                    ? String.format("PHP %7.2f", current.getAmount())
                    : "       --  ";
            System.out.printf("   | %2d. [%-10s] %-9s | %-24s | %-10s |\n",
                    count++,
                    current.getDate(),
                    current.getCategory(),
                    truncate(current.getDescription(), 24),
                    amtText);
            current = current.next;
        }
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}

// ============================================================================
// OOP 2: ABSTRACT BASE CLASS, ENCAPSULATION & COMPOSITION
// ============================================================================
abstract class StudentRecord implements Clearable {
    private String studentId;
    private String fullName;
    private int yearLevel;
    protected double balance;
    private boolean signedOff;
    private String signedOffBy;
    private ActivityLinkedList activityLog; // Composition: StudentRecord HAS-A LinkedList

    public StudentRecord(String studentId, String fullName, int yearLevel) {
        this.studentId = studentId.toUpperCase();
        this.fullName = fullName;
        this.yearLevel = yearLevel;
        this.balance = 0.0;
        this.signedOff = false;
        this.signedOffBy = "N/A";
        this.activityLog = new ActivityLinkedList();
    }

    // Encapsulated Getters
    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public int getYearLevel() { return yearLevel; }
    public double getBalance() { return balance; }
    public boolean isSignedOff() { return signedOff; }
    public String getSignedOffBy() { return signedOffBy; }
    public ActivityLinkedList getActivityLog() { return activityLog; }

    // Abstract methods to be overridden by subclasses (Runtime Polymorphism)
    public abstract double calculateFine(double baseAmount);
    public abstract String getStatusType();

    // OOP 3: POLYMORPHISM (Method Overloading - Compile-Time)
    public double recordSanction(String date, String eventName, double baseFine) {
        return recordSanction(date, eventName, baseFine, "Unexcused Absence");
    }

    public double recordSanction(String date, String eventName, double baseFine, String remarks) {
        double computedFine = calculateFine(baseFine);
        this.balance += computedFine;
        this.signedOff = false; // Revoke sign-off if a new fine is incurred
        this.signedOffBy = "N/A";
        this.activityLog.addLog(date, "SANCTION", eventName + " (" + remarks + ")", computedFine);
        return computedFine;
    }

    public void recordAttendance(String date, String eventName) {
        this.activityLog.addLog(date, "ATTENDED", eventName, 0.0);
    }

    public void payBalance(String date, double amountPaid) {
        if (amountPaid >= this.balance) {
            double paid = this.balance;
            this.balance = 0.0;
            this.activityLog.addLog(date, "PAYMENT", "Full Fine Settlement", paid);
        } else {
            this.balance -= amountPaid;
            this.activityLog.addLog(date, "PAYMENT", "Partial Fine Payment", amountPaid);
        }
    }

    @Override
    public boolean isEligibleForClearance() {
        return this.balance <= 0.001;
    }

    @Override
    public boolean signOffClearance(String officerName, String date) {
        if (!isEligibleForClearance()) {
            return false;
        }
        this.signedOff = true;
        this.signedOffBy = officerName;
        this.activityLog.addLog(date, "CLEARANCE", "Signed by " + officerName, 0.0);
        return true;
    }

    @Override
    public String getClearanceBadge() {
        if (signedOff && isEligibleForClearance()) {
            return "[SIGNED OFF & CLEARED]";
        } else if (isEligibleForClearance()) {
            return "[ELIGIBLE - PENDING SIGN-OFF]";
        } else {
            return "[ON HOLD - UNPAID SANCTIONS]";
        }
    }
}

// ============================================================================
// OOP 4: INHERITANCE & METHOD OVERRIDING
// ============================================================================
class RegularStudentRecord extends StudentRecord {
    public RegularStudentRecord(String studentId, String fullName, int yearLevel) {
        super(studentId, fullName, yearLevel);
    }

    // Polymorphic Fine Calculation:
    // Senior students (3rd/4th year) have a 1.25x rate for mandatory department events
    @Override
    public double calculateFine(double baseAmount) {
        double yearMultiplier = (getYearLevel() >= 3) ? 1.25 : 1.00;
        return baseAmount * yearMultiplier;
    }

    @Override
    public String getStatusType() {
        return "Regular Student (Yr " + getYearLevel() + ")";
    }
}

class BoardMemberRecord extends StudentRecord {
    private String positionTitle;

    public BoardMemberRecord(String studentId, String fullName, int yearLevel, String positionTitle) {
        super(studentId, fullName, yearLevel);
        this.positionTitle = positionTitle;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    // Polymorphic Fine Calculation:
    // Student Government / Officers incur a 2.0x accountability multiplier
    @Override
    public double calculateFine(double baseAmount) {
        double officerMultiplier = 2.00;
        double seniorityAddition = (getYearLevel() == 4) ? 25.00 : 0.00;
        return (baseAmount * officerMultiplier) + seniorityAddition;
    }

    @Override
    public String getStatusType() {
        return "Board Member - " + positionTitle + " (Yr " + getYearLevel() + ")";
    }
}

// ============================================================================
// DSA 2: BINARY SEARCH TREE (Ordered by Student ID for O(log n) Search)
// ============================================================================
class BSTNode {
    StudentRecord record;
    BSTNode left;
    BSTNode right;

    public BSTNode(StudentRecord record) {
        this.record = record;
        this.left = null;
        this.right = null;
    }
}

class StudentBST {
    private BSTNode root;

    public StudentBST() {
        this.root = null;
    }

    // Time Complexity: O(log n) average, O(n) worst-case
    public boolean insert(StudentRecord record) {
        if (search(record.getStudentId()) != null) {
            return false; // Duplicate Student ID rejected
        }
        root = insertRecursive(root, record);
        return true;
    }

    private BSTNode insertRecursive(BSTNode current, StudentRecord record) {
        if (current == null) {
            return new BSTNode(record);
        }
        int cmp = record.getStudentId().compareToIgnoreCase(current.record.getStudentId());
        if (cmp < 0) {
            current.left = insertRecursive(current.left, record);
        } else if (cmp > 0) {
            current.right = insertRecursive(current.right, record);
        }
        return current;
    }

    // Time Complexity: O(log n) average
    public StudentRecord search(String studentId) {
        BSTNode node = searchRecursive(root, studentId.trim().toUpperCase());
        return (node != null) ? node.record : null;
    }

    private BSTNode searchRecursive(BSTNode current, String targetId) {
        if (current == null || current.record.getStudentId().equalsIgnoreCase(targetId)) {
            return current;
        }
        if (targetId.compareToIgnoreCase(current.record.getStudentId()) < 0) {
            return searchRecursive(current.left, targetId);
        }
        return searchRecursive(current.right, targetId);
    }

    public int countStudentsWithFines() {
        return countFinesRecursive(root);
    }

    private int countFinesRecursive(BSTNode node) {
        if (node == null) return 0;
        int currentHasFine = (node.record.getBalance() > 0.001) ? 1 : 0;
        return currentHasFine + countFinesRecursive(node.left) + countFinesRecursive(node.right);
    }

    public StudentRecord[] extractStudentsWithFines() {
        int total = countStudentsWithFines();
        StudentRecord[] result = new StudentRecord[total];
        int[] indexWrapper = {0};
        extractFinesRecursive(root, result, indexWrapper);
        return result;
    }

    private void extractFinesRecursive(BSTNode node, StudentRecord[] arr, int[] index) {
        if (node == null) return;
        extractFinesRecursive(node.left, arr, index);
        if (node.record.getBalance() > 0.001) {
            arr[index[0]++] = node.record;
        }
        extractFinesRecursive(node.right, arr, index);
    }

    // In-Order Traversal naturally prints BST records sorted by Student ID
    public void printMasterDirectory() {
        if (root == null) {
            System.out.println("   [INFO] No student records found in the BST.");
            return;
        }
        System.out.println("+------------+------------------------+------------------------------+-----------+------------------------------+");
        System.out.printf("| %-10s | %-22s | %-28s | %-9s | %-28s |\n",
                "STUDENT ID", "FULL NAME", "ROLE / CLASSIFICATION", "BALANCE", "CLEARANCE STATUS");
        System.out.println("+------------+------------------------+------------------------------+-----------+------------------------------+");
        printInOrderRecursive(root);
        System.out.println("+------------+------------------------+------------------------------+-----------+------------------------------+");
    }

    private void printInOrderRecursive(BSTNode node) {
        if (node == null) return;
        printInOrderRecursive(node.left);
        StudentRecord r = node.record;
        System.out.printf("| %-10s | %-22s | %-28s | %9.2f | %-28s |\n",
                r.getStudentId(),
                truncate(r.getFullName(), 22),
                truncate(r.getStatusType(), 28),
                r.getBalance(),
                r.getClearanceBadge());
        printInOrderRecursive(node.right);
    }

    private String truncate(String str, int max) {
        return (str.length() <= max) ? str : str.substring(0, max - 3) + "...";
    }
}

// ============================================================================
// DSA 3: QUICK SORT ALGORITHM (O(n log n) Alphabetical Sorting by Name)
// ============================================================================
class QuickSorter {
    public static void sortByNameAlphabetically(StudentRecord[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            sortByNameAlphabetically(arr, low, pivotIndex - 1);
            sortByNameAlphabetically(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(StudentRecord[] arr, int low, int high) {
        StudentRecord pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            int nameComparison = arr[j].getFullName().compareToIgnoreCase(pivot.getFullName());
            // Tie-breaker using Student ID if two students have the exact same name
            if (nameComparison < 0 || (nameComparison == 0 &&
                    arr[j].getStudentId().compareToIgnoreCase(pivot.getStudentId()) < 0)) {
                i++;
                StudentRecord temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        StudentRecord temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}

// ============================================================================
// MAIN TERMINAL APPLICATION
// ============================================================================
public class ClearanceSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentBST studentTree = new StudentBST();

    public static void main(String[] args) {
        seedInitialData();
        boolean running = true;

        while (running) {
            printBanner();
            System.out.println("  [1] Register New Student Record (BST Insert)");
            System.out.println("  [2] Log Event Attendance (Linked List O(1) Append)");
            System.out.println("  [3] Issue Absence Sanction / Fine (Polymorphic Calculation)");
            System.out.println("  [4] Search Student Clearance Profile (BST O(log n) Lookup)");
            System.out.println("  [5] Settle Fines & Sign Off Clearance");
            System.out.println("  [6] Generate Pending Fines Report (Quick Sort Alphabetical)");
            System.out.println("  [7] View Master Student Directory (BST In-Order Traversal)");
            System.out.println("  [0] Exit System");
            System.out.println("========================================================================");

            int choice = readInt("Select menu option (0-7): ", 0, 7);
            System.out.println();

            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleLogAttendance();
                    break;
                case 3:
                    handleIssueSanction();
                    break;
                case 4:
                    handleSearchStudent();
                    break;
                case 5:
                    handleSettleAndSignOff();
                    break;
                case 6:
                    handleQuickSortReport();
                    break;
                case 7:
                    System.out.println(">>> MASTER STUDENT DIRECTORY (Sorted by ID via BST In-Order Traversal)");
                    studentTree.printMasterDirectory();
                    break;
                case 0:
                    running = false;
                    System.out.println("+----------------------------------------------------------------------+");
                    System.out.println("| Thank you for using the BSIT Student Clearance & Sanction Tracker!   |");
                    System.out.println("+----------------------------------------------------------------------+");
                    break;
                default:
                    System.out.println("[ERROR] Invalid option.");
            }
        }
        scanner.close();
    }

    private static void printBanner() {
        System.out.println("\n+======================================================================+");
        System.out.println("|         STUDENT GOVERNMENT CLEARANCE & SANCTION TRACKER v2.0         |");
        System.out.println("|             OOP + DSA Capstone  |  BSIT Department Console           |");
        System.out.println("+======================================================================+");
    }

    // --- MENU 1: ADD STUDENT ---
    private static void handleAddStudent() {
        System.out.println("--- REGISTER NEW STUDENT RECORD ---");
        String id = readRequiredString("Enter Student ID (e.g., 2025-006): ").toUpperCase();
        if (studentTree.search(id) != null) {
            System.out.println("[ERROR] A student with ID " + id + " already exists in the BST!");
            return;
        }

        String name = readRequiredString("Enter Full Name (Last, First): ");
        int year = readInt("Enter Year Level (1-4): ", 1, 4);
        String isOfficer = readRequiredString("Is this student a Student Gov / Dept Board Member? (Y/N): ");

        StudentRecord newRecord;
        if (isOfficer.equalsIgnoreCase("Y") || isOfficer.equalsIgnoreCase("YES")) {
            String position = readRequiredString("Enter Position Title (e.g., SG Board Member, Vice Mayor): ");
            newRecord = new BoardMemberRecord(id, name, year, position);
        } else {
            newRecord = new RegularStudentRecord(id, name, year);
        }

        studentTree.insert(newRecord);
        System.out.println("[SUCCESS] " + name + " (" + id + ") inserted into Binary Search Tree.");
    }

    // --- MENU 2: LOG ATTENDANCE ---
    private static void handleLogAttendance() {
        System.out.println("--- LOG DEPARTMENTAL EVENT ATTENDANCE ---");
        String id = readRequiredString("Enter Student ID: ");
        StudentRecord student = studentTree.search(id);

        if (student == null) {
            System.out.println("[ERROR] Student ID '" + id.toUpperCase() + "' not found in BST.");
            return;
        }

        String date = readRequiredString("Enter Event Date (YYYY-MM-DD): ");
        String eventName = readRequiredString("Enter Event Name (e.g., BSIT General Assembly): ");
        student.recordAttendance(date, eventName);
        System.out.println("[SUCCESS] Attendance logged in O(1) time for " + student.getFullName() + ".");
    }

    // --- MENU 3: ISSUE SANCTION ---
    private static void handleIssueSanction() {
        System.out.println("--- ISSUE EVENT ABSENCE SANCTION / FINE ---");
        String id = readRequiredString("Enter Student ID: ");
        StudentRecord student = studentTree.search(id);

        if (student == null) {
            System.out.println("[ERROR] Student ID '" + id.toUpperCase() + "' not found in BST.");
            return;
        }

        System.out.println("Target Student : " + student.getFullName() + " [" + student.getStatusType() + "]");
        String date = readRequiredString("Enter Date (YYYY-MM-DD): ");
        String eventName = readRequiredString("Enter Missed Event / Duty: ");
        double baseFine = readDouble("Enter Base Fine Amount (PHP): ", 1.0, 10000.0);
        String remarks = readOptionalString("Enter Remarks (Press Enter for 'Unexcused Absence'): ");

        double appliedFine;
        if (remarks.isEmpty()) {
            // Calls overloaded method #1
            appliedFine = student.recordSanction(date, eventName, baseFine);
        } else {
            // Calls overloaded method #2
            appliedFine = student.recordSanction(date, eventName, baseFine, remarks);
        }

        System.out.printf("[SUCCESS] Polymorphic Fine Calculated: PHP %.2f (Base: PHP %.2f)\n", appliedFine, baseFine);
        System.out.printf("          New Outstanding Balance for %s: PHP %.2f\n", student.getFullName(), student.getBalance());
    }

    // --- MENU 4: SEARCH STUDENT ---
    private static void handleSearchStudent() {
        System.out.println("--- SEARCH STUDENT CLEARANCE PROFILE (BST O(log n)) ---");
        String id = readRequiredString("Enter Student ID to inspect: ");
        StudentRecord student = studentTree.search(id);

        if (student == null) {
            System.out.println("[ERROR] Student ID '" + id.toUpperCase() + "' not found.");
            return;
        }

        System.out.println("\n+----------------------------------------------------------------------+");
        System.out.printf("| STUDENT CLEARANCE PROFILE : %-40s |\n", student.getStudentId());
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| Full Name        : %-49s |\n", student.getFullName());
        System.out.printf("| Classification   : %-49s |\n", student.getStatusType());
        System.out.printf("| Pending Balance  : PHP %-45.2f |\n", student.getBalance());
        System.out.printf("| Clearance Status : %-49s |\n", student.getClearanceBadge());
        System.out.printf("| Signed Off By    : %-49s |\n", student.getSignedOffBy());
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| CHRONOLOGICAL ACTIVITY LINKED LIST (%2d entries)                      |\n",
                student.getActivityLog().getSize());
        System.out.println("+----------------------------------------------------------------------+");
        student.getActivityLog().printChronologicalLog();
        System.out.println("+----------------------------------------------------------------------+");
    }

    // --- MENU 5: SETTLE FINES & SIGN OFF CLEARANCE ---
    private static void handleSettleAndSignOff() {
        System.out.println("--- SETTLE FINES & SIGN OFF END-OF-SEMESTER CLEARANCE ---");
        String id = readRequiredString("Enter Student ID: ");
        StudentRecord student = studentTree.search(id);

        if (student == null) {
            System.out.println("[ERROR] Student ID '" + id.toUpperCase() + "' not found.");
            return;
        }

        System.out.println("Student: " + student.getFullName() + " | Current Balance: PHP " + String.format("%.2f", student.getBalance()));

        if (student.getBalance() > 0.001) {
            String payNow = readRequiredString("Student has unpaid sanctions. Record a payment now? (Y/N): ");
            if (payNow.equalsIgnoreCase("Y") || payNow.equalsIgnoreCase("YES")) {
                double payment = readDouble("Enter payment amount (PHP): ", 1.0, student.getBalance());
                String date = readRequiredString("Enter Payment Date (YYYY-MM-DD): ");
                student.payBalance(date, payment);
                System.out.printf("[INFO] Remaining Balance: PHP %.2f\n", student.getBalance());
            }
        }

        if (student.isEligibleForClearance()) {
            String confirmSign = readRequiredString("Student has PHP 0.00 balance. Sign off official clearance? (Y/N): ");
            if (confirmSign.equalsIgnoreCase("Y") || confirmSign.equalsIgnoreCase("YES")) {
                String officer = readRequiredString("Enter Approving Officer Name (e.g., VP / Vice Mayor): ");
                String date = readRequiredString("Enter Sign-Off Date (YYYY-MM-DD): ");
                student.signOffClearance(officer, date);
                System.out.println("[SUCCESS] Official Clearance SIGNED OFF for " + student.getFullName() + "!");
            }
        } else {
            System.out.println("[HOLD] Cannot sign off clearance until remaining balance is settled.");
        }
    }

    // --- MENU 6: QUICK SORT PENDING FINES REPORT ---
    private static void handleQuickSortReport() {
        System.out.println(">>> END-OF-SEMESTER PENDING FINES REPORT (Sorted Alphabetically via Quick Sort)");
        StudentRecord[] pendingList = studentTree.extractStudentsWithFines();

        if (pendingList.length == 0) {
            System.out.println("[INFO] All students have PHP 0.00 balance! No pending fines to report.");
            return;
        }

        // Execute O(n log n) Quick Sort alphabetically by student's full name
        QuickSorter.sortByNameAlphabetically(pendingList, 0, pendingList.length - 1);

        System.out.println("+-----+------------+------------------------+------------------------------+--------------+");
        System.out.printf("| %-3s | %-10s | %-22s | %-28s | %-12s |\n",
                "NO.", "STUDENT ID", "FULL NAME (A-Z)", "CLASSIFICATION", "FINE BALANCE");
        System.out.println("+-----+------------+------------------------+------------------------------+--------------+");

        double grandTotal = 0.0;
        for (int i = 0; i < pendingList.length; i++) {
            StudentRecord s = pendingList[i];
            grandTotal += s.getBalance();
            System.out.printf("| %2d. | %-10s | %-22s | %-28s | PHP %8.2f |\n",
                    (i + 1),
                    s.getStudentId(),
                    s.getFullName().length() <= 22 ? s.getFullName() : s.getFullName().substring(0, 19) + "...",
                    s.getStatusType().length() <= 28 ? s.getStatusType() : s.getStatusType().substring(0, 25) + "...",
                    s.getBalance());
        }
        System.out.println("+-----+------------+------------------------+------------------------------+--------------+");
        System.out.printf("| TOTAL COLLECTIBLE SANCTIONS FROM %2d STUDENT(S):                          | PHP %8.2f |\n",
                pendingList.length, grandTotal);
        System.out.println("+--------------------------------------------------------------------------+--------------+");
    }

    // --- PRE-LOADED SAMPLE DATA FOR IMMEDIATE DEMO ---
    private static void seedInitialData() {
        StudentRecord s1 = new BoardMemberRecord("2025-002", "Galgo, Kevin Bryan", 2, "BSIT Vice Mayor");
        s1.recordAttendance("2026-09-05", "1st Semester Orientation");
        s1.recordAttendance("2026-10-12", "BSIT General Assembly");
        s1.signOffClearance("SG Auditor", "2026-10-30");

        StudentRecord s2 = new RegularStudentRecord("2025-001", "Zaragoza, Marco", 4);
        s2.recordAttendance("2026-09-05", "1st Semester Orientation");
        s2.recordSanction("2026-10-12", "BSIT General Assembly", 100.0, "No Excuse Letter");

        StudentRecord s3 = new BoardMemberRecord("2025-004", "Alvarez, Bea", 3, "SG Board Member");
        s3.recordSanction("2026-10-18", "Emergency Budget Hearing", 150.0, "Late Arrival");

        StudentRecord s4 = new RegularStudentRecord("2025-003", "Dela Cruz, Juan", 1);
        s4.recordSanction("2026-10-12", "BSIT General Assembly", 100.0);

        StudentRecord s5 = new RegularStudentRecord("2025-005", "Mendoza, Clara", 2);
        s5.recordAttendance("2026-10-12", "BSIT General Assembly");

        studentTree.insert(s1);
        studentTree.insert(s2);
        studentTree.insert(s3);
        studentTree.insert(s4);
        studentTree.insert(s5);
    }

    // --- SAFE INPUT HELPERS (Prevents InputMismatchException crashes) ---
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.println("   [!] Please enter a whole number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("   [!] Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("   [!] Please enter an amount between %.2f and %.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("   [!] Invalid number format. Example: 100 or 150.50");
            }
        }
    }

    private static String readRequiredString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("   [!] This field cannot be blank.");
        }
    }

    private static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}