package pl.kurs.finaltest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "students")
public class Student extends Person{
    private static final long serialVersionUID = 1L;

    @Column(name = "university_name")
    private String universityName;
    @Column(name = "year_of_study")
    private Integer yearOfStudy;
    @Column(name = "field_of_study")
    private String fieldOfStudy;
    @Column(name = "scholarship_amount")
    private Double scholarshipAmount;

    public Student() {
    }

    public Student(String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress, String universityName, Integer yearOfStudy, String fieldOfStudy, Double scholarshipAmount) {
        super(type, firstName, lastName, pesel, height, weight, emailAddress);
        this.universityName = universityName;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarshipAmount = scholarshipAmount;
    }

    public Student(Long id, String type, String firstName, String lastName, String pesel, Double height, Double weight, String emailAddress, String universityName, Integer yearOfStudy, String fieldOfStudy, Double scholarshipAmount) {
        super(id, type, firstName, lastName, pesel, height, weight, emailAddress);
        this.universityName = universityName;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarshipAmount = scholarshipAmount;
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

    public void setFieldOfStudy(String majorOfStudy) {
        this.fieldOfStudy = majorOfStudy;
    }

    public Double getScholarshipAmount() {
        return scholarshipAmount;
    }

    public void setScholarshipAmount(Double scholarshipAmount) {
        this.scholarshipAmount = scholarshipAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(universityName, student.universityName) && Objects.equals(yearOfStudy, student.yearOfStudy) && Objects.equals(fieldOfStudy, student.fieldOfStudy) && Objects.equals(scholarshipAmount, student.scholarshipAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), universityName, yearOfStudy, fieldOfStudy, scholarshipAmount);
    }
}
