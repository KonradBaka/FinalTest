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
