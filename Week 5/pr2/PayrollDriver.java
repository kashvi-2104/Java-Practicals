public class PayrollDriver {

    public static void main(String[] args) {

        Employee[] employees = {
            new FullTime("Rahul", 101, 50000),
            new PartTime("Amit", 102, 40, 200),
            new Intern("Kishan", 103, 10000),
            new FullTime("Priya", 104, 60000)
        };

        double total = 0;

        System.out.println("Employee Payroll:");
        System.out.println("-------------------------");

        for (Employee e : employees) {

            double salary = e.monthlySalary();

            System.out.println(
                "ID: " + e.id +
                ", Name: " + e.name +
                ", Salary: ₹" + salary
            );

            total += salary;

            if (e instanceof Intern) {
                System.out.println("Note: This employee is an Intern.");
            }
        }

        System.out.println("-------------------------");
        System.out.println("Total Payroll: ₹" + total);
    }
}