package com.example.frenchversification.controller;

import com.example.frenchversification.dto.PoemRequest;
import com.example.frenchversification.dto.SyllableCountResponse;
import com.example.frenchversification.service.SyllableCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PoemController {

    private final SyllableCounterService syllableCounterService;

    @Autowired
    public PoemController(SyllableCounterService syllableCounterService) {
        this.syllableCounterService = syllableCounterService;
    }

    @PostMapping("/count-syllables")
    public SyllableCountResponse countSyllables(@RequestBody PoemRequest poemRequest) {
        return syllableCounterService.analyzeLines(poemRequest.getLines());
    }
}
