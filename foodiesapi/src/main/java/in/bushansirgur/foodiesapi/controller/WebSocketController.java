package in.bushansirgur.foodiesapi.controller;

import in.bushansirgur.foodiesapi.dto.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/order.update")
    @SendTo("/topic/order.updates")
    public WebSocketMessage handleOrderUpdate(WebSocketMessage message) {
        return message;
    }
    
    @MessageMapping("/delivery.location")
    @SendTo("/topic/delivery.updates")
    public WebSocketMessage handleDeliveryLocation(WebSocketMessage message) {
        return message;
    }
    
    public void sendOrderUpdate(String orderId, String status, Object data) {
        WebSocketMessage message = WebSocketMessage.orderUpdate(orderId, status, data);
        messagingTemplate.convertAndSend("/topic/order.updates", message);
    }
    
    public void sendDeliveryUpdate(String orderId, Double latitude, Double longitude) {
        WebSocketMessage message = WebSocketMessage.deliveryUpdate(orderId, latitude, longitude);
        messagingTemplate.convertAndSend("/topic/delivery.updates", message);
    }
}
