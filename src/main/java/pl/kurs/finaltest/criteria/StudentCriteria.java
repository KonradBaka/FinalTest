package pl.kurs.finaltest.criteria;

public class StudentCriteria extends PersonCriteria {

    private String universityName;
    private Integer yearOfStudy;
    private String fieldOfStudy;
    private Double scholarshipFrom;
    private Double scholarshipTo;

    public StudentCriteria() {
    }

    public String getUniversityName() {
        return universityName;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public Double getScholarshipFrom() {
        return scholarshipFrom;
    }

    public Double getScholarshipTo() {
        return scholarshipTo;
    }

    @Override
    public String getType() {
        return "student";
    }
}
