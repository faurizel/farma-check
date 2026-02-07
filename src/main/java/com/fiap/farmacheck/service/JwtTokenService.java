package com.fiap.farmacheck.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fiap.farmacheck.exception.GenerateTokenErrorException;
import com.fiap.farmacheck.exception.ValidateTokenErrorException;
import com.fiap.farmacheck.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtTokenService {

    private static final String ISSUER = "Farma Check API";
    private static final ZoneId ZONE_SAO_PAULO = ZoneId.of("America/Sao_Paulo");

    @Value("${api.security.token.secret}")
    private String secret;

    private Algorithm algorithm;

    @PostConstruct
    void init() {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(UserDetailsImpl user) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(creationDate())
                    .withExpiresAt(expirationDate())
                    .withSubject(user.getUsername())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new GenerateTokenErrorException("Ocorreu um erro ao GERAR o token. Verifique!");
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new ValidateTokenErrorException("Token inválido ou expirado.");
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now(ZONE_SAO_PAULO).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZONE_SAO_PAULO).plusHours(2).toInstant();
    }

}
