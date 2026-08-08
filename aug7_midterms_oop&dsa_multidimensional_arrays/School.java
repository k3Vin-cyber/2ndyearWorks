public class School {
    public static void main(String[] args) {
        StudentGrades records = new StudentGrades();
        
        String[] studentNames = {"Kevin", "Aleah", "Ronnel", "Jayson", "Renz", "Jemuel", "John", "Mark", "Jasper", "Jayson"};
        
        int[][] studentGrades = {
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

        records.setStudents(studentNames);
        records.setGrades(studentGrades);
        
        records.displayGrades();
    }
}