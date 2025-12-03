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

    @Override
    public String toString() {
        return "Candidate {" +
                "\n  Politician: " + politician.getPoliticianName() +
                "\n  Party for Election: " + electionParty +
                "\n  Votes: " + votes +
                "\n}";
    }
}