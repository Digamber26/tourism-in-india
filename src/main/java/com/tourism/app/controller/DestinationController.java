package com.tourism.app.controller;

import com.tourism.app.dao.DestinationDao;
import com.tourism.app.dto.ApiResponse;
import com.tourism.app.exception.ResourceNotFoundException;
import com.tourism.app.model.Destination;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@CrossOrigin(origins = "*")
public class DestinationController {

    private final DestinationDao destinationDao;

    public DestinationController(DestinationDao destinationDao) {
        this.destinationDao = destinationDao;
    }

    @GetMapping
    public ApiResponse<List<Destination>> getAll() {
        return ApiResponse.success("Destinations fetched successfully", destinationDao.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Destination> getById(@PathVariable Long id) {
        Destination destination = destinationDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));
        return ApiResponse.success("Destination fetched successfully", destination);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Destination> create(@Valid @RequestBody Destination destination) {
        Destination saved = destinationDao.save(destination);
        return ApiResponse.success("Destination created successfully", saved);
    }

    @PutMapping("/{id}")
    public ApiResponse<Destination> update(@PathVariable Long id, @Valid @RequestBody Destination destination) {
        Destination updated = destinationDao.update(id, destination);
        return ApiResponse.success("Destination updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        destinationDao.deleteById(id);
        return ApiResponse.success("Destination deleted successfully", null);
    }
}
