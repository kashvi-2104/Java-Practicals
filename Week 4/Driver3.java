public class Driver3 {
     public static void main(String[] args) {
        String template = "Dear {name}, order {id} ships {date}.";
        String[] names = {"name", "id"};
        String[] values = {"Krisha", "A17"};
        String result = Templatefiller.fill(template, names, values);
        System.out.println(result);
    }
}
