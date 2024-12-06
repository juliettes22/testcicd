package com.fullstack.movies.backend.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.movies.backend.configurations.properties.JwtProperties;
import com.fullstack.movies.backend.exceptions.InternalServerException;
import com.fullstack.movies.backend.exceptions.errors.JwtErrorCode;
import com.fullstack.movies.backend.exceptions.errors.SystemErrorCode;
import com.fullstack.movies.backend.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class JwtManager {

    private final Clock clock;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    private void setupKey() {
        try {
            this.privateKey = this.parsePrivateKey(jwtProperties.getPrivateKey(), jwtProperties.getAlgorithm());
            this.publicKey = this.parsePublicKey(jwtProperties.getPublicKey(), jwtProperties.getAlgorithm());
        } catch (InternalServerException e) {
            log.error("Error while constructing private/public key from configuration: {}", e.getMessage());
            throw new InternalServerException(e, SystemErrorCode.TECHNICAL_ERROR);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtProperties.getValidity());
    }

    public Integer getExpirationTime() {
        return jwtProperties.getValidity();
    }


    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            long expiration
    ) {

        Instant now = clock.instant();

        JwtBuilder builder = Jwts
                .builder()
                .subject(user.getEmail())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiration)));

        extraClaims.forEach(builder::claim);

        return builder
                .signWith(privateKey)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .requireIssuer(jwtProperties.getIssuer())
                .json(new JacksonDeserializer<>(objectMapper))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private PrivateKey parsePrivateKey(String privateKey, String algorithm) {
        try {
            byte[] privateKeyB = Base64.getDecoder().decode(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyB));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalServerException(e, JwtErrorCode.PRIVATE_KEY_INVALID);
        }
    }

    private PublicKey parsePublicKey(String publicKey, String algorithm) {
        try {
            byte[] publicKeyB = Base64.getDecoder().decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyB));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new InternalServerException(e, JwtErrorCode.PUBLIC_KEY_INVALID);
        }
    }

}
