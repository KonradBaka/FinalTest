package pl.kurs.finaltest.database.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "employees")
public class Employee extends Person {
    private static final long serialVersionUID = 1L;

    @Column(name = "employment stary date")
    private LocalDate employmentStartDate;
    @Column(name = "current position")
    private String currentPosition;
    @Column(name = "ecurrent salary")
    private Double currentSalary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Position> positions = new HashSet<>();


    public Employee() {
    }

    public Employee(String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress, LocalDate employmentStartDate, String currentPosition, Double currentSalary, Set<Position> positions) {
        super(type, firstName, lastName, pesel, height, weight, emailAddress);
        this.employmentStartDate = employmentStartDate;
        this.currentPosition = currentPosition;
        this.currentSalary = currentSalary;
        this.positions = positions;
    }

    public Employee(Long id, String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress, LocalDate employmentStartDate, String currentPosition, Double currentSalary, Set<Position> positions) {
        super(id, type, firstName, lastName, pesel, height, weight, emailAddress);
        this.employmentStartDate = employmentStartDate;
        this.currentPosition = currentPosition;
        this.currentSalary = currentSalary;
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

    public Set<Position> getPositions() {
        return positions.stream().map(pos -> {
            pos.setEmployee(null);
            return pos;
        }).collect(Collectors.toSet());
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employmentStartDate, employee.employmentStartDate) && Objects.equals(currentPosition, employee.currentPosition) && Objects.equals(currentSalary, employee.currentSalary) && Objects.equals(positions, employee.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employmentStartDate, currentPosition, currentSalary, positions);
    }
}
