package pl.kurs.finaltest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentDto extends PersonDto{

    @NotBlank(message = "Nazwa uniwersytetu wymagana.")
    private String universityName;
    @NotNull(message = "Rok studiów wymagany")
    private Integer yearOfStudy;
    @NotBlank(message = "Kierunek studiów wymagany")
    private String fieldOfStudy;
    @NotNull(message = "Wysokość stypendium wymagana")
    private Double scholarshipAmount;



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

    public Double getScholarshipAmount() {
        return scholarshipAmount;
    }

    public void setScholarshipAmount(Double scholarshipAmount) {
        this.scholarshipAmount = scholarshipAmount;
    }

    @Override
    public String toString() {
        return "StudentDto{" +
                "universityName='" + universityName + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", scholarshipAmount=" + scholarshipAmount +
                '}';
    }

    @Override
    public String getTypeIdentifier() {
        return "student";
    }
}
