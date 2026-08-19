package com.tourism.app.dao;

import com.tourism.app.model.TravelPackage;

import java.util.List;
import java.util.Optional;

public interface TravelPackageDao {
    List<TravelPackage> findAll();
    List<TravelPackage> findByDestinationId(Long destinationId);
    Optional<TravelPackage> findById(Long id);
    TravelPackage save(TravelPackage travelPackage);
    TravelPackage update(Long id, TravelPackage travelPackage);
    void deleteById(Long id);
    boolean existsById(Long id);
}
