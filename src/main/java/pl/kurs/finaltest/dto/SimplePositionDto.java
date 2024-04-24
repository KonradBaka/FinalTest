package pl.kurs.finaltest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SimplePositionDto {

    @NotBlank(message = "Nazwa stanowiska wymagana.")
    private String name;
    @NotNull(message = "Data rozpoczęcia pracy wymagana.")
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull(message = "Pensja wymagana.")
    private Double salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
