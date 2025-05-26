package com.codewithmosh.store.exceptions;

import com.stripe.exception.StripeException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException {
public PaymentException(String message) {
    super(message);
}
}
