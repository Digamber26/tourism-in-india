package com.tourism.app.dao;

import com.tourism.app.model.Enquiry;

import java.util.List;
import java.util.Optional;

public interface EnquiryDao {
    List<Enquiry> findAll();
    Optional<Enquiry> findById(Long id);
    Enquiry save(Enquiry enquiry);
    Enquiry updateStatus(Long id, String status);
    void deleteById(Long id);
    boolean existsById(Long id);
}
