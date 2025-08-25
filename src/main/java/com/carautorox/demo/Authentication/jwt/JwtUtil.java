package com.carautorox.demo.Authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${security.jwt.secret:secretKeyForJwtGeneration}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) { // HS256 requires >= 256-bit key
            byte[] padded = new byte[32];
            System.arraycopy(bytes, 0, padded, 0, Math.min(bytes.length, 32));
            for (int i = bytes.length; i < 32; i++) padded[i] = (byte) i;
            bytes = padded;
        }
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    /** Generate a JWT with subject=userId and claims: version, organization, iat, exp */
    public String generateToken(String userId, String version, String org, int expiryMinutes) {
        long expiryTimeMillis = System.currentTimeMillis() + (expiryMinutes * 60 * 1000L);
        return Jwts.builder()
                .setSubject(Objects.requireNonNullElse(userId, "unknown"))
                .claim("version", Objects.requireNonNullElse(version, "v1"))
                .claim("organization", Objects.requireNonNullElse(org, "DefaultOrg"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiryTimeMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateV1Token(String userId, String org, int expiryMinutes) {
        return generateToken(userId, "v1", org, expiryMinutes);
    }

    public String generateV2Token(String userId, String org, int expiryMinutes) {
        return generateToken(userId, "v2", org, expiryMinutes);
    }

    /** Validate signature + expiration */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUserIdFromToken(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getVersionFromToken(String token) {
        try {
            return parseClaims(token).get("version", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getOrganizationFromToken(String token) {
        try {
            return parseClaims(token).get("organization", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
