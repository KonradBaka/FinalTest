package pl.kurs.finaltest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Entity
@Table(name = "retirees")
public class Retiree extends Person{
    private static final long serialVersionUID = 1L;

    @Column(name = "pension_amount")
    private Double pensionAmount;
    @Column(name = "years_worked")
    private Integer yearsWorked;

    public Retiree() {
    }

    public Retiree(String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress, Double pensionAmount, Integer yearsWorked) {
        super(type, firstName, lastName, pesel, height, weight, emailAddress);
        this.pensionAmount = pensionAmount;
        this.yearsWorked = yearsWorked;
    }

    public Retiree(Long id, String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress, Double pensionAmount, Integer yearsWorked) {
        super(id, type, firstName, lastName, pesel, height, weight, emailAddress);
        this.pensionAmount = pensionAmount;
        this.yearsWorked = yearsWorked;
    }

    public Double getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(Double pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public Integer getYearsWorked() {
        return yearsWorked;
    }

    public void setYearsWorked(Integer yearsWorked) {
        this.yearsWorked = yearsWorked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Retiree retiree = (Retiree) o;
        return Objects.equals(pensionAmount, retiree.pensionAmount) && Objects.equals(yearsWorked, retiree.yearsWorked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pensionAmount, yearsWorked);
    }
}
