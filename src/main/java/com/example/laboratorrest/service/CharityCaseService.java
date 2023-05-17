package com.example.laboratorrest.service;

import com.example.laboratorrest.model.CharityCase;
import com.example.laboratorrest.repository.CharityCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharityCaseService {
    private final CharityCaseRepository charityCaseRepository;

    public CharityCaseService(CharityCaseRepository charityCaseRepository) {
        this.charityCaseRepository = charityCaseRepository;
    }

    public List<CharityCase> getAll() {
        return this.charityCaseRepository.findAll();
    }

    public Optional<CharityCase> save(String caseName, String description) {
        return this.charityCaseRepository.save(new CharityCase(caseName, description));
    }

    public Optional<CharityCase> findOneById(Long id) {
        return this.charityCaseRepository.findOneById(id);
    }

    public Optional<CharityCase> deleteCharityCase(Long id) {
        return this.charityCaseRepository.delete(id);
    }

    public boolean updateCharityCase(String caseName, String description, Long id) {
        return this.charityCaseRepository.update(new CharityCase(id, caseName, description));
    }
}
