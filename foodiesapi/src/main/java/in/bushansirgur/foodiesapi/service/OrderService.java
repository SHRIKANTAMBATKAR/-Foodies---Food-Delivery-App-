package in.bushansirgur.foodiesapi.service;

import in.bushansirgur.foodiesapi.controller.WebSocketController;
import in.bushansirgur.foodiesapi.model.Order;
import in.bushansirgur.foodiesapi.model.Payment;
import in.bushansirgur.foodiesapi.repository.OrderRepository;
import in.bushansirgur.foodiesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final WebSocketController webSocketController;
    
    public Order createOrder(Order order) {
        // Generate order number
        order.setOrderNumber("ORD" + System.currentTimeMillis());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Initialize tracking
        Order.OrderTracking tracking = new Order.OrderTracking();
        tracking.setOrderPlaced(LocalDateTime.now());
        tracking.setCurrentStatus("PENDING");
        order.setTracking(tracking);
        
        Order savedOrder = orderRepository.save(order);
        
        // Send WebSocket notification
        webSocketController.sendOrderUpdate(savedOrder.getId(), "PENDING", savedOrder);
        
        return savedOrder;
    }
    
    public Order updateOrderStatus(String orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        // Update tracking
        Order.OrderTracking tracking = order.getTracking();
        if (tracking != null) {
            switch (status) {
                case CONFIRMED:
                    tracking.setOrderConfirmed(LocalDateTime.now());
                    break;
                case PREPARING:
                    tracking.setOrderPrepared(LocalDateTime.now());
                    break;
                case READY_FOR_PICKUP:
                    tracking.setOrderPrepared(LocalDateTime.now());
                    break;
                case PICKED_UP:
                    tracking.setOrderPickedUp(LocalDateTime.now());
                    break;
                case DELIVERED:
                    tracking.setOrderDelivered(LocalDateTime.now());
                    break;
            }
            tracking.setCurrentStatus(status.name());
        }
        
        Order savedOrder = orderRepository.save(order);
        
        // Send WebSocket notification
        webSocketController.sendOrderUpdate(orderId, status.name(), savedOrder);
        
        return savedOrder;
    }
    
    public Order assignDeliveryPartner(String orderId, String deliveryPartnerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if delivery partner exists and is available
        userRepository.findById(deliveryPartnerId)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
        
        order.setDeliveryPartnerId(deliveryPartnerId);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        // Send WebSocket notification
        webSocketController.sendOrderUpdate(orderId, "ASSIGNED", savedOrder);
        
        return savedOrder;
    }
    
    public Order updateDeliveryLocation(String orderId, Double latitude, Double longitude) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Order.OrderTracking tracking = order.getTracking();
        if (tracking != null) {
            tracking.setCurrentLatitude(latitude);
            tracking.setCurrentLongitude(longitude);
        }
        
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        
        // Send WebSocket notification
        webSocketController.sendDeliveryUpdate(orderId, latitude, longitude);
        
        return savedOrder;
    }
    
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    public List<Order> getOrdersByRestaurant(String restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }
    
    public List<Order> getOrdersByDeliveryPartner(String deliveryPartnerId) {
        return orderRepository.findByDeliveryPartnerId(deliveryPartnerId);
    }
    
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public List<Order> getPendingOrders() {
        return orderRepository.findPendingOrdersWithoutDeliveryPartner();
    }
}
