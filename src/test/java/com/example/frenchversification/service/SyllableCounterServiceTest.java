package com.example.frenchversification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyllableCounterServiceTest {

    private SyllableCounterService syllableCounterService;

    @BeforeEach
    public void setUp() {
        syllableCounterService = new SyllableCounterService();
    }

    @Test
    public void testSimpleLine() {
        // bon-jour (2) le (1) mon-de (1, final e is mute). Total = 4.
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Bonjour le monde"));
        assertEquals(4, counts.get(0));
    }

    @Test
    public void testAlexandrin() {
        // "Que ces vains ornements, que ces voiles me pèsent" - Phèdre, Racine
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Que ces vains ornements, que ces voiles me pèsent"));
        assertEquals(12, counts.get(0), "Should be 12 syllables for the alexandrin");
    }

    @Test
    public void testElision() {
        // "une amie" -> u-n'a-mie -> 3 syllables
        List<Integer> counts = syllableCounterService.countSyllables(List.of("une amie"));
        assertEquals(3, counts.get(0), "Should handle elision correctly");
    }

    @Test
    public void testECaducBeforeConsonant() {
        // "une table" -> u-ne (2) + ta-ble (1) -> 3
        List<Integer> counts = syllableCounterService.countSyllables(List.of("une table"));
        assertEquals(3, counts.get(0), "Should handle 'e' before consonant");
    }

    @Test
    public void testECaducAtEndOfLine() {
        // "Une belle dame" -> U-ne (2) bel-le (2) da-me (1) -> 5
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Une belle dame"));
         assertEquals(5, counts.get(0), "Should handle mute 'e' at the end of a line");
    }

    @Test
    public void testMultipleLines() {
        List<String> poem = List.of(
            "Ma seule étoile est morte, - et mon luth constellé", // 12
            "Porte le Soleil noir de la Mélancolie." // 12
        );
        List<Integer> counts = syllableCounterService.countSyllables(poem);
        assertEquals(12, counts.get(0));
        assertEquals(12, counts.get(1));
    }

    @Test
    public void testEmptyAndNullLines() {
        List<String> poem = new ArrayList<>();
        poem.add("");
        poem.add(null);
        poem.add("   ");

        List<Integer> counts = syllableCounterService.countSyllables(poem);
        assertEquals(0, counts.get(0));
        assertEquals(0, counts.get(1));
        assertEquals(0, counts.get(2));
    }
}
