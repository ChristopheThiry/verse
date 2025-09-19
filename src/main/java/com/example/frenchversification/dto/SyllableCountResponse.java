package com.example.frenchversification.dto;

import java.util.List;

public class SyllableCountResponse {

    private List<Integer> syllable_counts;

    public SyllableCountResponse(List<Integer> syllable_counts) {
        this.syllable_counts = syllable_counts;
    }

    public List<Integer> getSyllable_counts() {
        return syllable_counts;
    }

    public void setSyllable_counts(List<Integer> syllable_counts) {
        this.syllable_counts = syllable_counts;
    }
}
