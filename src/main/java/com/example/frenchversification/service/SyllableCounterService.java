package com.example.frenchversification.service;

import com.example.frenchversification.dto.LineAnalysisResult;
import com.example.frenchversification.dto.SyllableCountResponse;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SyllableCounterService {

    private static final String VOWELS = "aeiouy";
    private static final Set<String> H_ASPIRE_WORDS = Set.of("haut", "heros", "hache", "haie", "haine", "harpe", "hasard", "honte", "hussard", "hollande", "handicap", "hibou", "herisson");

    public SyllableCountResponse analyzeLines(List<String> lines) {
        if (lines == null) {
            return new SyllableCountResponse(new ArrayList<>());
        }

        List<LineAnalysisResult> results = IntStream.range(0, lines.size())
                .mapToObj(i -> {
                    String line = lines.get(i);
                    int syllableCount = countSyllablesInLine(line);
                    String footsDecomposition = generateFootsDecomposition(syllableCount);
                    return new LineAnalysisResult(i + 1, syllableCount, footsDecomposition);
                })
                .collect(Collectors.toList());

        return new SyllableCountResponse(results);
    }

    private String generateFootsDecomposition(int syllableCount) {
        if (syllableCount <= 0) {
            return "";
        }
        List<String> feet = new ArrayList<>();
        int remainingSyllables = syllableCount;
        while (remainingSyllables > 0) {
            if (remainingSyllables >= 2) {
                feet.add("2");
                remainingSyllables -= 2;
            } else {
                feet.add("1");
                remainingSyllables -= 1;
            }
        }
        return String.join(" ", feet);
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

        // Mute 'e' rule
        if (normalized.length() > 1 && (normalized.endsWith("e") || normalized.endsWith("es"))) {
            boolean isPronounced = (nextWord != null && !startsWithVowelOrHmute(nextWord));
            boolean isAccentAigu = originalWord.endsWith("é") || originalWord.endsWith("ée");

            if (!isPronounced && !isAccentAigu) {
                char charBeforeE = normalized.charAt(normalized.length() - (normalized.endsWith("es") ? 3 : 2));
                if (!isVowel(charBeforeE)) {
                    syllableCount--;
                }
            }
        }

        if (normalized.length() > 3 && normalized.endsWith("ent")) {
             boolean isPronounced = (nextWord != null && !startsWithVowelOrHmute(nextWord));
             if(!isPronounced) {
                 syllableCount--;
             }
        }

        // Diérèse/Synérèse very simplified heuristic
        if(originalWord.contains("tion") || originalWord.contains("sion") || originalWord.contains("cien")) {
            syllableCount++;
        }

        if (syllableCount <= 0 && !normalized.isEmpty()) {
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
                if (c == 'y' && lastWasVowel) {
                    // 'y' after a vowel is usually part of the same sound (e.g. "pays")
                } else if (!lastWasVowel) {
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
