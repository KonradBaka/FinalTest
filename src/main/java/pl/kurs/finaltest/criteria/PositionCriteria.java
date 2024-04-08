package pl.kurs.finaltest.criteria;

import java.time.LocalDate;

public class PositionCriteria extends EmployeeCriteria{

    private String name;
    private LocalDate startDateFrom;
    private LocalDate startDateTo;
    private LocalDate endDateFrom;
    private LocalDate endDateTo;
    private Double salaryFrom;
    private Double salaryTo;

    public PositionCriteria() {
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDateFrom() {
        return startDateFrom;
    }

    public LocalDate getStartDateTo() {
        return startDateTo;
    }

    public LocalDate getEndDateFrom() {
        return endDateFrom;
    }

    public LocalDate getEndDateTo() {
        return endDateTo;
    }

    public Double getSalaryFrom() {
        return salaryFrom;
    }

    public Double getSalaryTo() {
        return salaryTo;
    }

    @Override
    public String getType() {
        return "position";
    }
}
