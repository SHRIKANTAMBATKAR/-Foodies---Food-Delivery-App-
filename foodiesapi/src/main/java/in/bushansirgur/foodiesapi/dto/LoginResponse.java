package in.bushansirgur.foodiesapi.dto;

import in.bushansirgur.foodiesapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String email;
    private String name;
    private String role;
    private String status;
    private String phoneNumber;
    private String profileImageUrl;
    
    // Restaurant specific fields
    private String restaurantName;
    private String restaurantImageUrl;
    private Boolean isRestaurantApproved;
    
    // Delivery partner specific fields
    private Boolean isDeliveryPartnerApproved;
    private Boolean isAvailable;
    
    public static LoginResponse fromUser(User user, String token) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRole(user.getRole().name());
        response.setStatus(user.getStatus().name());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setProfileImageUrl(user.getProfileImageUrl());
        
        if (user.getRole() == User.UserRole.RESTAURANT) {
            response.setRestaurantName(user.getRestaurantName());
            response.setRestaurantImageUrl(user.getRestaurantImageUrl());
            response.setIsRestaurantApproved(user.getIsRestaurantApproved());
        }
        
        if (user.getRole() == User.UserRole.DELIVERY_PARTNER) {
            response.setIsDeliveryPartnerApproved(user.getIsDeliveryPartnerApproved());
            response.setIsAvailable(user.getIsAvailable());
        }
        
        return response;
    }
}
