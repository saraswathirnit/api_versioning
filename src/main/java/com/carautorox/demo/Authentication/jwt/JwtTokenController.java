package com.carautorox.demo.Authentication.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Hidden;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping // no base path here to allow multiple versions separately
public class JwtTokenController {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenController.class);

    // Hardcoded users with credentials and allowed API version
    private static final Map<String, String> userToSecurityCode = new HashMap<>();
    static final Map<String, String> userToVersion = new HashMap<>();

    // Secret key for signing JWT tokens (keep it safe!)
    static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    static {
        userToSecurityCode.put("Axcoo1", "XAXmz123Encrypted");
        userToVersion.put("Axcoo1", "v1");

        userToSecurityCode.put("Bxc002", "BXCmz456Encrypted");
        userToVersion.put("Bxc002", "v2");

        userToSecurityCode.put("SharedUser", "SHAREDkeyEncrypted");
        userToVersion.put("SharedUser", "both");
    }
     @Hidden
    @PostMapping("/v1/auth/token")
    public ResponseEntity<?> generateTokenV1(
            @RequestHeader("USER-ID") String userId,
            @RequestHeader("SECURITY-CODE") String securityCode) {

        userId = userId.trim();
        securityCode = securityCode.trim();

        log.info("Received USER-ID: '{}', SECURITY-CODE: '{}'", userId, securityCode);

        // Validate credentials
        if (!userToSecurityCode.containsKey(userId) ||
                !userToSecurityCode.get(userId).equals(securityCode)) {
            log.warn("Invalid credentials for USER-ID '{}'", userId);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String allowedVersion = userToVersion.get(userId);
        if (!"v1".equalsIgnoreCase(allowedVersion) && !"both".equalsIgnoreCase(allowedVersion)) {
            log.warn("User '{}' not allowed for version v1", userId);
            return ResponseEntity.status(401).body(Map.of("error", "Version access denied"));
        }

        // Generate real JWT token valid for 30 minutes
        long expiry = System.currentTimeMillis() + 30 * 60 * 1000L;
        String token = Jwts.builder()
                .setSubject(userId)
                .claim("version", allowedVersion)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiry))
                .signWith(SECRET_KEY)
                .compact();

        log.info("Generated JWT token for user '{}'", userId);

        return ResponseEntity.ok(Map.of("token", token));
    }


    // New endpoint for version 2 token generation
    @Hidden
    @PostMapping("/v2/auth/token")
    public ResponseEntity<?> generateTokenV2(
            @RequestHeader("USER-ID") String userId,
            @RequestHeader("SECURITY-CODE") String securityCode) {

        userId = userId.trim();
        securityCode = securityCode.trim();

        log.info("Received USER-ID: '{}', SECURITY-CODE: '{}'", userId, securityCode);

        // Validate credentials
        if (!userToSecurityCode.containsKey(userId) ||
                !userToSecurityCode.get(userId).equals(securityCode)) {
            log.warn("Invalid credentials for USER-ID '{}'", userId);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String allowedVersion = userToVersion.get(userId);
        if (!"v2".equalsIgnoreCase(allowedVersion) && !"both".equalsIgnoreCase(allowedVersion)) {
            log.warn("User '{}' not allowed for version v2", userId);
            return ResponseEntity.status(401).body(Map.of("error", "Version access denied"));
        }

        // Generate JWT token valid for 30 minutes
        long expiry = System.currentTimeMillis() + 1* 60 * 1000L;
        String token = Jwts.builder()
                .setSubject(userId)
                .claim("version", allowedVersion)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiry))
                .signWith(SECRET_KEY)
                .compact();

        log.info("Generated JWT token for user '{}'", userId);

        return ResponseEntity.ok(Map.of("token", token));
    }
}


