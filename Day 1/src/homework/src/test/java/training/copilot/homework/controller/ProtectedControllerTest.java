package training.copilot.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class ProtectedControllerTest {

    private ProtectedController controller;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProtectedController();
    }

    @Test
    void hello_returnsGreetingForAuthenticatedUser() {
        when(authentication.getName()).thenReturn("testuser");

        ResponseEntity<String> response = controller.hello(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello, testuser! You are authenticated.");
    }

    @Test
    void getUserInfo_returnsUsernameAndAuthorities() {
        when(authentication.getName()).thenReturn("testuser");
        List<org.springframework.security.core.GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        doReturn(authorities).when(authentication).getAuthorities();

        ResponseEntity<ProtectedController.UserInfo> response = controller.getUserInfo(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProtectedController.UserInfo userInfo = response.getBody();

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getUsername()).isEqualTo("testuser");
        assertThat(userInfo.getAuthorities()).containsExactly("ROLE_USER", "ROLE_ADMIN");
    }
}
