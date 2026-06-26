package model;

public class SavingsAccount extends Account {

    public SavingsAccount() {
        super("Savings", 50000.0);
    }

    @Override
    public double getMinimumDeposit() {
        return minimumDeposit;
    }
}
