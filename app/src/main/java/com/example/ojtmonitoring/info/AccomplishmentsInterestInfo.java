package com.example.ojtmonitoring.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccomplishmentsInterestInfo {

    private int id;
    private String accomplishments;
    private String interests;
    private List<EducationalBackgroundInfo> educationalBackgrounds = new ArrayList<EducationalBackgroundInfo>();
    private List<ReferencesInfo> referencesInfos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccomplishments() {
        return accomplishments;
    }

    public void setAccomplishments(String accomplishments) {
        this.accomplishments = accomplishments;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public List<EducationalBackgroundInfo> getEducationalBackgrounds() {
        return educationalBackgrounds;
    }

    public void setEducationalBackgrounds(List<EducationalBackgroundInfo> educationalBackgrounds) {
        this.educationalBackgrounds = educationalBackgrounds;
    }

    public List<ReferencesInfo> getReferencesInfos() {
        return referencesInfos;
    }

    public void setReferencesInfos(List<ReferencesInfo> referencesInfos) {
        this.referencesInfos = referencesInfos;
    }
}
