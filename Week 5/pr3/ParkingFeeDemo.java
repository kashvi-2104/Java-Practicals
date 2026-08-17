abstract class vehicle {
    String number;
    int allowedhrs;
    int actualhrs;

    vehicle(String number, int allowedhrs, int actualhrs) {
        this.number = number;
        this.allowedhrs = allowedhrs;
        this.actualhrs = actualhrs;
    }

    abstract double fee();
    abstract String type();
}

class Car extends vehicle {

    Car(String number, int allowedhrs, int actualhrs) {
        super(number, allowedhrs, actualhrs);
    }

    double fee() {
        int latehrs = actualhrs - allowedhrs;

        if (latehrs > 0)
            return latehrs * 20;
        return 0;
    }
    
    String type() {
        return "Car";
    }
}
class Bike extends vehicle {

    Bike(String number, int allowedhrs, int actualhrs) {
        super(number, allowedhrs, actualhrs);
    }

    double fee() {
        int latehrs = actualhrs - allowedhrs;

        if (latehrs > 0)
            return latehrs * 20;
        return 0;
    }
    String type() {
        return "Bike";
    }
}
class Truck extends vehicle {

    Truck(String number, int allowedhrs, int actualhrs) {
        super(number, allowedhrs, actualhrs);
    }

    double fee() {
        int latehrs = actualhrs - allowedhrs;

        if (latehrs > 0)
            return latehrs * 20;
        return 0;
    }
    String type() {
        return "Truck";
    }
}
public class ParkingFeeDemo {
    public static void main(String[] args) {
        vehicle[] vehicle = {
            new Car("GJ01AB1234", 2, 5),
            new Bike("GJ01CD5678", 3, 5),
            new Truck("GJ01EF9012", 4, 6)
        };

        double total = 0;
        for (vehicle v : vehicle) {
            double fee = v.fee();
            System.out.println(v.type()+" - "+ v.number + " : " + fee);
            total += fee;
        }
        System.out.println("Total Late Fees = " + total);
    }
}
