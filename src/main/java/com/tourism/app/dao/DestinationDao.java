package com.tourism.app.dao;

import com.tourism.app.model.Destination;

import java.util.List;
import java.util.Optional;

public interface DestinationDao {
    List<Destination> findAll();
    Optional<Destination> findById(Long id);
    Destination save(Destination destination);
    Destination update(Long id, Destination destination);
    void deleteById(Long id);
    boolean existsById(Long id);
}
