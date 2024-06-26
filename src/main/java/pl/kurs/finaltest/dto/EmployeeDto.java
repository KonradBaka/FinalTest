package pl.kurs.finaltest.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.annotations.PersonSubType;

import java.time.LocalDate;
import java.util.Set;

@PersonSubType("employee")
public class EmployeeDto extends PersonDto {

    @Temporal(TemporalType.DATE)
    @NotNull(message = "Data rozpoczęcia pracy wymagana.")
    private LocalDate employmentStartDate;
    @NotBlank(message = "Stanowisko wymagana;")
    private String currentPosition;
    @NotNull(message = "Aktualna pensja wymagana")
    private Double currentSalary;
    private Set<SimplePositionDto> positions;

    public Set<SimplePositionDto> getPositions() {
        return positions;
    }

    public void setPositions(Set<SimplePositionDto> positions) {
        this.positions = positions;
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
