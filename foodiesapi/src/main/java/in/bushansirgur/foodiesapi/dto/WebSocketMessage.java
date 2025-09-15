package in.bushansirgur.foodiesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String type;
    private String message;
    private Object data;
    private String timestamp;
    
    public static WebSocketMessage orderUpdate(String orderId, String status, Object data) {
        WebSocketMessage message = new WebSocketMessage();
        message.setType("ORDER_UPDATE");
        message.setMessage("Order " + orderId + " status updated to " + status);
        message.setData(data);
        message.setTimestamp(java.time.LocalDateTime.now().toString());
        return message;
    }
    
    public static WebSocketMessage deliveryUpdate(String orderId, Double latitude, Double longitude) {
        WebSocketMessage message = new WebSocketMessage();
        message.setType("DELIVERY_UPDATE");
        message.setMessage("Delivery location updated for order " + orderId);
        message.setData(new DeliveryLocation(orderId, latitude, longitude));
        message.setTimestamp(java.time.LocalDateTime.now().toString());
        return message;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryLocation {
        private String orderId;
        private Double latitude;
        private Double longitude;
    }
}
