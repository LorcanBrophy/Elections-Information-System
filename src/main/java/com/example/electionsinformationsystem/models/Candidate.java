package com.example.electionsinformationsystem.models;

public class Candidate {

    // fields
    private Politician politician;
    private String electionParty;
    private int votes;

    // constructor
    public Candidate(Politician politician, String electionParty, int votes) {
        this.politician = politician;
        this.electionParty = electionParty;
        this.votes = votes;
    }

    // getters and setters
    public Politician getPolitician() {
        return politician;
    }

    public void setPolitician(Politician politician) {
        this.politician = politician;
    }

    public String getElectionParty() {
        return electionParty;
    }

    public void setElectionParty(String electionParty) {
        this.electionParty = electionParty;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    // used for table view
    public String getPoliticianName() {
        return politician.getPoliticianName();
    }

    @Override
    public String toString() {
        return "Candidate: " + politician.getPoliticianName()
                + " | Party for Election: " + electionParty
                + " | Votes: " + votes;
    }
}