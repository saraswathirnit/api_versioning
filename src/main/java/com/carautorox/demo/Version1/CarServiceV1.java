package com.carautorox.demo.Version1;

import com.carautorox.demo.Databasestorage.TemporaryCarDatabase;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🧠 Business logic for Version 1 Car API
 */
@Service
public class CarServiceV1 {

    private static final Map<String, Integer> apiUsageCount = new ConcurrentHashMap<>();

    private void incrementCount(String endpoint) {
        apiUsageCount.merge(endpoint, 1, Integer::sum);
    }

    public List<CarModelV1> getAllCars() {
        incrementCount("GET:/v1/cars/all");
        return TemporaryCarDatabase.v1CarList;
    }

    public Optional<CarModelV1> getCarById(String id) {
        incrementCount("GET:/v1/cars/info/{id}");
        return TemporaryCarDatabase.v1CarList.stream()
                .filter(car -> car.getCarId().equals(id))
                .findFirst();
    }

    public String addSingleCar(CarInputDTOV1 dto) {
        incrementCount("POST:/v1/cars/add");

        // Check for duplication
        boolean exists = TemporaryCarDatabase.v1CarList.stream()
                .anyMatch(car -> car.getCarId().equalsIgnoreCase(dto.getCarId()));

        if (exists) {
            return "❌ Car with ID [" + dto.getCarId() + "] already exists. Please check in 'View All Cars'.";
        }

        CarModelV1 car = new CarModelV1(
                dto.getCarId(),
                dto.getCarOwnerName(),
                dto.getOwnerMobileNumber(),
                Instant.now()
        );
        TemporaryCarDatabase.v1CarList.add(car);
        return "✅ Car added successfully!";
    }

    public String addMultipleCars(List<CarInputDTOV1> carList) {
        incrementCount("POST:/v1/cars/add-multiple");

        int added = 0;
        int skipped = 0;
        StringBuilder skippedIds = new StringBuilder();

        for (CarInputDTOV1 dto : carList) {
            boolean exists = TemporaryCarDatabase.v1CarList.stream()
                    .anyMatch(car -> car.getCarId().equalsIgnoreCase(dto.getCarId()));

            if (exists) {
                skipped++;
                skippedIds.append(dto.getCarId()).append(" ");
                continue;
            }

            CarModelV1 car = new CarModelV1(
                    dto.getCarId(),
                    dto.getCarOwnerName(),
                    dto.getOwnerMobileNumber(),
                    Instant.now()
            );
            TemporaryCarDatabase.v1CarList.add(car);
            added++;
        }

        return "✅ Added: " + added + " cars" +
                (skipped > 0 ? " | ⚠️ Skipped (duplicate IDs): " + skipped + " [" + skippedIds.toString().trim() + "]" : "");
    }

    public String updateCar(String id, CarInputDTOV1 dto) {
        incrementCount("PUT:/v1/cars/update/{id}");
        for (CarModelV1 car : TemporaryCarDatabase.v1CarList) {
            if (car.getCarId().equals(id)) {
                car.setCarOwnerName(dto.getCarOwnerName());
                car.setOwnerMobileNumber(dto.getOwnerMobileNumber());
                car.setLastUpdatedTime(Instant.now());
                return "🔄 Car info updated!";
            }
        }
        return "❌ Car not found!";
    }

    public Map<String, Integer> getApiUsageStats() {
        return apiUsageCount;
    }
}
