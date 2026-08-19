package com.tourism.app.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TravelPackage {

    private Long id;

    @NotBlank(message = "Package name is required")
    private String name;

    @NotNull(message = "Destination id is required")
    private Long destinationId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private int durationDays;

    private String inclusions; // e.g. "Hotel, Breakfast, Sightseeing"

    public TravelPackage() {
    }

    public TravelPackage(Long id, String name, Long destinationId, BigDecimal price,
                          int durationDays, String inclusions) {
        this.id = id;
        this.name = name;
        this.destinationId = destinationId;
        this.price = price;
        this.durationDays = durationDays;
        this.inclusions = inclusions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }
}
