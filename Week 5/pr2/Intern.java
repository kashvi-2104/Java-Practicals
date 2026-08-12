class Intern extends Employee {

    private double stipend;

    Intern(String name, int id, double stipend) {

        super(name, id);

        this.stipend = stipend;
    }

    @Override
    double monthlySalary() {
        return stipend;
    }
}