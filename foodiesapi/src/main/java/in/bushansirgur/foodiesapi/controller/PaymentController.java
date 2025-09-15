package in.bushansirgur.foodiesapi.controller;

import in.bushansirgur.foodiesapi.model.Payment;
import in.bushansirgur.foodiesapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/create-razorpay-order")
    public ResponseEntity<?> createRazorpayOrder(@RequestBody Map<String, Object> request) {
        try {
            String orderId = request.get("orderId").toString();
            String customerId = request.get("customerId").toString();
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            Payment payment = paymentService.createRazorpayOrder(orderId, customerId, amount);
            
            return ResponseEntity.ok(Map.of(
                "paymentId", payment.getId(),
                "razorpayOrderId", payment.getRazorpayOrderId(),
                "amount", payment.getAmount(),
                "currency", payment.getCurrency()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/verify-razorpay-payment")
    public ResponseEntity<?> verifyRazorpayPayment(@RequestBody Map<String, String> request) {
        try {
            String razorpayOrderId = request.get("razorpayOrderId");
            String razorpayPaymentId = request.get("razorpayPaymentId");
            String razorpaySignature = request.get("razorpaySignature");
            
            Payment payment = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "paymentId", payment.getId(),
                "status", payment.getStatus().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentById(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable String orderId) {
        try {
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
