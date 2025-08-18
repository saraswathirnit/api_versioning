package com.carautorox.demo.Config;

import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtTokenStoreConfig {
    public static final Map<String, String> userToTokenMap = new HashMap<>();
    public static final Map<String, String> tokenToClientTypeMap = new HashMap<>();
    public static void saveToken(String userId, String token, String allowedVersion, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveToken'");
    }
}
