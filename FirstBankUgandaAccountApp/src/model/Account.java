package model;

public abstract class Account {
    protected String accountType;
    protected double minimumDeposit;

    public Account(String accountType, double minimumDeposit) {
        this.accountType = accountType;
        this.minimumDeposit = minimumDeposit;
    }

    public abstract double getMinimumDeposit();

    public boolean isValidDeposit(double deposit) {
        return deposit >= getMinimumDeposit();
    }

    public String getAccountType() {
        return accountType;
    }
}