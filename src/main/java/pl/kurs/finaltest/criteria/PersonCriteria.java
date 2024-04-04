package pl.kurs.finaltest.criteria;

public class PersonCriteria {

    private String type;
    private String name;
    private String surname;
    private String pesel;
    private Double heightFrom;
    private Double heightTo;
    private Double weightFrom;
    private Double weightTo;
    private String email;

    public PersonCriteria() {
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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

    public String getEmail() {
        return email;
    }
}
