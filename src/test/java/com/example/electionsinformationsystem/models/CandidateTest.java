package com.example.electionsinformationsystem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CandidateTest {

    private Politician politician1;
    private Politician politician2;
    private Candidate candidate;


    @BeforeEach
    void setup() {
        politician1 = new Politician("Ronald Stump", "2025-1-1", "Fianna Fáil", "Waterford", "stump.png");
        politician2 = new Politician("Boris O’Bama", "12/12/1980", "Fine Gael", "Cork", "obama.png");
        candidate = new Candidate(politician1, "Fianna Fáil", 1000);
    }

    @Test
    void testConstructor() {
        assertEquals(politician1, candidate.getPolitician());
        assertEquals("Fianna Fáil", candidate.getElectionParty());
        assertEquals(1000, candidate.getVotes());
    }

    @Test
    void testSetPolitician() {
        candidate.setPolitician(politician2);
        assertEquals(politician2, candidate.getPolitician());
    }

    @Test
    void testSetElectionParty() {
        candidate.setElectionParty("Sinn Féin");
        assertEquals("Sinn Féin", candidate.getElectionParty());
    }

    @Test
    void testSetVotes() {
        candidate.setVotes(2000);
        assertEquals(2000, candidate.getVotes());
    }

    @Test
    void testToString() {
        String expected =
                "Candidate {" +
                        "\n  Politician: " + politician1.getPoliticianName() +
                        "\n  Party for Election: " + candidate.getElectionParty() +
                        "\n  Votes: " + candidate.getVotes() +
                        "\n}";
        assertEquals(expected, candidate.toString());
    }
}