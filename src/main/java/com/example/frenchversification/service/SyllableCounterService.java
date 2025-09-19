package com.example.frenchversification.service;

import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SyllableCounterService {

    private static final String VOWELS = "aeiouy";
    private static final Set<String> H_ASPIRE_WORDS = Set.of("haut", "heros", "hache", "haie", "haine", "harpe", "hasard", "honte", "hussard", "hollande", "handicap", "hibou", "herisson");

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

        for (int i = 0; i < words.length; i++) {
            String currentWord = words[i];
            String nextWord = (i < words.length - 1) ? words[i+1] : null;
            totalSyllables += countSyllables(currentWord, nextWord);
        }

        return totalSyllables;
    }

    private int countSyllables(String word, String nextWord) {
        String originalWord = word.toLowerCase();
        if (originalWord.startsWith("'")) {
            originalWord = originalWord.substring(1);
        }

        String normalized = normalizeString(originalWord);
        if (normalized.isEmpty()) {
            return 0;
        }

        int syllableCount = countVowelGroups(normalized);

        boolean isPronouncedDueToLiaison = (nextWord != null && !startsWithVowelOrHmute(nextWord));

        // Mute 'e' and 'es' rule
        if (normalized.length() > 2 && normalized.endsWith("es")) {
            if (!isPronouncedDueToLiaison) {
                char charBeforeE = normalized.charAt(normalized.length() - 3);
                if (!isVowel(charBeforeE)) {
                    syllableCount--;
                }
            }
        } else if (normalized.length() > 1 && normalized.endsWith("e")) {
            boolean isAccentAigu = originalWord.endsWith("é") || originalWord.endsWith("ée");
            if (!isPronouncedDueToLiaison && !isAccentAigu) {
                char charBeforeE = normalized.charAt(normalized.length() - 2);
                if (!isVowel(charBeforeE)) {
                    syllableCount--;
                }
            }
        }

        // Mute 'ent' for verbs at the end of a line/elision
        if (normalized.length() > 3 && normalized.endsWith("ent")) {
             if(!isPronouncedDueToLiaison) {
                 syllableCount--;
             }
        }

        // Diérèse/Synérèse very simplified heuristic
        if(originalWord.contains("tion") || originalWord.contains("sion") || originalWord.contains("cien")) {
            syllableCount++;
        }

        if (syllableCount == 0 && !normalized.isEmpty()) {
            return 1;
        }

        return syllableCount;
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
        return count;
    }

    private String normalizeString(String s) {
        if (s == null) return "";
        s = s.toLowerCase();
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                         .replaceAll("\\p{M}", "");
    }

    private boolean isVowel(char c) {
        return VOWELS.indexOf(c) >= 0;
    }

    private boolean startsWithVowelOrHmute(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        String normalized = normalizeString(word);
        if (normalized.isEmpty()) return false;

        if (word.startsWith("'")) {
            return true;
        }

        char firstChar = normalized.charAt(0);

        if (isVowel(firstChar)) return true;

        if (firstChar == 'h') {
            return !H_ASPIRE_WORDS.contains(normalized);
        }
        return false;
    }
}
