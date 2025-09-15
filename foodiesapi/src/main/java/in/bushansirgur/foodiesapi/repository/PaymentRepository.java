package in.bushansirgur.foodiesapi.repository;

import in.bushansirgur.foodiesapi.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByCustomerId(String customerId);
    List<Payment> findByOrderId(String orderId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    List<Payment> findByMethod(Payment.PaymentMethod method);
    
    @Query("{'razorpayOrderId': ?0}")
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    
    @Query("{'razorpayPaymentId': ?0}")
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
    
    @Query("{'transactionId': ?0}")
    Optional<Payment> findByTransactionId(String transactionId);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'status': 'COMPLETED', 'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Payment> findCompletedPaymentsBetween(LocalDateTime startDate, LocalDateTime endDate);
}
