package com.omoke.store.checkout;

import com.omoke.store.order.Order;
import com.omoke.store.checkout.dtos.CheckoutSession;
import com.omoke.store.checkout.dtos.PaymentResult;
import com.omoke.store.checkout.dtos.WebhookRequest;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
