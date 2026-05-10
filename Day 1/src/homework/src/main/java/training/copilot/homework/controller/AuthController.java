package training.copilot.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.copilot.homework.dto.AuthRequest;
import training.copilot.homework.dto.AuthResponse;
import training.copilot.homework.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and receive JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // For demo purposes - in production, validate against a user database
        if ("admin".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = jwtTokenProvider.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, request.getUsername()));
        }
        
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate Token", description = "Check if JWT token is valid")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}

