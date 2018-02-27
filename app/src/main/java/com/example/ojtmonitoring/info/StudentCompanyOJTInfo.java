package com.example.ojtmonitoring.info;


public class StudentCompanyOJTInfo {

    private CompanyInfo companyInfo;
    private ResumeInfo resumeInfo;
    private int selected;

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public ResumeInfo getResumeInfo() {
        return resumeInfo;
    }

    public void setResumeInfo(ResumeInfo resumeInfo) {
        this.resumeInfo = resumeInfo;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
