package br.com.instituto.teresa.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limita tentativas de login por IP para prevenir ataques de força bruta.
 * Configurável via LOGIN_RATELIMIT_MAX_ATTEMPTS e LOGIN_RATELIMIT_WINDOW_MINUTES.
 */
@Component
public class LoginRateLimiter {

    private record AttemptRecord(int count, Instant windowStart) {}

    private final ConcurrentHashMap<String, AttemptRecord> attempts = new ConcurrentHashMap<>();

    @Value("${login.ratelimit.max-attempts:5}")
    private int maxAttempts;

    @Value("${login.ratelimit.window-minutes:15}")
    private int windowMinutes;

    public void checkBlocked(String ip) {
        AttemptRecord record = attempts.get(ip);
        if (record == null) return;

        if (isExpired(record)) {
            attempts.remove(ip);
            return;
        }

        if (record.count() >= maxAttempts) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Muitas tentativas de login. Tente novamente em " + windowMinutes + " minutos.");
        }
    }

    public void recordFailure(String ip) {
        attempts.compute(ip, (key, existing) -> {
            if (existing == null || isExpired(existing)) {
                return new AttemptRecord(1, Instant.now());
            }
            return new AttemptRecord(existing.count() + 1, existing.windowStart());
        });
    }

    public void resetAttempts(String ip) {
        attempts.remove(ip);
    }

    private boolean isExpired(AttemptRecord record) {
        return record.windowStart().plusSeconds(windowMinutes * 60L).isBefore(Instant.now());
    }
}
