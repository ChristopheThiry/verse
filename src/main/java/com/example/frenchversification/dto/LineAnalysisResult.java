package com.example.frenchversification.dto;

public class LineAnalysisResult {

    private int lineNumber;
    private int syllableCount;
    private String footsDecomposition;

    public LineAnalysisResult(int lineNumber, int syllableCount, String footsDecomposition) {
        this.lineNumber = lineNumber;
        this.syllableCount = syllableCount;
        this.footsDecomposition = footsDecomposition;
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

    public String getFootsDecomposition() {
        return footsDecomposition;
    }

    public void setFootsDecomposition(String footsDecomposition) {
        this.footsDecomposition = footsDecomposition;
    }
}
