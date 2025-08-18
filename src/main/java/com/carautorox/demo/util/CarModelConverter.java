package com.carautorox.demo.util;

import com.carautorox.demo.Version1.CarModelV1;
import com.carautorox.demo.version2.CarModelV2;

import java.time.Instant;

/**
 * Utility class to convert CarModelV1 instances to CarModelV2 instances.
 */
public class CarModelConverter {

    /**
     * Converts a CarModelV1 object to a CarModelV2 object.
     *
     * @param v1 the CarModelV1 instance to convert
     * @return a new CarModelV2 object with mapped fields
     */
    public static CarModelV2 convertV1ToV2(CarModelV1 v1) {
        if (v1 == null) {
            return null;
        }

        CarModelV2 v2 = new CarModelV2();
        
        // Map common fields
        v2.setCarId(v1.getCarId());
        v2.setCarOwnerName(v1.getCarOwnerName());
        v2.setOwnerMobileNumber(v1.getOwnerMobileNumber());

        // Set new V2-only fields to null or default values.
        v2.setChassisNo(null);                // V1 has no chassis info
        v2.setRcNo(null);                     // V1 has no RC info
        v2.setCarHealthInsurance(null);       // V1 has no insurance info
        v2.setUploadLicenseInfo(null);        // V1 has no license upload info

        // Use created/updated times from V1 if present, otherwise set to now.
        v2.setCreatedTime(v1.getCreatedTime() != null ? v1.getCreatedTime() : Instant.now());
        v2.setLastUpdatedTime(v1.getLastUpdatedTime() != null ? v1.getLastUpdatedTime() : Instant.now());

        return v2;
    }
}
