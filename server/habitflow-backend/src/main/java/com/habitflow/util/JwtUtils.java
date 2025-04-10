package com.habitflow.util;

import io.smallrye.jwt.build.Jwt;
import java.time.Duration;
import java.util.Set;

public class JwtUtils {
    public static String generateToken(String email, Long userId){
        return Jwt.issuer("habitflow")
                .upn(email)
                .claim("userId", userId)
                .groups(Set.of("user"))
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}
