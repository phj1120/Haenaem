package xyz.parkh.challenge.domain.challenge.model;

public enum ChallengePeriodType {
    HUNDRED(98), MONTHLY(28), WEEKLY(7), DAILY(1);

    private final int days;

    ChallengePeriodType(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
