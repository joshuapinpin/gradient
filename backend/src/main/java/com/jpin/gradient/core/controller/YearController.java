package com.jpin.gradient.core.controller;


import com.jpin.gradient.core.dto.create.YearCreateRequest;
import com.jpin.gradient.core.dto.response.YearResponse;
import com.jpin.gradient.core.dto.update.YearUpdateRequest;
import com.jpin.gradient.core.service.YearService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/years")
public class YearController {
    
    private final YearService yearService;
    
    public YearController(YearService yearService) {
        this.yearService = yearService;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public YearResponse create(@Valid @RequestBody YearCreateRequest request) {
        return yearService.createYear(request);
    }
    
    @GetMapping("/{id}")
    public YearResponse getById(@PathVariable Long id) {
        return yearService.getYearById(id);
    }
    
    @GetMapping
    public List<YearResponse> getAll() {
        return yearService.getYears();
    }

    @PutMapping("/{id}")
    public YearResponse update(
            @PathVariable Long id,
            @Valid @RequestBody YearUpdateRequest request) {
        return yearService.updateYear(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        yearService.deleteYear(id);
    }

    @DeleteMapping("/{yearId}/terms/{termId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTermFromYear(
            @PathVariable Long yearId,
            @PathVariable Long termId) {
        yearService.removeTermFromYear(yearId, termId);
    }
}
