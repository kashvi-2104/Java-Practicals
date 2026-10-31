
class Box<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {

    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericDemo {

    // Generic method
    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        // Box with Integer
        Box<Integer> intBox = new Box<>();
        intBox.set(100);

        // Box with String
        Box<String> stringBox = new Box<>();
        stringBox.set("Hello");

        System.out.println("Integer Box: " + intBox.get());
        System.out.println("String Box: " + stringBox.get());

        // Pair with different types
        Pair<String, Integer> student =
                new Pair<>("Om", 21);

        System.out.println("Name: " + student.getFirst());
        System.out.println("Age: " + student.getSecond());

        // Generic method
        String[] names = {"Om", "Rahul", "Amit"};
        Integer[] numbers = {10, 20, 30};

        System.out.println("First name: " + firstElement(names));
        System.out.println("First number: " + firstElement(numbers));
    }
}
