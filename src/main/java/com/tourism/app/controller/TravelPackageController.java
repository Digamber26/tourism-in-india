package com.tourism.app.controller;

import com.tourism.app.dao.TravelPackageDao;
import com.tourism.app.dto.ApiResponse;
import com.tourism.app.exception.ResourceNotFoundException;
import com.tourism.app.model.TravelPackage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "*")
public class TravelPackageController {

    private final TravelPackageDao travelPackageDao;

    public TravelPackageController(TravelPackageDao travelPackageDao) {
        this.travelPackageDao = travelPackageDao;
    }

    @GetMapping
    public ApiResponse<List<TravelPackage>> getAll(
            @RequestParam(required = false) Long destinationId) {
        List<TravelPackage> packages = destinationId != null
                ? travelPackageDao.findByDestinationId(destinationId)
                : travelPackageDao.findAll();
        return ApiResponse.success("Travel packages fetched successfully", packages);
    }

    @GetMapping("/{id}")
    public ApiResponse<TravelPackage> getById(@PathVariable Long id) {
        TravelPackage travelPackage = travelPackageDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel package not found with id: " + id));
        return ApiResponse.success("Travel package fetched successfully", travelPackage);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TravelPackage> create(@Valid @RequestBody TravelPackage travelPackage) {
        return ApiResponse.success("Travel package created successfully", travelPackageDao.save(travelPackage));
    }

    @PutMapping("/{id}")
    public ApiResponse<TravelPackage> update(@PathVariable Long id, @Valid @RequestBody TravelPackage travelPackage) {
        return ApiResponse.success("Travel package updated successfully", travelPackageDao.update(id, travelPackage));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        travelPackageDao.deleteById(id);
        return ApiResponse.success("Travel package deleted successfully", null);
    }
}
