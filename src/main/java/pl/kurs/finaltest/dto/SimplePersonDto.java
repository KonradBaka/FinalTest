package pl.kurs.finaltest.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
public class SimplePersonDto {


    @NotBlank(message = "Typ wymagany.")
    private String type;
    @NotBlank(message = "Imię wymagane.")
    private String firstName;
    @NotBlank(message = "Nazwisko wymagane.")
    private String lastName;
    @NotBlank(message = "PESEL wymagany.")
    private String pesel;
    @NotBlank(message = "Wzrost wymagany.")
    private Double height;
    @NotBlank(message = "Waga wymagany.")
    private Double weight;
    @Email(message = "Niewłaściwy emial")
    @NotBlank(message = "Email wymagany.")
    private String emailAddress;


    public SimplePersonDto() {
    }

    public SimplePersonDto(String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress) {
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.height = height;
        this.weight = weight;
        this.emailAddress = emailAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
