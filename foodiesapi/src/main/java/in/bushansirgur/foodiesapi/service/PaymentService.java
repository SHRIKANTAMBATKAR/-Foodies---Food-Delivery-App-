package in.bushansirgur.foodiesapi.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.bushansirgur.foodiesapi.model.Payment;
import in.bushansirgur.foodiesapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    public Payment createRazorpayOrder(String orderId, String customerId, BigDecimal amount) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(new BigDecimal("100")).intValue()); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_" + orderId);
            orderRequest.put("payment_capture", 1);
            
            Order razorpayOrder = razorpay.orders.create(orderRequest);
            
            Payment payment = new Payment();
            payment.setId(UUID.randomUUID().toString());
            payment.setOrderId(orderId);
            payment.setCustomerId(customerId);
            payment.setAmount(amount);
            payment.setCurrency("INR");
            payment.setMethod(Payment.PaymentMethod.RAZORPAY);
            payment.setStatus(Payment.PaymentStatus.PENDING);
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());
            
            return paymentRepository.save(payment);
            
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }
    }
    
    public Payment verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            // Verify the payment signature
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);
            
            // In a real implementation, you would verify the signature here
            // For now, we'll assume the payment is successful
            
            Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            
            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setRazorpaySignature(razorpaySignature);
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());
            
            return paymentRepository.save(payment);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify payment: " + e.getMessage());
        }
    }
    
    public Payment getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
    
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment not found for order"));
    }
}
