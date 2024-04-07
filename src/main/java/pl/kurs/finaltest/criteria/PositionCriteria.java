package pl.kurs.finaltest.criteria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

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
