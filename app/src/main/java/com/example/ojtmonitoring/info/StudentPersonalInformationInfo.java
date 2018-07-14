package com.example.ojtmonitoring.info;


import java.util.ArrayList;
import java.util.List;

public class StudentPersonalInformationInfo{

    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String gender;
    private List<WorkExperienceInfo> workExperienceInfos = new ArrayList<WorkExperienceInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<WorkExperienceInfo> getWorkExperienceInfos() {
        return workExperienceInfos;
    }

    public void setWorkExperienceInfos(List<WorkExperienceInfo> workExperienceInfos) {
        this.workExperienceInfos = workExperienceInfos;
    }
}
