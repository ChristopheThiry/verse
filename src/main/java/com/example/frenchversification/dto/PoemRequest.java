package com.example.frenchversification.dto;

import java.util.List;

public class PoemRequest {

    private List<String> lines;

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
