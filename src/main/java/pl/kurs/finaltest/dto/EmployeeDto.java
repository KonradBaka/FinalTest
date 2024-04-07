package pl.kurs.finaltest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public class EmployeeDto extends PersonDto {

    @Temporal(TemporalType.DATE)
    @NotNull(message = "Data rozpoczęcia pracy wymagana.")
    private LocalDate employmentStartDate;
    @NotBlank(message = "Stanowisko wymagana;")
    private String currentPosition;
    @NotNull(message = "Aktualna pensja wymagana")
    private Double currentSalary;


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

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "employmentStartDate=" + employmentStartDate +
                ", currentPosition='" + currentPosition + '\'' +
                ", currentSalary=" + currentSalary +
                '}';
    }

    @Override
    public String getTypeIdentifier() {
        return "employee";
    }
}
