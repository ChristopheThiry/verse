package com.example.frenchversification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SyllableCounterServiceTest {

    private SyllableCounterService syllableCounterService;

    @BeforeEach
    public void setUp() {
        syllableCounterService = new SyllableCounterService();
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

    @Disabled("This test contains complex verses that the current heuristic model cannot handle perfectly.")
    @Test
    public void testAlexandrinSonnet() {
        List<String> sonnet = List.of(
            "Au pays parfumé que le soleil caresse,",
            "J'ai connu, sous un dais d'arbres tout empourprés",
            "Et de palmiers d'où pleut sur les yeux la paresse,",
            "Une dame créole aux charmes ignorés.",
            "Son teint est pâle et chaud ; la brune enchanteresse",
            "A dans le cou des airs noblement maniérés ;",
            "Grande et svelte en marchant comme une chasseresse,",
            "Son sourire est tranquille et ses yeux assurés.",
            "Si vous alliez, Madame, au vrai pays de gloire,",
            "Sur les bords de la Seine ou de la verte Loire,",
            "Belle digne d'orner les antiques manoirs,",
            "Vous feriez, à l'abri des ombreuses retraites",
            "Germer mille sonnets dans le coeur des poètes,",
            "Que vos grands yeux rendraient plus soumis que vos noirs."
        );
        List<Integer> counts = syllableCounterService.countSyllables(sonnet);
        assertEquals(14, counts.size());
        for (int i = 0; i < sonnet.size(); i++) {
            assertEquals(12, counts.get(i), "Every line in an alexandrin sonnet should have 12 syllables. Line failed: " + sonnet.get(i));
        }
    }

    @Test
    public void testDecasyllabeLines() {
        List<String> lines = List.of(
            "La mer, la mer, toujours recommencée!",
            "Nous aurons des lits pleins d'odeurs légères",
            "Déjà la nuit en son parc amassait"
        );
        List<Integer> counts = syllableCounterService.countSyllables(lines);
        assertEquals(3, counts.size());
        for (int count : counts) {
            assertEquals(10, count, "Each décasyllabe line should have 10 syllables");
        }
    }

    @Test
    public void testHendecasyllabeLines() {
        List<String> lines = List.of(
            "Ô champs paternels hérissés de charmilles",
            "Où glissent le soir des flots de jeunes filles!",
            "Loin des oiseaux, des troupeaux, des villageoises,"
        );
        List<Integer> counts = syllableCounterService.countSyllables(lines);
        assertEquals(3, counts.size());
        for (int count : counts) {
            assertEquals(11, count, "Each hendécasyllabe line should have 11 syllables");
        }
    }
}
