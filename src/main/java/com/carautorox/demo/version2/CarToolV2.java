package com.carautorox.demo.version2;

import com.carautorox.demo.mcp.McpToolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * CarToolV2 (Aug 21) — minimal MCP router:
 * actions: list | get | add | update
 *
 * Adjust DTO/service names if yours differ (e.g., CarInputDTOV2).
 */
@Component
public class CarToolV2 implements McpToolHandler {

    private final CarServiceV2 carService;

    @Autowired
    public CarToolV2(CarServiceV2 carService) {
        this.carService = carService;
    }

    @Override
    public String getToolName() {
        return "car.v2";
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
                    // If your project uses a DTO class, map fields accordingly.
                    CarInputDTOV2 dto = new CarInputDTOV2();
                    dto.setCarId((String) args.get("carId"));
                    dto.setCarOwnerName((String) args.get("carName"));
                    dto.setCarOdometer(castInt(args.get("carOdometer")));
                    dto.setCarRcNumber((String) args.get("carRcNumber"));
                    dto.setCarRcNumber((String) args.get("carChassisNumber"));
                    yield carService.addSingleCar(dto);
                }
                case "update" -> {
                    CarInputDTOV2 dto = new CarInputDTOV2();
                    dto.setCarOwnerName((String) args.get("carName"));
                    dto.setCarOdometer(castInt(args.get("carOdometer")));
                    dto.setCarRcNumber((String) args.get("carRcNumber"));
                    dto.setCarRcNumber((String) args.get("carChassisNumber"));
                    yield carService.updateCar((String) args.get("id"), dto);
                }
                default -> "❌ Unknown action";
            };
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    private Integer castInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        return Integer.parseInt(String.valueOf(v));
    }
}
