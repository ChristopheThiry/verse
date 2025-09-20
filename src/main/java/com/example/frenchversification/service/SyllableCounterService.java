package com.example.frenchversification.service;

import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyllableCounterService {

    private static final String VOWELS = "aeiouy";

    public List<Integer> countSyllables(List<String> lines) {
        if (lines == null) {
            return new ArrayList<>();
        }
        return lines.stream()
                .map(this::countSyllablesInLine)
                .collect(Collectors.toList());
    }

    private int countSyllablesInLine(String line) {
        if (line == null || line.isBlank()) {
            return 0;
        }

        String cleanedLine = line.toLowerCase().trim();
        cleanedLine = cleanedLine.replaceAll("[\\p{Punct}&&[^']]+", " ");
        cleanedLine = cleanedLine.replaceAll("\\s+", " ").trim();

        if (cleanedLine.isEmpty()) {
            return 0;
        }

        String[] words = cleanedLine.split("\\s+");
        int totalSyllables = 0;

        for (String word : words) {
            totalSyllables += countVowelGroups(normalizeString(word));
        }

        // Rule for mute 'e' at the end of the line
        if (words.length > 0) {
            String lastWord = normalizeString(words[words.length - 1]);
            if (lastWord.length() > 2 && (lastWord.endsWith("e") || lastWord.endsWith("es"))) {
                char charBeforeE = lastWord.charAt(lastWord.length() - (lastWord.endsWith("es") ? 3 : 2));
                if (!isVowel(charBeforeE)) {
                    totalSyllables--;
                }
            }
            if (lastWord.length() > 3 && lastWord.endsWith("ent")) {
                totalSyllables--;
            }
        }

        return totalSyllables;
    }

    private int countVowelGroups(String normalizedWord) {
        if (normalizedWord == null || normalizedWord.isEmpty()) return 0;
        int count = 0;
        boolean lastWasVowel = false;
        for (char c : normalizedWord.toCharArray()) {
            if (isVowel(c)) {
                if (!lastWasVowel) {
                    count++;
                }
                lastWasVowel = true;
            } else {
                lastWasVowel = false;
            }
        }
        return count; // Don't assume 1, let it be 0 if no vowels.
    }

    private String normalizeString(String s) {
        if (s == null) return "";
        s = s.toLowerCase();
        if (s.startsWith("'")) {
            s = s.substring(1);
        }
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                         .replaceAll("\\p{M}", "");
    }

    private boolean isVowel(char c) {
        return VOWELS.indexOf(c) >= 0;
    }
}
