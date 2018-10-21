package com.example.ojtmonitoring.info;


public class CompanyInfo {

    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String emailAddress;
    private String description;
    private int selected;
    private boolean alreadySentResume;
    private int rating;


    public CompanyInfo(){
        super();
    }

    public CompanyInfo(int id,String companyName,String address,String phoneNumber,String emailAddress,String description){
        this.id = id;
        this.name = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String companyName) {
        this.name = companyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public boolean isAlreadySentResume() {
        return alreadySentResume;
    }

    public void setAlreadySentResume(boolean alreadySentResume) {
        this.alreadySentResume = alreadySentResume;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
