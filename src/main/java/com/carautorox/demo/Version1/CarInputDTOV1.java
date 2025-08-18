package com.carautorox.demo.Version1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 📥 Version 1 - Input DTO for Car
 * - All fields are optional
 * - Extra fields will throw error
 */
@JsonIgnoreProperties(ignoreUnknown = false)  // ⛔ Extra fields not allowed
public class CarInputDTOV1 {

    private String carId;

    private String carOwnerName;

    private String ownerMobileNumber;

    // --- Getters and Setters ---
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
}
