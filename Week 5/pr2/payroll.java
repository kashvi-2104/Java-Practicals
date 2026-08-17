abstract class employee{
    String name;
    int id;

    employee(String name, int id){
        this.name=name;
        this.id=id;
    }
    abstract double monthlysalary();
}

class fulltime extends employee{
   double salary;
    fulltime(String name, int id, double salary) {
        super(name, id);
        this.salary = salary;
    }
    double monthlysalary(){
        return salary;
    }
}

class partime extends employee{
    int hrs;
    float rate;

    partime(String name, int id, int hrs, float rate){
        super(name, id);
        this.hrs=hrs;
        this.rate=rate;
    }
    double monthlysalary(){
        return hrs*rate;
    }
}
class intern extends employee{
    double stipend;

    intern(String name, int id, double stipend){
        super(name, id);
        this.stipend=stipend;
    }
    double monthlysalary(){
        return stipend;
    }
}

public class payroll {
    public static void main(String[] args) {
         employee[] emp = {
            new fulltime("Krisha", 101, 50000),
            new partime("Ishita", 102, 80, 300),
            new intern("Diya", 103, 15000),
        };
        double total = 0;
        for (employee e : emp) {
            double salary = e.monthlysalary();
            System.out.println(e.name + " (" + e.id + ") = Rs " + salary);
            if (e instanceof intern) {
                System.out.println("->This employee is an intern.");
            }
            total += salary;
        }
        System.out.println("Total Payroll = Rs " + total);
    }
}
