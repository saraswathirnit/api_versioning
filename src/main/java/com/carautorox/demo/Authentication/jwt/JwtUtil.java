package com.carautorox.demo.Authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "secretKeyForJwtGeneration";

    /**
     * Generate a JWT token with claims: userId (subject), version, organization, issuedAt, expiry
     */
    public static String generateToken(String userId, String version, String org, int expiryMinutes) {
        long expiryTimeMillis = System.currentTimeMillis() + (expiryMinutes * 60 * 1000L);

        return Jwts.builder()
                .setSubject(userId)
                .claim("version", version != null ? version : "v1")
                .claim("organization", org != null ? org : "DefaultOrg")
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiryTimeMillis))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static String generateV1Token(String userId, String org, int expiryMinutes) {
        return generateToken(userId, "v1", org, expiryMinutes);
    }

    public static String generateV2Token(String userId, String org, int expiryMinutes) {
        return generateToken(userId, "v2", org, expiryMinutes);
    }

    /**
     * Validate the JWT token signature and expiration
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract userId (subject) from token
     */
    public static String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract version claim from token
     */
    public static String getVersionFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("version", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}


