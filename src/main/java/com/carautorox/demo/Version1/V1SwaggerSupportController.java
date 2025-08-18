package com.carautorox.demo.Version1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class V1SwaggerSupportController {

    @GetMapping("/v1/health")
    public String health() {
        return "Version 1 API ";
    }
}

