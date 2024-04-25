package pl.kurs.finaltest.criteria;

public class PersonCriteria {

    private String type;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double heightFrom;
    private Double heightTo;
    private Double weightFrom;
    private Double weightTo;
    private String emailAddress;

    public PersonCriteria() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setHeightFrom(Double heightFrom) {
        this.heightFrom = heightFrom;
    }

    public void setHeightTo(Double heightTo) {
        this.heightTo = heightTo;
    }

    public void setWeightFrom(Double weightFrom) {
        this.weightFrom = weightFrom;
    }

    public void setWeightTo(Double weightTo) {
        this.weightTo = weightTo;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getType() {
        return type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public Double getHeightFrom() {
        return heightFrom;
    }

    public Double getHeightTo() {
        return heightTo;
    }

    public Double getWeightFrom() {
        return weightFrom;
    }

    public Double getWeightTo() {
        return weightTo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
