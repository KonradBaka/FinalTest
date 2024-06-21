package pl.kurs.finaltest.dto;

import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.annotations.PersonSubType;

@PersonSubType("retiree")
public class RetireeDto extends PersonDto{

    @NotNull(message = "Emerytura wymagana.")
    private Double pensionAmount;
    @NotNull(message = "Lata pracy wymagane.")
    private Integer yearsWorked;


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



}
