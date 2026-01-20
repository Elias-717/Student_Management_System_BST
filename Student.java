public class Student {
    public String name;
    public String matric;
    public double cgpa;

    public Student(String name, String matric, double cgpa) {
        this.name = name;
        this.matric = matric;
        this.cgpa = cgpa;
    }

    @Override
    public String toString() {
        return matric + " (" + name + ", CGPA " + String.format("%.2f", cgpa) + ")";
    }
}
