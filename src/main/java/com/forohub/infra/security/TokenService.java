package com.forohub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.forohub.model.Usuario; // Asegúrate de que esta importación sea correcta
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String apiSecret;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            String token = JWT.create()
                    .withIssuer("forohub")
                    .withSubject(usuario.getCorreoElectronico())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){

            throw new RuntimeException("Error al generar el token JWT", exception);
        }
    }


    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException exception){
            // Token inválido o expirado
            throw new RuntimeException("Token JWT inválido o expirado", exception);
        }
    }

    private Instant generarFechaExpiracion() {
        // Genera la fecha de expiración sumando el tiempo de expiración a la fecha actual
        return LocalDateTime.now().plusSeconds(expirationTime / 1000).toInstant(ZoneOffset.of("-03:00"));
    }
}