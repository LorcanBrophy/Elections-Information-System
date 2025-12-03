package com.example.electionsinformationsystem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ElectionTest {

    private Election election;
    private Candidate candidate1;
    private Candidate candidate2;

    @BeforeEach
    void setup() {
        election = new Election("General", "Dublin", "2025-01-01", 5);
        candidate1 = new Candidate(new Politician("Ronald Stump", "1/1/1032", "Fianna Fáil", "Waterford", "stump.png"), "Fianna Fáil", 1000);
        candidate2 = new Candidate(new Politician("Boris O’Bama", "12/12/1980", "Fine Gael", "Cork", "obama.png"), "Fine Gael", 750);
    }

    @Test
    void testConstructor() {
        assertEquals("General", election.getElectionType());
        assertEquals("Dublin", election.getLocation());
        assertEquals("2025-01-01", election.getElectionDate());
        assertEquals(5, election.getNumWinners());
    }

    @Test
    void testSetElectionType() {
        election.setElectionType("Local");
        assertEquals("Local", election.getElectionType());
    }

    @Test
    void testSetLocation() {
        election.setLocation("Cork");
        assertEquals("Cork", election.getLocation());
    }

    @Test
    void testSetElectionDate() {
        election.setElectionDate("2030-01-01");
        assertEquals("2030-01-01", election.getElectionDate());
    }

    @Test
    void testSetNumWinners() {
        election.setNumWinners(3);
        assertEquals(3, election.getNumWinners());
    }

    @Test
    void testAddCandidate() {
        election.addCandidate(candidate1);
        election.addCandidate(candidate2);
        assertEquals(2, election.getCandidates().size());
    }

    @Test
    void testRemoveCandidate() {
        election.addCandidate(candidate1);
        election.addCandidate(candidate2);
        election.removeCandidate(candidate1);
        assertEquals(1, election.getCandidates().size());
    }

    @Test
    void testToString() {
        String expected =
                "Election {" +
                        "\n  Type: " + election.getElectionType() +
                        "\n  Location: " + election.getLocation() +
                        "\n  Date: " + election.getElectionDate() +
                        "\n  Number of Winners: " + election.getNumWinners() +
                        "\n}";

        assertEquals(expected, election.toString());
    }
}