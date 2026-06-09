package br.com.instituto.teresa.service;

import br.com.instituto.teresa.domain.AdminUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret:my-secret-key-teresa}")
    private String secret;

    @PostConstruct
    public void validateSecret() {
        if ("insecure-local-dev-only".equals(secret) || "my-secret-key-teresa".equals(secret)) {
            throw new IllegalStateException(
                "JWT_SECRET não está configurado com um valor seguro. " +
                "Defina a variável de ambiente JWT_SECRET antes de iniciar a aplicação. " +
                "Gere um valor seguro com: openssl rand -hex 64"
            );
        }
    }

    public String generateToken(AdminUser user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("teresa-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("teresa-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
