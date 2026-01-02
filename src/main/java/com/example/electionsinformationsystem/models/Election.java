package com.example.electionsinformationsystem.models;

public class Election {

    // fields
    private String electionType;
    private String location;
    private String electionDate;
    private int numWinners;
    private final LinkedList<Candidate> candidates = new LinkedList<>(); // stores each candidate in an election

    // constructor
    public Election(String electionType, String location, String electionDate, int numWinners) {
        this.electionType = electionType;
        this.location = location;
        this.electionDate = electionDate;
        this.numWinners = numWinners;
    }

    // getters and setters
    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getElectionDate() {
        return electionDate;
    }

    public void setElectionDate(String electionDate) {
        this.electionDate = electionDate;
    }

    public int getNumWinners() {
        return numWinners;
    }

    public void setNumWinners(int numWinners) {
        this.numWinners = numWinners;
    }

    public LinkedList<Candidate> getCandidates() {
        return candidates;
    }

    // methods
    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    public void removeCandidate(Candidate candidate) {
        candidates.remove(candidate);
    }


    // toString
    @Override
    public String toString() {
        if (electionType.equalsIgnoreCase("Presidential")) location = "Nationwide";

        return electionType + " Election (" + location + ") - " + electionDate + " | Winners: " + numWinners;
    }
}
