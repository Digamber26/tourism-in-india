package com.tourism.app.controller;

import com.tourism.app.dao.EnquiryDao;
import com.tourism.app.dto.ApiResponse;
import com.tourism.app.exception.ResourceNotFoundException;
import com.tourism.app.model.Enquiry;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/enquiries")
@CrossOrigin(origins = "*")
public class EnquiryController {

    private static final Set<String> VALID_STATUSES = Set.of("NEW", "CONTACTED", "CONFIRMED", "CLOSED");

    private final EnquiryDao enquiryDao;

    public EnquiryController(EnquiryDao enquiryDao) {
        this.enquiryDao = enquiryDao;
    }

    @GetMapping
    public ApiResponse<List<Enquiry>> getAll() {
        return ApiResponse.success("Enquiries fetched successfully", enquiryDao.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Enquiry> getById(@PathVariable Long id) {
        Enquiry enquiry = enquiryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found with id: " + id));
        return ApiResponse.success("Enquiry fetched successfully", enquiry);
    }

    // A customer submits this from the "Enquire Now" / booking form on a package page
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Enquiry> create(@Valid @RequestBody Enquiry enquiry) {
        return ApiResponse.success("Enquiry submitted successfully", enquiryDao.save(enquiry));
    }

    // Admin-side: move an enquiry through NEW -> CONTACTED -> CONFIRMED -> CLOSED
    @PatchMapping("/{id}/status")
    public ApiResponse<Enquiry> updateStatus(@PathVariable Long id, @RequestParam String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Status must be one of " + VALID_STATUSES);
        }
        return ApiResponse.success("Enquiry status updated", enquiryDao.updateStatus(id, status.toUpperCase()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        enquiryDao.deleteById(id);
        return ApiResponse.success("Enquiry deleted successfully", null);
    }
}
