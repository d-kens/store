package com.omoke.store.auth;


import com.omoke.store.auth.dtos.AccessToken;
import com.omoke.store.auth.dtos.AuthResponse;
import com.omoke.store.auth.dtos.LoginRequest;
import com.omoke.store.entities.User;
import com.omoke.store.users.UsersRepository;
import com.omoke.store.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        var user = usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);


        return new AuthResponse(accessToken.toString(), refreshToken.toString());
    }


    public AccessToken refresh(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired())
            return null;

        var user = usersRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user).toString();

        return new AccessToken(accessToken);
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        return usersRepository.findById(userId).orElse(null);
    }
}
