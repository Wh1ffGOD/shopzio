package com.ecommerce.controller;

import com.ecommerce.dto.PaymentVerificationRequest;
import com.ecommerce.dto.RazorpayOrderRequest;
import com.ecommerce.dto.RazorpayOrderResponse;
import com.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "Razorpay payment endpoints")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ── Step 1: Create Razorpay Order ─────────────────────────────────────────
    // Frontend calls this → gets razorpayOrderId + keyId → opens Razorpay popup
    @Operation(
        summary = "Create a Razorpay Order",
        description = "Returns razorpayOrderId and keyId which the frontend uses to open the Razorpay checkout popup",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createOrder(
            @Valid @RequestBody RazorpayOrderRequest request) {
        return ResponseEntity.ok(paymentService.createOrder(request));
    }

    // ── Step 2: Verify Payment ────────────────────────────────────────────────
    // After user pays, frontend sends razorpayOrderId + paymentId + signature
    // Backend verifies HMAC-SHA256 signature and marks order as CONFIRMED
    @Operation(
        summary = "Verify Razorpay Payment",
        description = "Verifies the HMAC-SHA256 signature from Razorpay and marks order as paid",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequest request) {
        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }
}
