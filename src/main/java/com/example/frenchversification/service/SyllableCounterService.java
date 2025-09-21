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

    private static final Set<String> CONSONANT_CLUSTERS = Set.of("bl", "br", "ch", "cl", "cr", "dr", "fl", "fr", "gl", "gr", "ph", "pl", "pr", "sh", "th", "tr", "vr", "gn");

    public SyllableCountResponse analyzeLines(List<String> lines) {
        if (lines == null) {
            return new SyllableCountResponse(new ArrayList<>());
        }

        List<LineAnalysisResult> results = IntStream.range(0, lines.size())
                .mapToObj(i -> {
                    String line = lines.get(i);
                    int syllableCount = countSyllablesInLine(line);
                    String syllableDecomposition = generateSyllableDecomposition(line);
                    return new LineAnalysisResult(i + 1, syllableCount, syllableDecomposition);
                })
                .collect(Collectors.toList());

        return new SyllableCountResponse(results);
    }

    private String generateSyllableDecomposition(String line) {
        if (line == null || line.isBlank()) {
            return "";
        }
        String text = line.toLowerCase().trim();
        text = text.replaceAll("[\\p{Punct}&&[^']]+", " ");
        text = text.replaceAll("\\s+", " ").trim();

        if (text.isEmpty()) {
            return "";
        }

        String[] words = text.split("\\s+");
        List<String> resultSyllables = new ArrayList<>();
        for (String word : words) {
            resultSyllables.add(decomposeWord(word));
        }
        return String.join("-", resultSyllables);
    }

    private String decomposeWord(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        List<Integer> vowelIndices = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i))) {
                if (vowelIndices.isEmpty() || i > vowelIndices.get(vowelIndices.size() - 1) + 1) {
                    vowelIndices.add(i);
                }
            }
        }

        if (vowelIndices.size() <= 1) {
            return word;
        }

        List<String> syllables = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < vowelIndices.size() - 1; i++) {
            int v1 = vowelIndices.get(i);
            int v2 = vowelIndices.get(i + 1);
            int consonantsBetween = v2 - v1 - 1;

            int splitIndex;
            if (consonantsBetween == 1) {
                splitIndex = v1 + 1; // V-CV
            } else if (consonantsBetween == 2) {
                String cluster = word.substring(v1 + 1, v1 + 3);
                if (CONSONANT_CLUSTERS.contains(cluster)) {
                    splitIndex = v1 + 1; // V-CCV
                } else {
                    splitIndex = v1 + 2; // VC-CV
                }
            } else if (consonantsBetween == 3) {
                splitIndex = v1 + 2; // VC-CCV
            } else {
                splitIndex = v1 + (consonantsBetween / 2) + 1;
            }

            syllables.add(word.substring(start, splitIndex));
            start = splitIndex;
        }
        syllables.add(word.substring(start));

        return String.join("-", syllables);
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
