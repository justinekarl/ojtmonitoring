package com.example.ojtmonitoring.info;

public class ResumeInfo {
    private int id;
    private StudentPersonalInformationInfo studentPersonalInformationInfo;
    private AccomplishmentsInterestInfo accomplishmentsInterestInfo;
    private boolean approved;
    private double ojtHoursNeeded;
    private int updatedByTeacherId;
    private String teacherNotes;
    private boolean companyAccepted;
    private String college;

    //used in student company ojt info
    private int selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StudentPersonalInformationInfo getStudentPersonalInformationInfo() {
        return studentPersonalInformationInfo;
    }

    public void setStudentPersonalInformationInfo(StudentPersonalInformationInfo studentPersonalInformationInfo) {
        this.studentPersonalInformationInfo = studentPersonalInformationInfo;
    }

    public AccomplishmentsInterestInfo getAccomplishmentsInterestInfo() {
        return accomplishmentsInterestInfo;
    }

    public void setAccomplishmentsInterestInfo(AccomplishmentsInterestInfo accomplishmentsInterestInfo) {
        this.accomplishmentsInterestInfo = accomplishmentsInterestInfo;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public double getOjtHoursNeeded() {
        return ojtHoursNeeded;
    }

    public void setOjtHoursNeeded(double ojtHoursNeeded) {
        this.ojtHoursNeeded = ojtHoursNeeded;
    }

    public int getUpdatedByTeacherId() {
        return updatedByTeacherId;
    }

    public void setUpdatedByTeacherId(int updatedByTeacherId) {
        this.updatedByTeacherId = updatedByTeacherId;
    }

    public String getTeacherNotes() {
        return teacherNotes;
    }

    public void setTeacherNotes(String teacherNotes) {
        this.teacherNotes = teacherNotes;
    }

    public boolean isCompanyAccepted() {
        return companyAccepted;
    }

    public void setCompanyAccepted(boolean companyAccepted) {
        this.companyAccepted = companyAccepted;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
