class PartTime extends Employee {

    private double hours;
    private double rate;

    PartTime(String name, int id, double hours, double rate) {

        super(name, id);

        this.hours = hours;
        this.rate = rate;
    }

    @Override
    double monthlySalary() {
        return hours * rate;
    }
}