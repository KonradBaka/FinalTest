package pl.kurs.finaltest.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EmployeeDto extends PersonDto {

    @Temporal(TemporalType.DATE)
    @NotNull(message = "Data rozpoczęcia pracy wymagana.")
    private LocalDate employmentStartDate;
    @NotBlank(message = "Stanowisko wymagana;")
    private String currentPosition;
    @NotNull(message = "Aktualna pensja wymagana")
    private Double currentSalary;
    @NotNull
    private Integer numberOfPositions;

    public Integer getNumberOfPositions() {
        return numberOfPositions;
    }

    public void setNumberOfPositions(Integer numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Double getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(Double currentSalary) {
        this.currentSalary = currentSalary;
    }


}
