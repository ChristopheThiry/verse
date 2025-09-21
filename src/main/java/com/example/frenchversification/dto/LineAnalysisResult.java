package com.example.frenchversification.dto;

public class LineAnalysisResult {

    private int lineNumber;
    private int syllableCount;
    private String syllableDecomposition;

    public LineAnalysisResult(int lineNumber, int syllableCount, String syllableDecomposition) {
        this.lineNumber = lineNumber;
        this.syllableCount = syllableCount;
        this.syllableDecomposition = syllableDecomposition;
    }

    // Getters and setters
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getSyllableCount() {
        return syllableCount;
    }

    public void setSyllableCount(int syllableCount) {
        this.syllableCount = syllableCount;
    }

    public String getSyllableDecomposition() {
        return syllableDecomposition;
    }

    public void setSyllableDecomposition(String syllableDecomposition) {
        this.syllableDecomposition = syllableDecomposition;
    }
}
