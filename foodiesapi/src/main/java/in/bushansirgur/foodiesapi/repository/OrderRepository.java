package in.bushansirgur.foodiesapi.repository;

import in.bushansirgur.foodiesapi.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByCustomerId(String customerId);
    List<Order> findByRestaurantId(String restaurantId);
    List<Order> findByDeliveryPartnerId(String deliveryPartnerId);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    @Query("{'customerId': ?0, 'status': {$in: ?1}}")
    List<Order> findByCustomerIdAndStatusIn(String customerId, List<Order.OrderStatus> statuses);
    
    @Query("{'restaurantId': ?0, 'status': {$in: ?1}}")
    List<Order> findByRestaurantIdAndStatusIn(String restaurantId, List<Order.OrderStatus> statuses);
    
    @Query("{'deliveryPartnerId': ?0, 'status': {$in: ?1}}")
    List<Order> findByDeliveryPartnerIdAndStatusIn(String deliveryPartnerId, List<Order.OrderStatus> statuses);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'orderNumber': ?0}")
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Query("{'status': 'PENDING', 'deliveryPartnerId': null}")
    List<Order> findPendingOrdersWithoutDeliveryPartner();
    
    @Query("{'status': 'READY_FOR_PICKUP', 'deliveryPartnerId': ?0}")
    List<Order> findReadyForPickupOrdersByDeliveryPartner(String deliveryPartnerId);
}
