package model;

public class JointAccount extends Account {

    public JointAccount() {
        super("Joint", 100000.0);
    }

    @Override
    public double getMinimumDeposit() {
        return minimumDeposit;
    }
}
