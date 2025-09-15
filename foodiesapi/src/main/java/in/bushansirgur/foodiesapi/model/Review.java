package in.bushansirgur.foodiesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    private String restaurantId;
    private String menuItemId;
    private String deliveryPartnerId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    private String comment;
    private List<String> images;
    private ReviewType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isVerified;
    private Integer helpfulCount;
    private Boolean isReported;
    private String reportReason;
    
    public enum ReviewType {
        RESTAURANT, MENU_ITEM, DELIVERY_PARTNER, OVERALL
    }
}
