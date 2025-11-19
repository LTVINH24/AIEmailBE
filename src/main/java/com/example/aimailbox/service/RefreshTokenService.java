package com.example.aimailbox.service;

import com.example.aimailbox.model.RefreshToken;
import com.example.aimailbox.model.User;
import com.example.aimailbox.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshExpirationDays;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               @Value("${jwt.refresh-expiration-days}") long refreshExpirationDays) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpirationDays = refreshExpirationDays;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS))
                .build();
        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public boolean isExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public RefreshToken rotateRefreshToken(String tokenStr) {
        RefreshToken existing = refreshTokenRepository.findByTokenForUpdate(tokenStr)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (isExpired(existing)) {
            refreshTokenRepository.delete(existing);
            throw new RuntimeException("Refresh token expired");
        }
        User user = existing.getUser();
        refreshTokenRepository.delete(existing);
        RefreshToken newToken = createRefreshToken(user);
        return newToken;
    }

    public void deleteByToken(String token) {
        findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
