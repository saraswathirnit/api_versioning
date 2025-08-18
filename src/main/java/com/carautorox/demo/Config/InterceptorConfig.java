package com.carautorox.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import com.carautorox.demo.Authentication.jwt.JwtAuthInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor())
                .addPathPatterns("/v1/**", "/v2/**")
                .excludePathPatterns("/v1/auth/token", "/v2/auth/token"); 
              
    }
}



