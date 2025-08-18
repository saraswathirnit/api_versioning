package com.carautorox.demo.Version1;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🌐 REST Controller for Version 1 Car API
 * Handles HTTP requests for car-related operations (CRUD) for Version 1.
 */
@RestController
@RequestMapping("/v1/cars")
@Validated
public class CarControllerV1 {

    private final CarServiceV1 carService;

    public CarControllerV1(CarServiceV1 carService) {
        this.carService = carService;
    }

    @Operation(
        summary = "Get all cars",
        description = "Returns the list of all cars for Version 1",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of cars",
                content = @Content(schema = @Schema(implementation = CarModelV1.class))
            ),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @GetMapping("/all")
    public ResponseEntity<?> getAllCars() {
        try {
            List<CarModelV1> cars = carService.getAllCars();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to fetch cars: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Get car by ID",
        description = "Retrieve a single car using its ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Car found",
                content = @Content(schema = @Schema(implementation = CarModelV1.class))
            ),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getCarById(@PathVariable String id) {
        try {
            return carService.getCarById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body("❌ Car not found!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to retrieve car: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Add a new car",
        description = "Adds a single car using CarInputDTOV1",
        responses = {
            @ApiResponse(responseCode = "200", description = "Car added successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to add car")
        }
    )
    @PostMapping("/add")
    public ResponseEntity<?> addSingleCar(@Valid @RequestBody CarInputDTOV1 dto) {
        try {
            String message = carService.addSingleCar(dto);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to add car: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Add multiple cars",
        description = "Adds multiple cars in one request",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cars added successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to add cars")
        }
    )
    @PostMapping("/add-multiple")
    public ResponseEntity<?> addMultipleCars(@Valid @RequestBody List<CarInputDTOV1> carList) {
        try {
            String message = carService.addMultipleCars(carList);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to add multiple cars: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Update car",
        description = "Update a car's details by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to update car")
        }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCar(@PathVariable String id, @Valid @RequestBody CarInputDTOV1 dto) {
        try {
            String message = carService.updateCar(id, dto);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to update car: " + e.getMessage());
        }
    }

    @Operation(summary = "Get API usage stats", description = "Returns usage statistics for Version 1 APIs")
    @GetMapping("/api-usage")
    public ResponseEntity<?> getApiUsageStats() {
        try {
            Map<String, Integer> stats = carService.getApiUsageStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to fetch API usage stats: " + e.getMessage());
        }
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, String>> handleUnknownProperties(UnrecognizedPropertyException ex) {
        String unknownField = ex.getPropertyName();
        Map<String, String> error = new HashMap<>();
        error.put("error", "❌ Extra field '" + unknownField + "' is not allowed in the request");
        return ResponseEntity.badRequest().body(error);
    }
}

