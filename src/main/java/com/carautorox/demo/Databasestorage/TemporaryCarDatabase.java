package com.carautorox.demo.Databasestorage;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.carautorox.demo.Version1.CarModelV1;
import com.carautorox.demo.version2.CarModelV2;

public class TemporaryCarDatabase {

    // V1 Data store
    public static final List<CarModelV1> v1CarList = new CopyOnWriteArrayList<>();

    // V2 Data store
    public static final List<CarModelV2> v2CarList = new CopyOnWriteArrayList<>();

    static {
        loadInitialV1Data();

        // Clone V1 data into V2 with default values for missing mandatory fields
        cloneV1DataToV2();
    }

    private static void loadInitialV1Data() {
        CarModelV1 row1 = new CarModelV1();
        row1.setCarId("CAR101");
        row1.setCarOwnerName("Ravi Teja");
        row1.setOwnerMobileNumber("9876543210");
        row1.setCreatedTime(Instant.parse("2025-08-01T10:30:00Z"));
        row1.setLastUpdatedTime(null);

        v1CarList.add(row1);
    }

    private static void cloneV1DataToV2() {
        for (CarModelV1 v1Car : v1CarList) {
            CarModelV2 v2Car = new CarModelV2();

            v2Car.setCarId(v1Car.getCarId());
            v2Car.setCarOwnerName(v1Car.getCarOwnerName());
            v2Car.setOwnerMobileNumber(v1Car.getOwnerMobileNumber());
            v2Car.setCreatedTime(v1Car.getCreatedTime());
            v2Car.setLastUpdatedTime(v1Car.getLastUpdatedTime());

            // Set default values for new mandatory fields if null or empty
            v2Car.setChassisNo("DEFAULT-CHASSIS-" + v1Car.getCarId());
            v2Car.setRcNo("DEFAULT-RC-" + v1Car.getCarId());
            v2Car.setCarHealthInsurance("NOT_AVAILABLE");
            v2Car.setUploadLicenseInfo("NOT_UPLOADED");

            v2CarList.add(v2Car);
        }
    }
}
