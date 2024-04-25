package pl.kurs.finaltest.criteria;

import java.time.LocalDate;

public class EmployeeCriteria extends PersonCriteria{

    private String currentPosition;
    private Double currentSalaryFrom;
    private Double currentSalaryTo;
    private LocalDate startDateOfEmploymentFrom;
    private LocalDate startDateOfEmploymentTo;

    public EmployeeCriteria() {
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCurrentSalaryFrom(Double currentSalaryFrom) {
        this.currentSalaryFrom = currentSalaryFrom;
    }

    public void setCurrentSalaryTo(Double currentSalaryTo) {
        this.currentSalaryTo = currentSalaryTo;
    }

    public void setStartDateOfEmploymentFrom(LocalDate startDateOfEmploymentFrom) {
        this.startDateOfEmploymentFrom = startDateOfEmploymentFrom;
    }

    public void setStartDateOfEmploymentTo(LocalDate startDateOfEmploymentTo) {
        this.startDateOfEmploymentTo = startDateOfEmploymentTo;
    }

    public Double getCurrentSalaryFrom() {
        return currentSalaryFrom;
    }

    public Double getCurrentSalaryTo() {
        return currentSalaryTo;
    }

    public LocalDate getStartDateOfEmploymentFrom() {
        return startDateOfEmploymentFrom;
    }

    public LocalDate getStartDateOfEmploymentTo() {
        return startDateOfEmploymentTo;
    }

    @Override
    public String getType() {
        return "emoloyee";
    }
}
