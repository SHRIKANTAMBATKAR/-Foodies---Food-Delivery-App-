package in.bushansirgur.foodiesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    
    @NotBlank(message = "Order number is required")
    private String orderNumber;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    
    private String deliveryPartnerId;
    
    @NotNull(message = "Order items are required")
    private List<OrderItem> items;
    
    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", message = "Subtotal must be non-negative")
    private BigDecimal subtotal;
    
    @NotNull(message = "Delivery fee is required")
    @DecimalMin(value = "0.0", message = "Delivery fee must be non-negative")
    private BigDecimal deliveryFee;
    
    @NotNull(message = "Tax amount is required")
    @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
    private BigDecimal taxAmount;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
    
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private String paymentId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    
    private Address deliveryAddress;
    private String specialInstructions;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private OrderTracking tracking;
    
    @Data
   @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String menuItemId;
        private String menuItemName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private String variant;
        private String specialInstructions;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderTracking {
        private LocalDateTime orderPlaced;
        private LocalDateTime orderConfirmed;
        private LocalDateTime orderPrepared;
        private LocalDateTime orderPickedUp;
        private LocalDateTime orderDelivered;
        private Double currentLatitude;
        private Double currentLongitude;
        private String currentStatus;
        private String notes;
    }
    
    public enum OrderStatus {
        PENDING, CONFIRMED, PREPARING, READY_FOR_PICKUP, PICKED_UP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED, REFUNDED
    }
    
    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED, PARTIALLY_REFUNDED
    }
}
