package com.streetlight.model;

public class StreetLight {

    private int lightId;
    private String lightCode;
    private String location;
    private int powerRating;
    private String status;
    private String createdAt;

    public StreetLight() {
    }

    public StreetLight(int lightId, String lightCode, String location,
                       int powerRating, String status, String createdAt) {

        this.lightId = lightId;
        this.lightCode = lightCode;
        this.location = location;
        this.powerRating = powerRating;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getLightId() {
        return lightId;
    }

    public void setLightId(int lightId) {
        this.lightId = lightId;
    }

    public String getLightCode() {
        return lightCode;
    }

    public void setLightCode(String lightCode) {
        this.lightCode = lightCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPowerRating() {
        return powerRating;
    }

    public void setPowerRating(int powerRating) {
        this.powerRating = powerRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}