package com.example.electionsinformationsystem.models;

public class Politician {

    // fields
    private String politicianName;
    private String dateOfBirth;
    private String politicalParty;
    private String homeCounty;
    private String photoUrl;
    private final LinkedList<Election> electionRecord = new LinkedList<>();

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

    public LinkedList<Election> getElectionRecord() {
        return electionRecord;
    }

    public void addElection(Election election) {
        boolean alreadyExists = false;

        for (Election e : electionRecord) {
            if (e.equals(election)) {
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) electionRecord.add(election);
    }

    public void removeElection(Election election) {
        electionRecord.remove(election);
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
