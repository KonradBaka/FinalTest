package pl.kurs.finaltest.criteria;

public class RetireeCriteria extends PersonCriteria{

    private Double pensionAmountFrom;
    private Double pensionAmountTo;
    private Integer yearsWorkedFrom;
    private Integer yearsWorkedTo;

    public RetireeCriteria() {
    }

    public void setPensionAmountFrom(Double pensionAmountFrom) {
        this.pensionAmountFrom = pensionAmountFrom;
    }

    public void setPensionAmountTo(Double pensionAmountTo) {
        this.pensionAmountTo = pensionAmountTo;
    }

    public void setYearsWorkedFrom(Integer yearsWorkedFrom) {
        this.yearsWorkedFrom = yearsWorkedFrom;
    }

    public void setYearsWorkedTo(Integer yearsWorkedTo) {
        this.yearsWorkedTo = yearsWorkedTo;
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

    @Override
    public String getType() {
        return "retiree";
    }
}
