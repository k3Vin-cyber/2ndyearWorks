public class StudentGrades {
    String[] students = {"Kevin", "Aleah", "Ronnel", "Jayson", "Renz", "Jemuel", "John", "Mark", "Jasper", "Jayson"};
    
    int[][] grades = {
        {88, 91, 95},  
        {85, 89, 92},  
        {92, 94, 96},
        {92, 94, 96},
        {88, 91, 95},
        {85, 89, 92},
        {92, 94, 96},
        {92, 94, 96},
        {88, 91, 95},
        {85, 89, 92}
    };
    void displayGrades() {
        System.out.println("=====================================================");
        System.out.println("           BSIT Department - Grade Report            ");
        System.out.println("=====================================================");
        
        System.out.printf("%-10s | %-8s | %-8s | %-8s | %-8s\n", 
                          "Student", "Prelim", "Midterm", "Final", "Average");
        System.out.println("-----------------------------------------------------");
        
        for (int i = 0; i < students.length; i++) {
            
            int sum = 0; 
            
            for (int j = 0; j < grades[i].length; j++) {
                sum += grades[i][j]; 
            }
            
            double average = (double) sum / grades[i].length;
            
            System.out.printf("%-10s | %-8d | %-8d | %-8d | %-8.1f\n", 
                              students[i], grades[i][0], grades[i][1], grades[i][2], average);
        }
        System.out.println("=====================================================");
    }
}