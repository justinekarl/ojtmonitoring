package com.example.ojtmonitoring.info;


public class WorkExperienceInfo{

    private int id;
    private String companyName;
    private String address;
    private String jobDescription;
    private String dutiesResponsibilities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getDutiesResponsibilities() {
        return dutiesResponsibilities;
    }

    public void setDutiesResponsibilities(String dutiesResponsibilities) {
        this.dutiesResponsibilities = dutiesResponsibilities;
    }
}
