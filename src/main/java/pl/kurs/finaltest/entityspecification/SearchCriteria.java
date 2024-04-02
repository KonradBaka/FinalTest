package pl.kurs.finaltest.entityspecification;

import java.time.LocalDate;

public class SearchCriteria {

    // Common criteria for all types
    private String name;
    private String surname;
    private String pesel;
    private Double heightFrom;
    private Double heightTo;
    private Double weightFrom;
    private Double weightTo;
    private String email;
    private String type;

    // Criteria specific to Students
    private String universityName;
    private Integer yearOfStudy;
    private String fieldOfStudy;
    private Double scholarshipFrom;
    private Double scholarshipTo;

    // Criteria specific to Employees
    private String currentPosition;
    private Double currentSalaryFrom;
    private Double currentSalaryTo;
    private LocalDate startDateOfEmploymentFrom;
    private LocalDate startDateOfEmploymentTo;

    // Criteria specific to Retirees
    private Double pensionAmountFrom;
    private Double pensionAmountTo;
    private Integer yearsWorkedFrom;
    private Integer yearsWorkedTo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public Double getHeightFrom() {
        return heightFrom;
    }

    public void setHeightFrom(Double heightFrom) {
        this.heightFrom = heightFrom;
    }

    public Double getHeightTo() {
        return heightTo;
    }

    public void setHeightTo(Double heightTo) {
        this.heightTo = heightTo;
    }

    public Double getWeightFrom() {
        return weightFrom;
    }

    public void setWeightFrom(Double weightFrom) {
        this.weightFrom = weightFrom;
    }

    public Double getWeightTo() {
        return weightTo;
    }

    public void setWeightTo(Double weightTo) {
        this.weightTo = weightTo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Double getScholarshipFrom() {
        return scholarshipFrom;
    }

    public void setScholarshipFrom(Double scholarshipFrom) {
        this.scholarshipFrom = scholarshipFrom;
    }

    public Double getScholarshipTo() {
        return scholarshipTo;
    }

    public void setScholarshipTo(Double scholarshipTo) {
        this.scholarshipTo = scholarshipTo;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Double getCurrentSalaryFrom() {
        return currentSalaryFrom;
    }

    public void setCurrentSalaryFrom(Double currentSalaryFrom) {
        this.currentSalaryFrom = currentSalaryFrom;
    }

    public Double getCurrentSalaryTo() {
        return currentSalaryTo;
    }

    public void setCurrentSalaryTo(Double currentSalaryTo) {
        this.currentSalaryTo = currentSalaryTo;
    }

    public LocalDate getStartDateOfEmploymentFrom() {
        return startDateOfEmploymentFrom;
    }

    public void setStartDateOfEmploymentFrom(LocalDate startDateOfEmploymentFrom) {
        this.startDateOfEmploymentFrom = startDateOfEmploymentFrom;
    }

    public LocalDate getStartDateOfEmploymentTo() {
        return startDateOfEmploymentTo;
    }

    public void setStartDateOfEmploymentTo(LocalDate startDateOfEmploymentTo) {
        this.startDateOfEmploymentTo = startDateOfEmploymentTo;
    }

    public Double getPensionAmountFrom() {
        return pensionAmountFrom;
    }

    public void setPensionAmountFrom(Double pensionAmountFrom) {
        this.pensionAmountFrom = pensionAmountFrom;
    }

    public Double getPensionAmountTo() {
        return pensionAmountTo;
    }

    public void setPensionAmountTo(Double pensionAmountTo) {
        this.pensionAmountTo = pensionAmountTo;
    }

    public Integer getYearsWorkedFrom() {
        return yearsWorkedFrom;
    }

    public void setYearsWorkedFrom(Integer yearsWorkedFrom) {
        this.yearsWorkedFrom = yearsWorkedFrom;
    }

    public Integer getYearsWorkedTo() {
        return yearsWorkedTo;
    }

    public void setYearsWorkedTo(Integer yearsWorkedTo) {
        this.yearsWorkedTo = yearsWorkedTo;
    }
}
