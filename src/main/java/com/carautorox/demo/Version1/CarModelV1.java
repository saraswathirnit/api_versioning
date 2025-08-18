package com.carautorox.demo.Version1;

import java.time.Instant;

/**
 * 🚗 Model class for Version 1 Car API
 */
public class CarModelV1 {

    private String carId;
    private String carOwnerName;
    private String ownerMobileNumber;
    private Instant createdTime;
    private Instant lastUpdatedTime;

    public CarModelV1() {}

    public CarModelV1(String carId, String carOwnerName, String ownerMobileNumber, Instant createdTime) {
        this.carId = carId;
        this.carOwnerName = carOwnerName;
        this.ownerMobileNumber = ownerMobileNumber;
        this.createdTime = createdTime;
        this.lastUpdatedTime = createdTime;
    }

    // Getters and Setters
    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarOwnerName() {
        return carOwnerName;
    }

    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }

    public String getOwnerMobileNumber() {
        return ownerMobileNumber;
    }

    public void setOwnerMobileNumber(String ownerMobileNumber) {
        this.ownerMobileNumber = ownerMobileNumber;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Instant lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
