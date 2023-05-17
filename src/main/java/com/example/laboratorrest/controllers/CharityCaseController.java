package com.example.laboratorrest.controllers;

import com.example.laboratorrest.model.CharityCase;
import com.example.laboratorrest.service.CharityCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/charitycases")
public class CharityCaseController {
    private final CharityCaseService charityCaseService;

    public CharityCaseController(CharityCaseService charityCaseService) {
        this.charityCaseService = charityCaseService;
    }

    @PostMapping
    public ResponseEntity<CharityCase> saveCharityCase(@RequestBody CharityCaseRequest charityCaseRequest) {
        Optional<CharityCase> charityCase = charityCaseService.save(
                charityCaseRequest.getName(),
                charityCaseRequest.getDescription());
        return charityCase.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCharityCase(@PathVariable("id") Long id) {
        Optional<CharityCase> charityCase = charityCaseService.deleteCharityCase(id);
        if (charityCase.isPresent()) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<CharityCase>> getAll() {
        return ResponseEntity.ok(this.charityCaseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharityCase> findOne(@PathVariable("id") long id) {
        Optional<CharityCase> charityCase = charityCaseService.findOneById(id);
        if (charityCase.isPresent()) {
            return ResponseEntity.ok(charityCase.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCharityCase(@PathVariable("id") long id,
                                                    @RequestBody CharityCaseRequest charityCaseRequest) {
        boolean wasEntityUpdated = charityCaseService.updateCharityCase(charityCaseRequest.getName(), charityCaseRequest.getDescription(), id);
        if (wasEntityUpdated) {
            return ResponseEntity.ok("Updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
