package pl.kurs.finaltest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.adnotations.PersonSubType;

@PersonSubType("student")
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


}
