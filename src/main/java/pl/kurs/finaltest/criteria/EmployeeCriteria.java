package pl.kurs.finaltest.criteria;

import java.time.LocalDate;

public class EmployeeCriteria extends PersonCriteria{

    private String currentPosition;
    private Double currentSalaryFrom;
    private Double currentSalaryTo;
    private LocalDate startDateOfEmploymentFrom;
    private LocalDate startDateOfEmploymentTo;

    public String getCurrentPosition() {
        return currentPosition;
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
}
