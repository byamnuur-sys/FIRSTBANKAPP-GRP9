package model;

public class FixedDepositAccount extends Account {

    public FixedDepositAccount() {
        super("Fixed Deposit", 1000000.0);
    }

    @Override
    public double getMinimumDeposit() {
        return minimumDeposit;
    }
}