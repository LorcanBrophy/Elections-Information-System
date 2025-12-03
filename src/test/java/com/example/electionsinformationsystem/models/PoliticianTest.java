package com.example.electionsinformationsystem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PoliticianTest {

    private Politician politician;

    @BeforeEach
    void setup() {
        politician = new Politician("Ronald Stump", "2025-1-1", "Fianna Fáil", "Waterford", "stump.png");
    }

    @Test
    void testConstructor() {
        assertEquals("Ronald Stump", politician.getPoliticianName());
        assertEquals("2025-1-1", politician.getDateOfBirth());
        assertEquals("Fianna Fáil", politician.getPoliticalParty());
        assertEquals("Waterford", politician.getHomeCounty());
        assertEquals("stump.png", politician.getPhotoUrl());
    }

    @Test
    void testSetPoliticianName() {
        politician.setPoliticianName("Boris O’Bama");
        assertEquals("Boris O’Bama", politician.getPoliticianName());
    }

    @Test
    void testSetDateOfBirth() {
        politician.setDateOfBirth("2000/12/12");
        assertEquals("2000/12/12", politician.getDateOfBirth());
    }

    @Test
    void testSetPoliticalParty() {
        politician.setPoliticalParty("Fine Gael");
        assertEquals("Fine Gael", politician.getPoliticalParty());
    }

    @Test
    void testSetHomeCounty() {
        politician.setHomeCounty("Cork");
        assertEquals("Cork", politician.getHomeCounty());
    }

    @Test
    void testSetPhotoUrl() {
        politician.setPhotoUrl("new.png");
        assertEquals("new.png", politician.getPhotoUrl());
    }

    @Test
    void testToString() {
        String expected =
                "Politician {" +
                        "\n  Name: " + politician.getPoliticianName() +
                        "\n  DoB: " + politician.getDateOfBirth() +
                        "\n  Party: " + politician.getPoliticalParty() +
                        "\n  County: " + politician.getHomeCounty() +
                        "\n  Photo URL: " + politician.getPhotoUrl() +
                        "\n}";

        assertEquals(expected, politician.toString());
    }
}