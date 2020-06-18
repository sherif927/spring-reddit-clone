package io.bitpark.redditclone.services;

import io.bitpark.redditclone.dto.AuthenticationResponse;
import io.bitpark.redditclone.dto.LoginRequest;
import io.bitpark.redditclone.dto.RefreshTokenRequest;
import io.bitpark.redditclone.dto.RegisterRequest;
import io.bitpark.redditclone.models.AuthUser;
import io.bitpark.redditclone.models.NotificationEmail;
import io.bitpark.redditclone.models.User;
import io.bitpark.redditclone.models.VerificationToken;
import io.bitpark.redditclone.exceptions.SpringRedditException;
import io.bitpark.redditclone.repositories.UserRepository;
import io.bitpark.redditclone.repositories.VerificationTokenRepository;
import io.bitpark.redditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    public User signUp(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        User createdUser = userRepository.save(user);

        String token = generateVerificationToken(createdUser);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

        return createdUser;
    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;

    }

    public void verifyAccount(String token) {
        VerificationToken existingToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid Token!"));
        fetchUserAndEnable(existingToken);
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User userToEnable = userRepository.findByUsername(username)
                            .orElseThrow(() -> new SpringRedditException("User Not Found"));
        userToEnable.setEnabled(true);
        userRepository.save(userToEnable);
    }


    public AuthenticationResponse login(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtProvider.generateToken(authentication.getName());

            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getExpiryLong()))
                    .username(loginRequest.getUsername())
                    .build();

        }catch(AuthenticationException  e){
            throw new SpringRedditException("Invalid Credentials");
        }
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        AuthUser principal = (AuthUser) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String newToken = jwtProvider.generateToken(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(newToken)
                .username(refreshTokenRequest.getUsername())
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpiryLong()))
                .build();
    }

}
