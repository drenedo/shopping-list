package me.renedo.config.security.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Component
public class TokenValidator {
    //One year of validity
    public static final long JWT_TOKEN_VALIDITY = 365 * 24 * 60 * 60;

    private static final Logger logger = LogManager.getLogger();

    private final Algorithm hmac512;
    private final JWTVerifier verifier;

    public TokenValidator(@Value("${jwt.secret:vgsdgsdgfsdgfWEtwetfsdfar234234}") final String secret) {
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
    }

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
            .withSubject(userDetails.getUsername())
            .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(JWT_TOKEN_VALIDITY).atZone(ZoneId.systemDefault()).toInstant()))
            .sign(this.hmac512);
    }

    public String validateTokenAndGetUsername(final String token) {
        try {
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            logger.warn("token invalid: {}", verificationEx.getMessage());
            return null;
        }
    }
}
