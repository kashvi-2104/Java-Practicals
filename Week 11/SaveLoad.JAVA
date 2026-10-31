
import java.io.*;

class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    int rollNo;
    String name;

    // This field will NOT be saved during serialization
    transient String password;

    Student(int rollNo, String name, String password) {
        this.rollNo = rollNo;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println(
            "Roll No: " + rollNo +
            ", Name: " + name +
            ", Password: " + password
        );
    }
}

public class SaveLoad {

    public static void main(String[] args) {

        Student[] students = {
            new Student(1, "Om", "om123"),
            new Student(2, "Rahul", "rahul123"),
            new Student(3, "Amit", "amit123")
        };

        // Save objects (Serialization)
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream("students.dat"))) {

            out.writeObject(students);

            System.out.println("Students saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load objects (Deserialization)
        try (ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream("students.dat"))) {

            Student[] loadedStudents =
                (Student[]) in.readObject();

            System.out.println("\nStudents loaded from file:");

            for (Student s : loadedStudents) {
                s.display();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
