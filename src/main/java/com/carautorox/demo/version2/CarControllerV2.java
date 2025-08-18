package com.carautorox.demo.version2;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 🚗 REST Controller for Version 2 Car API
 */
@RestController
@RequestMapping("/v2/cars")
@Validated
public class CarControllerV2 {

    private final CarServiceV2 carService;

    public CarControllerV2(CarServiceV2 carService) {
        this.carService = carService;
    }

    // ✅ GET all cars
    @GetMapping("/all")
    public List<CarModelV2> getAllCars() {
        return carService.getAllCars();
    }

    // ✅ GET car by ID
    @GetMapping("/info/{carId}")
    public ResponseEntity<CarModelV2> getCarById(@PathVariable String carId) {
        return carService.getCarById(carId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST add single car
    @PostMapping("/add")
    public ResponseEntity<String> addCar(@Valid @RequestBody CarInputDTOV2 carInput) {
        try {
            String message = carService.addSingleCar(carInput);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Internal Error: " + e.getMessage());
        }
    }

    // ✅ POST add multiple cars
    @PostMapping("/add-multiple")
    public ResponseEntity<String> addMultipleCars(@Valid @RequestBody List<@Valid CarInputDTOV2> carInputs) {
        try {
            String message = carService.addMultipleCars(carInputs);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Internal Error: " + e.getMessage());
        }
    }

    // ✅ PUT update car by ID
    @PutMapping("/update/{carId}")
    public ResponseEntity<String> updateCar(@PathVariable String carId, @Valid @RequestBody CarInputDTOV2 carInput) {
        try {
            String message = carService.updateCar(carId, carInput);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Internal Error: " + e.getMessage());
        }
    }

    // ✅ GET API usage stats
    @GetMapping("/api-usage")
    public ResponseEntity<Map<String, Integer>> getApiUsageStats() {
        return ResponseEntity.ok(carService.getApiUsageStats());
    }

    /**
     * ✅ Global handler for field validation errors (e.g. missing fields, invalid values)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), "❌ " + error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * ✅ Global handler for unknown (extra) fields
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnknownFields(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        if (ex.getCause() instanceof UnrecognizedPropertyException unrecognizedEx) {
            String unknownField = unrecognizedEx.getPropertyName();
            error.put(unknownField, "❌ Extra field '" + unknownField + "' is not allowed");
        } else {
            error.put("error", "❌ Invalid request body");
        }
        return ResponseEntity.badRequest().body(error);
    }
}
