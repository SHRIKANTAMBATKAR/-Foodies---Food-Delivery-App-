package in.bushansirgur.foodiesapi.controller;

import in.bushansirgur.foodiesapi.model.Order;
import in.bushansirgur.foodiesapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrdersByCustomer(@PathVariable String customerId) {
        try {
            List<Order> orders = orderService.getOrdersByCustomer(customerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getOrdersByRestaurant(@PathVariable String restaurantId) {
        try {
            List<Order> orders = orderService.getOrdersByRestaurant(restaurantId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/delivery/{deliveryPartnerId}")
    public ResponseEntity<?> getOrdersByDeliveryPartner(@PathVariable String deliveryPartnerId) {
        try {
            List<Order> orders = orderService.getOrdersByDeliveryPartner(deliveryPartnerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, 
                                            @RequestBody Map<String, String> request) {
        try {
            Order.OrderStatus status = Order.OrderStatus.valueOf(request.get("status"));
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{orderId}/assign-delivery")
    public ResponseEntity<?> assignDeliveryPartner(@PathVariable String orderId, 
                                                @RequestBody Map<String, String> request) {
        try {
            String deliveryPartnerId = request.get("deliveryPartnerId");
            Order updatedOrder = orderService.assignDeliveryPartner(orderId, deliveryPartnerId);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{orderId}/delivery-location")
    public ResponseEntity<?> updateDeliveryLocation(@PathVariable String orderId, 
                                                 @RequestBody Map<String, Object> request) {
        try {
            Double latitude = Double.valueOf(request.get("latitude").toString());
            Double longitude = Double.valueOf(request.get("longitude").toString());
            Order updatedOrder = orderService.updateDeliveryLocation(orderId, latitude, longitude);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingOrders() {
        try {
            List<Order> orders = orderService.getPendingOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
