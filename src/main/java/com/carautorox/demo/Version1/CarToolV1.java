package com.carautorox.demo.Version1;

import com.carautorox.demo.mcp.McpToolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * CarToolV1 (Aug 21) — minimal MCP router:
 * actions: list | get | add | update
 */
@Component
public class CarToolV1 implements McpToolHandler {

    private final CarServiceV1 carService;

    @Autowired
    public CarToolV1(CarServiceV1 carService) {
        this.carService = carService;
    }

    @Override
    public String getToolName() {
        return "car.v1";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Object input) {
        try {
            Map<String, Object> args = (Map<String, Object>) input;
            String action = ((String) args.get("action")).toLowerCase();

            return switch (action) {
                case "list" -> carService.getAllCars();
                case "get" -> carService.getCarById((String) args.get("id"))
                        .orElseThrow(() -> new RuntimeException("❌ Not found"));
                case "add" -> {
                    CarInputDTOV1 dto = new CarInputDTOV1();
                    dto.setCarId((String) args.get("carId"));
                    dto.setCarOwnerName((String) args.get("carOwnerName"));
                    dto.setOwnerMobileNumber((String) args.get("ownerMobileNumber"));
                    yield carService.addSingleCar(dto);
                }
                case "update" -> {
                    CarInputDTOV1 dto = new CarInputDTOV1();
                    dto.setCarOwnerName((String) args.get("carOwnerName"));
                    dto.setOwnerMobileNumber((String) args.get("ownerMobileNumber"));
                    yield carService.updateCar((String) args.get("id"), dto);
                }
                default -> "❌ Unknown action";
            };
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}
