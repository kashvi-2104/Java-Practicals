abstract class Shape{
    abstract double area();
}
class circle extends Shape{
    int radius;

    circle(int radius){
        this.radius=radius;
    }
    
    double area(){
        return Math.PI*radius*radius;
    }
}

class rectangle extends Shape{
    int length,breadth;

    rectangle(int len, int breadth){
        this.length=len;
        this.breadth=breadth;
    }
    
    double area(){
        return length*breadth;
    }
}

class triangle extends Shape{
    int base,height;

    triangle(int b, int ht){
        this.base=b;
        this.height=ht;
    }
    
    double area(){
        return 0.5F*base*height;
    }
}

public class Shapedemo {
    public static void main(String[] args) {
        double total=0;
        Shape[] shapes = {
            new circle(5),
            new rectangle(4, 6),
            new triangle(8, 3),
            new circle(10)};
        for(Shape s : shapes){
            double area=s.area();
            total+=area;
            System.out.printf("%s Area = %.2f%n",s.getClass().getSimpleName(), area);
        }
        System.out.printf("Total Area = %.2f", total);
    }   
}
