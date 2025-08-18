
package com.carautorox.demo.Config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // ✅ Support for java.time.Instant, LocalDate, etc.
        mapper.registerModule(new JavaTimeModule());

        // ✅ Serialize java.time values (like Instant) as ISO-8601 strings
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ⛔️ Reject unknown properties during deserialization
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        return mapper;
    }
}
