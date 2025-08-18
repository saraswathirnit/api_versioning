package com.carautorox.demo.version2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class V2SwaggerSupportController {

    @GetMapping("/v2/health")
    public String health() {
        return "Version 2 API ";
    }
}


