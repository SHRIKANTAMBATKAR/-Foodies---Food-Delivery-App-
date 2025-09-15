package in.bushansirgur.foodiesapi.controller;

import in.bushansirgur.foodiesapi.dto.LoginRequest;
import in.bushansirgur.foodiesapi.dto.LoginResponse;
import in.bushansirgur.foodiesapi.model.User;
import in.bushansirgur.foodiesapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User registeredUser = authService.register(user);
            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", registeredUser.getId(),
                "email", registeredUser.getEmail(),
                "role", registeredUser.getRole().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            User user = authService.getUserByEmail(loginRequest.getEmail());
            LoginResponse response = LoginResponse.fromUser(user, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        try {
            // Extract user info from token (you might want to create a utility method for this)
            // For now, returning a placeholder
            return ResponseEntity.ok(Map.of("message", "Profile endpoint - implement token parsing"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
