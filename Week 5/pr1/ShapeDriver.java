public class ShapeDriver {

    public static void main(String[] args) {

        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(10, 3),
            new Circle(3)
        };

        double total = 0;
        double largest = 0;

        System.out.println("Shape Areas:");

        for (Shape s : shapes) {

            double area = s.area();

            System.out.printf("Area = %.2f%n", area);

            total += area;

            if (area > largest) {
                largest = area;
            }
        }

        System.out.printf("%nTotal Area = %.2f%n", total);
        System.out.printf("Largest Area = %.2f%n", largest);
    }
}