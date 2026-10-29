import java.lang.annotation.*;
import java.lang.reflect.Field;

// Column annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

// Student class
class Student {

    @Column(name = "name")
    String name;

    @Column(name = "age")
    String age;

    @Column(name = "city")
    String city;

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("City: " + city);
    }
}

// Main class
public class Main3 {

    public static void main(String[] args) {

        String[] headers = {"name", "age", "city"};

        String[] data = {"Kashvi", "18", "Ahmedabad"};

        Student student = new Student();

        Field[] fields = Student.class.getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Column.class)) {

                Column column = field.getAnnotation(Column.class);

                String columnName = column.name();

                // Find matching header
                for (int i = 0; i < headers.length; i++) {

                    if (headers[i].equals(columnName)) {

                        try {
                            field.setAccessible(true);
                            field.set(student, data[i]);
                        } catch (Exception e) {
                            System.out.println("Error setting value");
                        }

                    }
                }
            }
        }

        student.display();
    }
}

