public class StudentGrades {
    private String[] students;
    private int[][] grades;

    public void setStudents(String[] students) {
        this.students = students;
    }

    public void setGrades(int[][] grades) {
        this.grades = grades;
    }

    public String[] getStudents() {
        return students;
    }

    public int[][] getGrades() {
        return grades;
    }

    public void displayGrades() {
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