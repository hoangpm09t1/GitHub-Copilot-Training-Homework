package training.copilot.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
@Tag(name = "Protected Resources", description = "Protected endpoints requiring JWT authentication")
public class ProtectedController {

    @GetMapping("/hello")
    @Operation(summary = "Hello", description = "A protected endpoint that requires JWT token", 
               security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<String> hello(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok("Hello, " + username + "! You are authenticated.");
    }

    @GetMapping("/user-info")
    @Operation(summary = "User Info", description = "Get authenticated user information",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<UserInfo> getUserInfo(Authentication authentication) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(authentication.getName());
        userInfo.setAuthorities(authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList());
        return ResponseEntity.ok(userInfo);
    }

    public static class UserInfo {
        private String username;
        private java.util.List<String> authorities;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public java.util.List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(java.util.List<String> authorities) {
            this.authorities = authorities;
        }
    }
}

