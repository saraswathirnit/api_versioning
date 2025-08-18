package com.carautorox.demo.version2;

import com.carautorox.demo.Databasestorage.TemporaryCarDatabase;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CarServiceV2 {

    private final ConcurrentHashMap<String, AtomicInteger> apiCallCounts = new ConcurrentHashMap<>();

    private void incrementApiCallCount(String method, String path) {
        String key = method + " " + path;
        apiCallCounts.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public List<CarModelV2> getAllCars() {
        incrementApiCallCount("GET", "/v2/cars/all");
        return TemporaryCarDatabase.v2CarList;
    }

    public Optional<CarModelV2> getCarById(String carId) {
        incrementApiCallCount("GET", "/v2/cars/info/{carId}");
        return TemporaryCarDatabase.v2CarList.stream()
                .filter(car -> car.getCarId().equals(carId))
                .findFirst();
    }

    public String addSingleCar(CarInputDTOV2 carInput) {
        incrementApiCallCount("POST", "/v2/cars/add");

        validateMandatoryFields(carInput);

        // check duplicate carId
        if (isDuplicateCarId(carInput.getCarId())) {
            throw new IllegalArgumentException("❌ Duplicate carId found: " + carInput.getCarId());
        }

        CarModelV2 car = buildCarModel(carInput);
        TemporaryCarDatabase.v2CarList.add(car);
        return "✅ Car added successfully!";
    }

    public String addMultipleCars(List<CarInputDTOV2> carInputs) {
        incrementApiCallCount("POST", "/v2/cars/add-multiple");

        for (CarInputDTOV2 input : carInputs) {
            validateMandatoryFields(input);

            if (isDuplicateCarId(input.getCarId())) {
                throw new IllegalArgumentException("❌ Duplicate carId found: " + input.getCarId());
            }

            CarModelV2 car = buildCarModel(input);
            TemporaryCarDatabase.v2CarList.add(car);
        }
        return "✅ Multiple cars added successfully!";
    }

    public String updateCar(String carId, CarInputDTOV2 carInput) {
        incrementApiCallCount("PUT", "/v2/cars/update/{carId}");

        validateMandatoryFields(carInput);

        for (int i = 0; i < TemporaryCarDatabase.v2CarList.size(); i++) {
            CarModelV2 existing = TemporaryCarDatabase.v2CarList.get(i);
            if (existing.getCarId().equals(carId)) {
                existing.setCarOwnerName(carInput.getCarOwnerName());
                existing.setOwnerMobileNumber(carInput.getOwnerMobileNumber());
                existing.setChassisNo(carInput.getChassisNo());
                existing.setRcNo(carInput.getRcNo());
                existing.setCarHealthInsurance(carInput.getCarHealthInsurance());
                existing.setUploadLicenseInfo(carInput.getUploadLicenseInfo());
                existing.setLastUpdatedTime(Instant.now());
                TemporaryCarDatabase.v2CarList.set(i, existing);
                return "🔄 Car info updated!";
            }
        }
        throw new NoSuchElementException("❌ Car with ID " + carId + " not found");
    }

    public Map<String, Integer> getApiUsageStats() {
        Map<String, Integer> usage = new HashMap<>();
        apiCallCounts.forEach((key, count) -> usage.put(key, count.get()));
        return usage;
    }

    private void validateMandatoryFields(CarInputDTOV2 input) {
        if (isBlank(input.getCarId())) throw new IllegalArgumentException("❗ carId is mandatory");
        if (isBlank(input.getCarOwnerName())) throw new IllegalArgumentException("❗ carOwnerName is mandatory");
        if (isBlank(input.getOwnerMobileNumber())) throw new IllegalArgumentException("❗ ownerMobileNumber is mandatory");
        if (isBlank(input.getChassisNo())) throw new IllegalArgumentException("❗ chassisNo is mandatory");
        if (isBlank(input.getRcNo())) throw new IllegalArgumentException("❗ rcNo is mandatory");
        if (isBlank(input.getCarHealthInsurance())) throw new IllegalArgumentException("❗ carHealthInsurance is mandatory");
        if (isBlank(input.getUploadLicenseInfo())) throw new IllegalArgumentException("❗ uploadLicenseInfo is mandatory");
    }

    private boolean isDuplicateCarId(String carId) {
        return TemporaryCarDatabase.v2CarList.stream()
                .anyMatch(car -> car.getCarId().equals(carId));
    }

    private CarModelV2 buildCarModel(CarInputDTOV2 dto) {
        CarModelV2 car = new CarModelV2();
        car.setCarId(dto.getCarId());
        car.setCarOwnerName(dto.getCarOwnerName());
        car.setOwnerMobileNumber(dto.getOwnerMobileNumber());
        car.setChassisNo(dto.getChassisNo());
        car.setRcNo(dto.getRcNo());
        car.setCarHealthInsurance(dto.getCarHealthInsurance());
        car.setUploadLicenseInfo(dto.getUploadLicenseInfo());
        car.setCreatedTime(Instant.now());
        car.setLastUpdatedTime(null);
        return car;
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
