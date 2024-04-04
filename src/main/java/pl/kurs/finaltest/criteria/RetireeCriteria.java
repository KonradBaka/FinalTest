package pl.kurs.finaltest.criteria;

public class RetireeCriteria extends PersonCriteria{

    private Double pensionAmountFrom;
    private Double pensionAmountTo;
    private Integer yearsWorkedFrom;
    private Integer yearsWorkedTo;

    public RetireeCriteria() {
    }

    public Double getPensionAmountFrom() {
        return pensionAmountFrom;
    }

    public Double getPensionAmountTo() {
        return pensionAmountTo;
    }

    public Integer getYearsWorkedFrom() {
        return yearsWorkedFrom;
    }

    public Integer getYearsWorkedTo() {
        return yearsWorkedTo;
    }
}
