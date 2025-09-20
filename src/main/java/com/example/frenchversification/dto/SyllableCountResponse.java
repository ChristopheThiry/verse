package com.example.frenchversification.dto;

import java.util.List;

public class SyllableCountResponse {

    private List<LineAnalysisResult> results;

    public SyllableCountResponse(List<LineAnalysisResult> results) {
        this.results = results;
    }

    // Getters and setters
    public List<LineAnalysisResult> getResults() {
        return results;
    }

    public void setResults(List<LineAnalysisResult> results) {
        this.results = results;
    }
}
