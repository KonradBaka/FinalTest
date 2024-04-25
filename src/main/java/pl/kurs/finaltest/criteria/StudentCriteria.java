package pl.kurs.finaltest.criteria;

public class StudentCriteria extends PersonCriteria {

    private String universityName;
    private Integer yearOfStudy;
    private String fieldOfStudy;
    private Double scholarshipFrom;
    private Double scholarshipTo;

    public StudentCriteria() {
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public void setScholarshipFrom(Double scholarshipFrom) {
        this.scholarshipFrom = scholarshipFrom;
    }

    public void setScholarshipTo(Double scholarshipTo) {
        this.scholarshipTo = scholarshipTo;
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
