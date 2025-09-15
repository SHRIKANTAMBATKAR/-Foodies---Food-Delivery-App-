package in.bushansirgur.foodiesapi.service;

import in.bushansirgur.foodiesapi.model.User;
import in.bushansirgur.foodiesapi.repository.UserRepository;
import in.bushansirgur.foodiesapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
    
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("User already exists with phone number: " + user.getPhoneNumber());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setStatus(User.UserStatus.ACTIVE);
        
        // Set default status based on role
        if (user.getRole() == User.UserRole.RESTAURANT) {
            user.setIsRestaurantApproved(false);
            user.setStatus(User.UserStatus.PENDING_APPROVAL);
        } else if (user.getRole() == User.UserRole.DELIVERY_PARTNER) {
            user.setIsDeliveryPartnerApproved(false);
            user.setStatus(User.UserStatus.PENDING_APPROVAL);
        }
        
        return userRepository.save(user);
    }
    
    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            if (user.getStatus() != User.UserStatus.ACTIVE) {
                throw new RuntimeException("Account is not active");
            }
            
            return jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
    
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
