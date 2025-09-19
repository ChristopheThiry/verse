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

    @Test
    public void testHugoAlexandrin() {
        // "Le soir tombait; la lutte était ardente et noire." - Victor Hugo
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Le soir tombait; la lutte était ardente et noire."));
        assertEquals(12, counts.get(0), "Hugo's alexandrin should be 12 syllables");
    }

    @Test
    public void testCorneilleAlexandrin() {
        // "Nous partîmes cinq cents; mais par un prompt renfort" - Corneille
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Nous partîmes cinq cents; mais par un prompt renfort"));
        assertEquals(12, counts.get(0), "Corneille's alexandrin should be 12 syllables");
    }

    @Test
    public void testRacineAlexandrin() {
        // "Il pense voir en pleurs dissiper cet orage" - Racine
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Il pense voir en pleurs dissiper cet orage"));
        assertEquals(12, counts.get(0), "Racine's alexandrin should be 12 syllables");
    }

    // Décasyllabe Tests (10 syllables)
    @Test
    public void testValeryDecasyllabe() {
        List<Integer> counts = syllableCounterService.countSyllables(List.of("La mer, la mer, toujours recommencée!"));
        assertEquals(10, counts.get(0), "Valéry's décasyllabe should be 10 syllables");
    }

    @Test
    public void testBaudelaireDecasyllabe() {
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Nous aurons des lits pleins d'odeurs légères"));
        assertEquals(10, counts.get(0), "Baudelaire's décasyllabe should be 10 syllables");
    }

    @Test
    public void testDuBellayDecasyllabe() {
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Déjà la nuit en son parc amassait"));
        assertEquals(10, counts.get(0), "Du Bellay's décasyllabe should be 10 syllables");
    }

    // Hendécasyllabe Tests (11 syllables)
    @Test
    public void testDesbordesValmoreHendecasyllabe1() {
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Ô champs paternels hérissés de charmilles"));
        assertEquals(11, counts.get(0), "Desbordes-Valmore's hendécasyllabe should be 11 syllables");
    }

    @Test
    public void testDesbordesValmoreHendecasyllabe2() {
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Où glissent le soir des flots de jeunes filles!"));
        assertEquals(11, counts.get(0), "Desbordes-Valmore's hendécasyllabe should be 11 syllables");
    }

    @Test
    public void testRimbaudHendecasyllabe() {
        List<Integer> counts = syllableCounterService.countSyllables(List.of("Loin des oiseaux, des troupeaux, des villageoises,"));
        assertEquals(11, counts.get(0), "Rimbaud's hendécasyllabe should be 11 syllables");
    }
}
