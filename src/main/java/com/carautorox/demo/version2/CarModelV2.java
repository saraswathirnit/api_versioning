package com.carautorox.demo.version2;

import java.time.Instant;

public class CarModelV2 {
    private String carId;
    private String carOwnerName;
    private String ownerMobileNumber; // ✅ Renamed from carMobileNumber

   
    private String chassisNo;           // mandatory
    private String rcNo;                
    private String carHealthInsurance;  
    private String uploadLicenseInfo;  

    private Instant createdTime;
    private Instant lastUpdatedTime;

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

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getRcNo() {
        return rcNo;
    }

    public void setRcNo(String rcNo) {
        this.rcNo = rcNo;
    }

    public String getCarHealthInsurance() {
        return carHealthInsurance;
    }

    public void setCarHealthInsurance(String carHealthInsurance) {
        this.carHealthInsurance = carHealthInsurance;
    }

    public String getUploadLicenseInfo() {
        return uploadLicenseInfo;
    }

    public void setUploadLicenseInfo(String uploadLicenseInfo) {
        this.uploadLicenseInfo = uploadLicenseInfo;
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
