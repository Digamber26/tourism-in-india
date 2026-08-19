package com.tourism.app.model;

import jakarta.validation.constraints.NotBlank;

public class Destination {

    private Long id;

    @NotBlank(message = "Destination name is required")
    private String name;

    @NotBlank(message = "State is required")
    private String state;

    private String description;

    private String category; // e.g. Hill Station, Beach, Heritage, Wildlife

    public Destination() {
    }

    public Destination(Long id, String name, String state, String description, String category) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.category = category;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
