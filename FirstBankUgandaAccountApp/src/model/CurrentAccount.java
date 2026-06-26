package model;

public class CurrentAccount extends Account {

    public CurrentAccount() {
        super("Current", 200000.0);
    }

    @Override
    public double getMinimumDeposit() {
        return minimumDeposit;
    }
}
