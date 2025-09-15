package in.bushansirgur.foodiesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "addresses")
public class Address {
    @Id
    private String id;
    
    @NotBlank(message = "Address line is required")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Pincode is required")
    private String pincode;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotNull(message = "Latitude is required")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
    private String landmark;
    private String instructions;
    private AddressType type;
    private Boolean isDefault;
    private String userId; // Reference to user who owns this address
    
    public enum AddressType {
        HOME, WORK, OTHER, RESTAURANT
    }
}
