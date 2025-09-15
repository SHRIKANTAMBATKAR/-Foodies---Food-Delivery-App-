package in.bushansirgur.foodiesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Indexed(unique = true)
    private String phoneNumber;
    
    private UserRole role;
    private UserStatus status;
    private String profileImageUrl;
    private Address defaultAddress;
    private List<Address> addresses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean emailVerified;
    private boolean phoneVerified;
    
    // Restaurant specific fields
    private String restaurantName;
    private String restaurantDescription;
    private String restaurantImageUrl;
    private Address restaurantAddress;
    private String cuisineType;
    private Double rating;
    private Integer totalReviews;
    private String licenseNumber;
    private String gstNumber;
    private Boolean isRestaurantApproved;
    
    // Delivery partner specific fields
    private String vehicleType;
    private String vehicleNumber;
    private String licenseNumber;
    private Boolean isDeliveryPartnerApproved;
    private Double currentLatitude;
    private Double currentLongitude;
    private Boolean isAvailable;
    private Double totalEarnings;
    private Integer totalDeliveries;
    
    public enum UserRole {
        CUSTOMER, RESTAURANT, DELIVERY_PARTNER, ADMIN
    }
    
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, PENDING_APPROVAL
    }
}
