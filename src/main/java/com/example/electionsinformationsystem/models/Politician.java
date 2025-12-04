package com.example.electionsinformationsystem.models;

public class Politician {

    // fields
    String politicianName;
    String dateOfBirth;
    String politicalParty; // should default to 'Independent' if empty, in dialog just make "if null, set Independant"
    String homeCounty;
    String photoUrl;
    private Election[] electionRecord;

    // constructor
    public Politician(String politicianName, String dateOfBirth, String politicalParty, String homeCounty, String photoUrl) {
        this.politicianName = politicianName;
        this.dateOfBirth = dateOfBirth;
        this.politicalParty = politicalParty;
        this.homeCounty = homeCounty;
        this.photoUrl = photoUrl;
    }

    // getters and setters
    public String getPoliticianName() {
        return politicianName;
    }

    public void setPoliticianName(String politicianName) {
        this.politicianName = politicianName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(String politicalParty) {
        this.politicalParty = politicalParty;
    }

    public String getHomeCounty() {
        return homeCounty;
    }

    public void setHomeCounty(String homeCounty) {
        this.homeCounty = homeCounty;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "Politician: " + politicianName
                + " | DoB: " + dateOfBirth
                + " | Party: " + politicalParty
                + " | County: " + homeCounty
                + " | Photo: " + photoUrl;
    }
}
