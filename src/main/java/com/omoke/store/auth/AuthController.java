package com.omoke.store.auth;

import com.omoke.store.auth.dtos.AccessToken;
import com.omoke.store.config.JwtConfig;
import com.omoke.store.auth.dtos.LoginRequest;
import com.omoke.store.users.dtos.UserDto;
import com.omoke.store.users.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtConfig jwtConfig;
    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(
            @Valid @RequestBody LoginRequest authRequest,
            HttpServletResponse response
    ) {

        var authResponse = authService.login(authRequest);

        var cookie = new Cookie("refreshToken", authResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(new AccessToken(authResponse.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessToken> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var accessToken = authService.refresh(refreshToken);

        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/me")
    public UserDto me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        return userService.findUserById(userId);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
