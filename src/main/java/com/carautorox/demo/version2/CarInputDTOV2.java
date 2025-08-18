package com.carautorox.demo.version2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * 📥 Input DTO for Version 2 Car API
 * - Only chassisNo is mandatory
 * - All other fields are optional
 * - Extra unknown fields will trigger an error
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class CarInputDTOV2 {

    private String carId;
    private String carOwnerName;
    private String ownerMobileNumber;
    
    @NotBlank(message = "Chassis Number is mandatory") // ✅ Only this is required
    private String chassisNo;
    
    private String rcNo;
    private String carHealthInsurance;
    private String uploadLicenseInfo;

    // Getters and Setters

    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }

    public String getCarOwnerName() { return carOwnerName; }
    public void setCarOwnerName(String carOwnerName) { this.carOwnerName = carOwnerName; }

    public String getOwnerMobileNumber() { return ownerMobileNumber; }
    public void setOwnerMobileNumber(String ownerMobileNumber) { this.ownerMobileNumber = ownerMobileNumber; }

    public String getChassisNo() { return chassisNo; }
    public void setChassisNo(String chassisNo) { this.chassisNo = chassisNo; }

    public String getRcNo() { return rcNo; }
    public void setRcNo(String rcNo) { this.rcNo = rcNo; }

    public String getCarHealthInsurance() { return carHealthInsurance; }
    public void setCarHealthInsurance(String carHealthInsurance) { this.carHealthInsurance = carHealthInsurance; }

    public String getUploadLicenseInfo() { return uploadLicenseInfo; }
    public void setUploadLicenseInfo(String uploadLicenseInfo) { this.uploadLicenseInfo = uploadLicenseInfo; }
}
