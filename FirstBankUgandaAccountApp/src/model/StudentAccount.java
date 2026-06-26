package model;

public class StudentAccount extends Account {

    public StudentAccount() {
        super("Student", 10000.0);
    }

    @Override
    public double getMinimumDeposit() {
        return minimumDeposit;
    }
}
