package com.example.copilot.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret",
                "mySecretKeyForJWTThatIsAtLeast256BitsLongForHS256Algorithm");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 86400000L);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user@example.com", null, List.of());

        String token = jwtUtil.generateToken(auth);

        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user@example.com", null, List.of());
        String token = jwtUtil.generateToken(auth);

        String username = jwtUtil.getUsernameFromToken(token);

        assertThat(username).isEqualTo("user@example.com");
    }

    @Test
    void validateToken_shouldReturnTrue_forValidToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user@example.com", null, List.of());
        String token = jwtUtil.generateToken(auth);

        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalse_forInvalidToken() {
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalse_forTamperedToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user@example.com", null, List.of());
        String token = jwtUtil.generateToken(auth) + "tampered";

        assertThat(jwtUtil.validateToken(token)).isFalse();
    }
}
