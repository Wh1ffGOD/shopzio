package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayOrderResponse {

    private String razorpayOrderId;  // order_XXXXXXXXXX — used by frontend to open popup
    private String keyId;            // rzp_test_XXXX — frontend needs this to init Razorpay
    private BigDecimal amount;       // in INR (we convert to paise internally)
    private String currency;
    private Long internalOrderId;    // our DB order ID
}
