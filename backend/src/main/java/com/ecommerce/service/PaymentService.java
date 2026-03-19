package com.ecommerce.service;

import com.ecommerce.dto.PaymentVerificationRequest;
import com.ecommerce.dto.RazorpayOrderRequest;
import com.ecommerce.dto.RazorpayOrderResponse;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Order;
import com.ecommerce.model.Payment;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentRepository;
import com.ecommerce.repository.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    // ── Step 1: Create Razorpay Order ─────────────────────────────────────────
    // Frontend calls this → gets razorpayOrderId + keyId → opens Razorpay popup
    @Transactional
    public RazorpayOrderResponse createOrder(RazorpayOrderRequest request) {
        try {
            User user = getCurrentUser();

            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            // Razorpay works in smallest currency unit (paise for INR)
            int amountInPaise = request.getAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .intValue();

            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", request.getCurrency());
            options.put("receipt", "receipt_order_" + request.getOrderId());

            com.razorpay.Order razorpayOrder = client.orders.create(options);
            String razorpayOrderId = razorpayOrder.get("id");

            // Save payment record
            Payment payment = Payment.builder()
                    .razorpayOrderId(razorpayOrderId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(Payment.PaymentStatus.CREATED)
                    .user(user)
                    .build();

            // Link to our order if provided
            if (request.getOrderId() != null) {
                Order order = orderRepository.findById(request.getOrderId())
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
                payment.setOrder(order);
                order.setStripePaymentIntentId(razorpayOrderId); // reusing field for razorpay order id
                orderRepository.save(order);
            }

            paymentRepository.save(payment);
            log.info("Razorpay order created: {} for user: {}", razorpayOrderId, user.getEmail());

            return RazorpayOrderResponse.builder()
                    .razorpayOrderId(razorpayOrderId)
                    .keyId(keyId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .internalOrderId(request.getOrderId())
                    .build();

        } catch (RazorpayException e) {
            log.error("Razorpay error: {}", e.getMessage());
            throw new RuntimeException("Payment order creation failed: " + e.getMessage());
        }
    }

    // ── Step 2: Verify Payment Signature ─────────────────────────────────────
    // After user pays, Razorpay returns 3 values to frontend.
    // Frontend sends them here for server-side verification.
    @Transactional
    public String verifyPayment(PaymentVerificationRequest request) {
        try {
            // Razorpay signature = HMAC-SHA256(orderId + "|" + paymentId, keySecret)
            String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
            String generatedSignature = generateHmacSha256(data, keySecret);

            if (!generatedSignature.equals(request.getRazorpaySignature())) {
                log.warn("Payment signature mismatch for order: {}", request.getRazorpayOrderId());
                throw new RuntimeException("Payment verification failed — invalid signature");
            }

            // Signature valid → update payment record
            Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setStatus(Payment.PaymentStatus.PAID);
            paymentRepository.save(payment);

            // Update our order status to CONFIRMED
            if (payment.getOrder() != null) {
                Order order = payment.getOrder();
                order.setStatus(Order.OrderStatus.CONFIRMED);
                order.setPaymentStatus("PAID");
                orderRepository.save(order);
                log.info("Order {} confirmed after successful payment", order.getId());
            }

            return "Payment verified successfully";

        } catch (Exception e) {
            log.error("Payment verification error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // ── HMAC-SHA256 signature generation ─────────────────────────────────────
    private String generateHmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
