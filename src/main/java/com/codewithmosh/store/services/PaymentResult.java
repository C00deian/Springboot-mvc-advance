package com.codewithmosh.store.services;


import com.codewithmosh.store.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
