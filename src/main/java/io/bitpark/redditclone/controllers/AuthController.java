package io.bitpark.redditclone.controllers;

import io.bitpark.redditclone.dto.AuthenticationResponse;
import io.bitpark.redditclone.dto.LoginRequest;
import io.bitpark.redditclone.dto.RefreshTokenRequest;
import io.bitpark.redditclone.dto.RegisterRequest;
import io.bitpark.redditclone.models.User;
import io.bitpark.redditclone.services.AuthService;
import io.bitpark.redditclone.services.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest){
        User createdUser = authService.signUp(registerRequest);
        return ResponseEntity.ok("User Created");
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String token){
        authService.verifyAccount(token);
        return ResponseEntity.ok("Account Verified");
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
       return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok("User Logged out");
    }
}
